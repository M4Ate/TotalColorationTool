package com.todense.viewmodel;

import com.google.inject.Inject;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphManager;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.io.IOException;

/*
 * ViewModel for the ILP Solver.
 */
public class SolverViewModel implements ViewModel {

    private GraphManager graphManager;

    @InjectScope
    GraphScope graphScope;

    @Inject
    NotificationCenter notificationCenter;

    public void initialize() {
        graphManager = graphScope.getGraphManager();
    }


    public void start(Boolean preferColor, Color preferredColor, Boolean similarColoring,
                      Boolean currentColors, Boolean useServer, String IP, String Port){

        notificationCenter.publish(MainViewModel.TASK_STARTED, "Calculating Coloration");

        ILPType type = ILPType.MINCOLORS;

        if(similarColoring){
            type = ILPType.SIMILARCOLORS;
        } else if(preferColor && !currentColors){
            type = ILPType.WITHLOWCOLORS;
            type.setMaximizeColor(preferredColor);
        } else if(preferColor && currentColors){
            type = ILPType.WITHLOWSETCOLORS;
            type.setMaximizeColor(preferredColor);
        } else if(!preferColor && currentColors){
            type = ILPType.WITHSETCOLORS;
        }

        Graph currentGraph = graphManager.getGraph().copy();

        ILPProblem problem = ILPGenerator.generateILP(currentGraph, type);

        String jsonString = problem.getILPAsJsonString();

        System.out.println(jsonString);

        //Anfrage an Server machen eventuell anderer Thread oder async

        //Graph newGraph = GraphColorer.getColoredGraph(currentGraph, problem, jsonResponse); //Graph, ILP Problem und Type

        //Hier sollte der Graph mit gesetzt werden als neuer
        //TODO: currentGraph gegen den newGraph austauschen
        try{
            notificationCenter.publish(GraphViewModel.NEW_GRAPH_REQUEST, currentGraph);
            notificationCenter.publish(MainViewModel.TASK_FINISHED, "Graph colored");
        } catch (RuntimeException e){
            notificationCenter.publish(MainViewModel.TASK_FINISHED, e.getMessage());
        }
    }

    public String[] loadSavedServerValues(){

        String[] array = new String[2];

        array[0] = "127.0.0.1";
        array[1] = "1337";

        String data = "";

        try {
            File readConfig = new File("ServerConfig.txt");
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
            System.out.println("Couldn't resolve the file. Please check the format.");
        }

        return array;
    }

    public boolean saveServerConfig(String IP, String Port) {
        boolean saveSuccess = true;

        try {
            File file = new File("ServerConfig.txt");

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

}
