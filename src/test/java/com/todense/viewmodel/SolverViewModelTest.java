package com.todense.viewmodel;

import com.todense.TestUtil.TestGraphScopeDummy;
import com.todense.TestUtil.TestNotificationCenterDummy;
import javafx.scene.paint.Color;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.*;

public class SolverViewModelTest {

    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1337";

    TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();
    TestGraphScopeDummy graphScope = new TestGraphScopeDummy();


    @Test
    public void defaultBehavior(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();
    }

    @Test
    public void testMultipleThreads(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);

    }

    @Test
    public void testPreferColorConstraint(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(true, Color.rgb(230,150,80), false, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();
    }

    @Test
    public void testSimilarColoringConstraint(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(false, null, true, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();
    }

    @Test
    public void testCurrentColorsConstraint(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(false, null, false, true, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();
    }

    @Test
    public void testPreferWithCurrentColors(){
        SolverViewModel viewModel = new SolverViewModel(notificationCenter, graphScope);
        viewModel.initialize();
        viewModel.start(true, Color.rgb(230,150,80), false, true, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();
    }

    @Test
    public void loadServerValuesNoFile(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();

        String[] array = new String[2];

        array[0] = DEFAULT_IP;
        array[1] = DEFAULT_PORT;

        SolverViewModel viewModel = new SolverViewModel();

        String[] testReturn = viewModel.loadSavedServerValues();

        assertEquals(array, testReturn);
    }

    @Test
    public void loadServerValuesWrongFormat(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();
        try {
            File file = new File("ServerConfig.cfg");
            FileWriter configWriter = new FileWriter(file.getName());
            configWriter.write("Server_IP:128.0.2.3Server_Port:1332");
            configWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] array = new String[2];

        array[0] = DEFAULT_IP;
        array[1] = DEFAULT_PORT;

        SolverViewModel viewModel = new SolverViewModel();

        String[] testReturn = viewModel.loadSavedServerValues();

        assertEquals(array, testReturn);
    }

    @Test
    public void loadSavedServerValuesWorking(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();
        try {
            File file = new File("ServerConfig.cfg");
            FileWriter configWriter = new FileWriter(file.getName());
            configWriter.write("Server_IP: test.server.net Server_Port: 80");
            configWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String[] array = new String[2];

        array[0] = "test.server.net";
        array[1] = "80";

        SolverViewModel viewModel = new SolverViewModel();

        String[] testReturn = viewModel.loadSavedServerValues();

        assertEquals(array, testReturn);
    }

    @Test
    public void saveServerInputFileExists(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();
        try {
            File file = new File("ServerConfig.cfg");
            FileWriter configWriter = new FileWriter(file.getName());
            configWriter.write("Server_IP: test.server.net Server_Port: 80");
            configWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();

        SolverViewModel viewModel = new SolverViewModel(notificationCenter);

        assertTrue(viewModel.saveServerConfig(DEFAULT_IP, DEFAULT_PORT));
    }

    @Test
    public void saveServerInputFileDoesntExist(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();

        TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();

        SolverViewModel viewModel = new SolverViewModel(notificationCenter);

        assertTrue(viewModel.saveServerConfig(DEFAULT_IP, DEFAULT_PORT));
    }

}
