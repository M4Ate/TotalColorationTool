package com.todense.viewmodel;

import com.google.inject.Inject;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphManager;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import com.todense.viewmodel.scope.GraphScope;
import com.todense.viewmodel.solver.graphColoring.GraphColorer;
import com.todense.viewmodel.solver.ilpGeneration.ILPGenerator;
import com.todense.viewmodel.solver.ilpGeneration.ILPProblem;
import com.todense.viewmodel.solver.ilpGeneration.ILPType;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.scene.paint.Color;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ViewModel for the ILP Solver.
 */
public class SolverViewModel implements ViewModel {

    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1337";

    private GraphManager graphManager;


    @InjectScope
    GraphScope graphScope;

    @Inject
    NotificationCenter notificationCenter;

    private Thread solverThread = new Thread();

    public void initialize() {
        graphManager = graphScope.getGraphManager();
    }


    /**
     *
     * This method is used when the user wants a coloration for the TotalColoring Issue.
     * It first creates the Problem with the given ILP Type.
     * It then parses it to a JSON and sends it to a server.
     * After receiving the answer the graph gets colored accordingly and then set.
     *
     * @param preferColor - whether the user wants to use a color as often as possible or not.
     * @param preferredColor - the color the user wants to use as often as possible.
     * @param similarColoring - will definitely use the current colors.
     * @param currentColors - will try to use the current colors.
     * @param useServer - whether the user wants to use an external server or not.
     * @param IP - the IP of the server the user wants to use.
     * @param Port - the port in which the server is waiting for the request.
     */
    public void start(Boolean preferColor, Color preferredColor, Boolean similarColoring,
                      Boolean currentColors, Boolean useServer, String IP, String Port){

        String IPv4_Regex = "^((25[0-5]|(2[0-4]|1\\d|[1-9]|)\\d)\\.?\\b){4}$";
        Pattern pattern = Pattern.compile(IPv4_Regex);
        Matcher matcher = pattern.matcher(IP);

        if(!matcher.matches() && useServer){
            notificationCenter.publish(MainViewModel.WRITE, "Not a valid IPv4");
            return;
        }

        if(!solverThread.isAlive()){
            notificationCenter.publish(MainViewModel.TASK_STARTED, "Calculating Coloration");
            solverThread = new Thread(() -> calculateColoration(preferColor, preferredColor, similarColoring, currentColors, useServer, IP, Port));
            solverThread.start();
        }
    }

    /**
     * This method is used to stop the current coloration.
     */
    public void stop(){
        if(solverThread.isAlive()){
            solverThread.interrupt();
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "Coloration stopped");
        }
    }


    private void calculateColoration(Boolean preferColor, Color preferredColor, Boolean similarColoring,
                      Boolean currentColors, Boolean useServer, String IP, String Port){

        ILPType type = ILPType.MINCOLORS;
        Process startServer = null;

        //Decide on the type of ILP-Problem we have.
        if(similarColoring){
            type = ILPType.SIMILARCOLORS;
        } else if(preferColor && !currentColors){
            type = ILPType.WITHLOWCOLORS;
            type.setMaximizeColor(preferredColor);
        } else if(preferColor){
            type = ILPType.WITHLOWSETCOLORS;
            type.setMaximizeColor(preferredColor);
        } else if(currentColors){
            type = ILPType.WITHSETCOLORS;
        }

        //Copy graph so the user can't change it.
        Graph currentGraph = graphManager.getGraph();   //.copy()

        ILPProblem problem = ILPGenerator.generateILP(currentGraph, type);
        String jsonString = problem.getILPAsJsonString();

        System.out.println(jsonString);

        //Anfrage an Server machen eventuell anderer Thread oder async
        String responseString;

        if(!useServer){
            try{

                startServer = Runtime.getRuntime().exec("java -jar ILP-Solver.jar");

                System.out.println("Started the server on localhost.");


            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        try {
            if (useServer) responseString = requestServer(IP, Port, jsonString);
            else responseString = requestServer(DEFAULT_IP, DEFAULT_PORT, jsonString);

        } catch (IOException | InterruptedException e) {
            notificationCenter.publish(MainViewModel.TASK_FINISHED, e.getMessage());
            return;
        }

        if(!useServer && startServer != null){startServer.destroyForcibly();}

        Graph newGraph = GraphColorer.getColoredGraph(currentGraph, problem, responseString); //Graph, ILP Problem und Type

        //Set the now colored graph as the new graph.
        try{
            notificationCenter.publish(GraphViewModel.NEW_GRAPH_REQUEST, newGraph);
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "Graph colored");
        } catch (RuntimeException e){
            notificationCenter.publish(MainViewModel.TASK_FINISHED, e.getMessage());
        }
    }

    /**
     * This method tries to read the saved server values onstart.
     * @return the read server values or otherwise the standard values.
     */
    public String[] loadSavedServerValues(){

        String[] array = new String[2];

        array[0] = DEFAULT_IP;
        array[1] = DEFAULT_PORT;

        String data = "";

        try {
            File readConfig = new File("ServerConfig.cfg");
            Scanner myReader = new Scanner(readConfig);
            while (myReader.hasNextLine()) {
                data = myReader.nextLine();
            }
            myReader.close();

            String[] extract = data.split(" ");

            array[0] = extract[1];
            array[1] = extract[3];

        } catch (FileNotFoundException e) {
            System.out.println("No server config file found.");

        } catch(ArrayIndexOutOfBoundsException e){
            System.out.println("Couldn't resolve the file. Please check that it matches the format.");
        }

        return array;
    }

    /**
     * This method is used to store new values for an external server.
     * @param IP - the IP of the external server.
     * @param Port - the port on which the server is listening.
     * @return the boolean value if the save was successful
     */
    public boolean saveServerConfig(String IP, String Port) {
        boolean saveSuccess = true;

        try {
            File file = new File("ServerConfig.cfg");

            if (file.createNewFile()) {
                FileWriter configWriter = new FileWriter(file.getName());
                configWriter.write("Server_IP: " + IP + " " + "Server_Port: " + Port);
                configWriter.close();

            } else {
                FileWriter configWriter = new FileWriter(file.getName());
                configWriter.write("");
                configWriter.write("Server_IP: " + IP + " " + "Server_Port: " + Port);
                configWriter.close();
            }

        } catch (IOException e) {
            saveSuccess = false;
            notificationCenter.publish(MainViewModel.WRITE, e.getMessage());

        } finally {
            if(saveSuccess){
                notificationCenter.publish(MainViewModel.WRITE, "Server config saved");
            }
        }
        return saveSuccess;
    }

    /**
     * This method TODO MAX??s
     *
     * @param IP - the IP of the server.
     * @param Port - the Port of the server.
     * @param jsonString - the ILP-Problem as a JSON String
     * @return the JSON Response
     * @throws IOException - TODO MAX??
     * @throws InterruptedException TODO MAX??
     */
    private String requestServer(String IP, String Port, String jsonString) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + IP + ":" + Port + "/post"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

}
