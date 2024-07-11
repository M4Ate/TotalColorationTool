package com.todense.viewmodel;

import com.google.inject.Inject;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.GraphScope;
import com.todense.viewmodel.solver.JsonBuilder;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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


    public void start(Boolean preferColor, Color preferredColor, Boolean brightColoring, Boolean currentColorsToggle, Boolean useServer, String IP, String Port){
        System.out.println(preferColor);
        System.out.println(preferredColor);
        System.out.println(brightColoring);
        System.out.println(currentColorsToggle);
        System.out.println(useServer);
        System.out.println(IP);
        System.out.println(Port);
    }

    /**
     *  uses an ILP solver to generate a minimal total coloring of the currently displayed graph
     */
    public void colorWithILPSolver(){
        Graph graph = graphManager.getGraph();
        String jsonRequest = JsonBuilder.generateConstraintsJson(graph);

        //request to server

        //color graph according to ilp solution

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
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        return array;
    }

    public boolean saveServerConfig(String IP, String Port) {
        boolean saveSuccess = true;

        try {
            File file = new File("ServerConfig.txt");

            if (file.createNewFile()) {
                FileWriter configWriter = new FileWriter(file.getName());
                configWriter.write("Server_IP:" + IP + " " + "Server_Port" + Port);
                configWriter.close();

            } else {
                System.out.println("File already exists.");

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
