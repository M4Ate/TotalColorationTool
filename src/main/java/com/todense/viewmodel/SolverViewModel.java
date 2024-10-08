package com.todense.viewmodel;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import com.todense.application.StarterInvoker;
import com.todense.model.graph.Graph;
import com.todense.util.GraphCopy;
import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.BackgroundScope;

import java.io.*;
import java.net.ServerSocket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import java.util.Objects;

/**
 * ViewModel for the ILP Solver.
 */
public class SolverViewModel implements ViewModel {

    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1337";

    private GraphManager graphManager;


    @InjectScope
    GraphScope graphScope;

    @InjectScope
    BackgroundScope backgroundScope;

    @Inject
    NotificationCenter notificationCenter;

    private Thread solverThread;

    private static int openPort;

    /*
     * Default Constructor
     */
    SolverViewModel() {
    }

    /**
     * Constructor for the ILP-Solver ViewModel.
     *
     * @param graphScope - the graphScope of the current graph.
     * @param backgroundScope - the backgroundScope of the current background.
     * @param notificationCenter - the notificationCenter for the current application.
     */
    SolverViewModel(GraphScope graphScope, BackgroundScope backgroundScope, NotificationCenter notificationCenter) {
        this.graphScope = graphScope;
        this.backgroundScope = backgroundScope;
        this.notificationCenter = notificationCenter;
        initialize();
    }


    /**
     * This method is used to initialize the ILP-Solver ViewModel.
     */
    public void initialize() {
        graphManager = graphScope.getGraphManager();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (solverThread != null) {
                solverThread.interrupt();
            }
        }));

        openPort = getRandomOpenPort();

        File serverJar = new File("ILP-Server.jar");
        if (!serverJar.exists()) {
            if (!extractServer()) {
                notificationCenter.publish(MainViewModel.TASK_FINISHED, "Couldn't extract server.");
            }
        }


        try{

            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ILP-Server.jar", "-s", "any", "-p", String.valueOf(openPort));
            Process serverProcess = pb.start();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (serverProcess.isAlive()) {
                    serverProcess.destroy();
                }
            }));

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }


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

        if(solverThread == null || !solverThread.isAlive()){
            notificationCenter.publish(MainViewModel.TASK_STARTED, "Calculating Coloration");
            solverThread = new Thread(() -> calculateColoration(preferColor, preferredColor, similarColoring, currentColors, useServer, IP, Port));
            solverThread.start();
        }
    }

    /**
     * This method is used to stop the current coloration.
     */
    public void stop(){
        if(solverThread != null && solverThread.isAlive()){
            solverThread.interrupt();
            solverThread = null;
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "Coloration stopped");
        }
    }

    /**
     * This method is used to check if the solver is currently running.
     *
     * @return true if the solver is running, false otherwise.
     */
    public boolean isRunning(){
        return solverThread != null && solverThread.isAlive();
    }



    private void calculateColoration(Boolean preferColor, Color preferredColor, Boolean similarColoring,
                      Boolean currentColors, Boolean useServer, String IP, String Port){

        ILPType type = ILPType.MINCOLORS;

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
        Graph currentGraph = GraphCopy.copyGraphWithColorsAndID(graphManager.getGraph());

        ILPProblem problem = ILPGenerator.generateILP(currentGraph, type);
        if(problem == null) {
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "too many unique colors used");
            return;
        }
        String jsonString = problem.getILPAsJsonString();

        //Server request on other Thread or async
        String responseString;



        try {
            if (useServer) responseString = requestServer(IP, Port, jsonString);
            else responseString = requestServer(DEFAULT_IP, String.valueOf(openPort), jsonString);

            JsonObject responseObject = JsonParser.parseString(responseString).getAsJsonObject();
            boolean error = responseObject.get("error").getAsBoolean();

            if (error) {
                notificationCenter.publish(MainViewModel.TASK_FINISHED,
                        "Server returned an error: "
                                + responseObject.get("errorMessage").getAsString());
                System.out.println(responseObject.get("errorMessage").getAsString());
                return;
            }

        } catch (IllegalArgumentException e) {
            notificationCenter.publish(MainViewModel.TASK_FINISHED,
                    "Invalid IP or Port. Please check the server settings. Error: "
                            + e.getMessage());
            System.out.println(e.getMessage());
            
            return;
        } catch (IOException e) {
            if (e.getMessage() == null) {
                notificationCenter.publish(MainViewModel.TASK_FINISHED,
                            "Couldn't properly connect to server.");
                return;
            }
            System.out.println(e.getMessage());
            return;
        } catch(InterruptedException e){
            return;
        }

        Graph newGraph = GraphColorer.getColoredGraph(currentGraph, problem, responseString, getBackgroundColor()); //Graph, ILP Problem und Type

        //Set the now colored graph as the new graph.
        try{
            notificationCenter.publish(GraphViewModel.NEW_GRAPH_REQUEST, newGraph);
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "Graph colored");
        } catch (RuntimeException e){
            notificationCenter.publish(MainViewModel.TASK_FINISHED, e.getMessage());
        }

    }

    private Color getBackgroundColor(){
        return backgroundScope.getBackgroundColor();
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
     * Method to send a request to the server.
     *
     * @param IP - the IP of the server.
     * @param Port - the Port of the server.
     * @param jsonString - the ILP-Problem as a JSON String
     * @return the JSON Response
     * @throws IOException - If the request could not be sent.
     * @throws InterruptedException - If the request was interrupted.
     */
    private String requestServer(String IP, String Port, String jsonString) throws IOException, InterruptedException{
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://" + IP + ":" + Port + "/post"))
                .POST(HttpRequest.BodyPublishers.ofString(jsonString))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }


    private static int getRandomOpenPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Unable to find an open port", e);
        }
    }

    private boolean extractServer() {
        String innerJarName = "ILP-Server.jar";

        try {
            // Get the directory where the outer JAR is located
            File outerJarDir = new File(
                StarterInvoker.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile();

            // Create a file object for the inner JAR
            File innerJarFile = new File(outerJarDir, innerJarName);

            // Get the resource stream of the inner JAR
            try (InputStream is = StarterInvoker.class.getResourceAsStream("/" + innerJarName)) {
                Objects.requireNonNull(is, "Inner JAR not found in resources.");

                // Copy the inner JAR resource to the target file
                Files.copy(is, innerJarFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            System.out.println("Extracted " + innerJarName + " to " + innerJarFile.getAbsolutePath());
            return true;

        } catch (URISyntaxException | IOException e) {
            return false;
        }
    }
}
