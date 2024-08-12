package com.todense.viewmodel;

import com.todense.TestUtil.TestBackgroundScopeDummy;
import com.todense.TestUtil.TestGraphScopeDummy;
import com.todense.TestUtil.TestNotificationCenterDummy;
import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.paint.Color;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runners.MethodSorters;

import javax.swing.text.View;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SolverViewModelTest {


    private static final String DEFAULT_IP = "127.0.0.1";
    private static final String DEFAULT_PORT = "1337";

    TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();
    TestGraphScopeDummy graphScope = new TestGraphScopeDummy();
    TestBackgroundScopeDummy backgroundScope = new TestBackgroundScopeDummy();

    SolverViewModel viewModel = new SolverViewModel(graphScope, backgroundScope, notificationCenter);



    @Before
    public void setUp() {
        /*
        notificationCenter = new TestNotificationCenterDummy();
        graphScope = new TestGraphScopeDummy();
        backgroundScope = new TestBackgroundScopeDummy();
        
        viewModel = new SolverViewModel(graphScope, backgroundScope, notificationCenter);
        viewModel.initialize();
        */

    }

    @After
    public void tearDown() {
        viewModel.stop();
        notificationCenter.getGraphToPublish(null);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Thread sleep interrupted");
        }
    }


    @Test
    public void defaultBehavior(){

        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());

    }

    @Test
    public void stop(){


        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.stop();

        assertEquals("Coloration stopped", notificationCenter.getMessagesToPublish().get(3));
    }

    @Test
    public void useServer(){


        int port = getRandomOpenPort();
        try{
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "ILP-Server.jar", "-s", "any", "-p", String.valueOf(port));
            Process serverProcess = pb.start();

        } catch (IOException e) {
            fail("Server could not be started");
        }


        viewModel.start(false, null, false, false, true, DEFAULT_IP, String.valueOf(port));
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
    }


    @Test
    public void testMultipleThreads(){


        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);
        viewModel.start(false, null, false, false, false, DEFAULT_IP, DEFAULT_PORT);

        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
    }


    @Test
    public void testPreferColorConstraint(){


        viewModel.start(true, Color.rgb(230,150,80), false, false, false, DEFAULT_IP, DEFAULT_PORT);
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
    }

    @Test
    public void testSimilarColoringConstraint(){


        viewModel.start(false, null, true, false, false, DEFAULT_IP, DEFAULT_PORT);
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
    }

    @Test
    public void testCurrentColorsConstraint(){


        viewModel.start(false, null, false, true, false, DEFAULT_IP, DEFAULT_PORT);
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
    }

    @Test
    public void testPreferWithCurrentColors(){


        viewModel.start(true, Color.rgb(230,150,80), false, true, false, DEFAULT_IP, DEFAULT_PORT);
        waitFor(viewModel);
        assertNotNull(notificationCenter.getGraphToPublish());
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





        assertTrue(viewModel.saveServerConfig(DEFAULT_IP, DEFAULT_PORT));
    }

    @Test
    public void saveServerInputFileDoesntExist(){

        File readConfig = new File("ServerConfig.cfg");
        readConfig.delete();




        assertTrue(viewModel.saveServerConfig(DEFAULT_IP, DEFAULT_PORT));
    }



     private int getRandomOpenPort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException("Unable to find an open port", e);
        }
    }

    private void waitFor(SolverViewModel viewModel){
        while (viewModel.isRunning()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                fail("Thread sleep interrupted");
            }
        }
    }



}
