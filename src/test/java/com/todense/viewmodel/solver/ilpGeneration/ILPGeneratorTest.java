package com.todense.viewmodel.solver.ilpGeneration;

import com.google.gson.Gson;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.ObjectInputFilter;

import static org.junit.jupiter.api.Assertions.*;

class ILPGeneratorTest {

    private class TestCase {
        public boolean preferColor;
        public String color;
        public boolean tryKeepCurrentColors;
        public boolean forceCurrentColors;
        public String testGraph;
        public String expectedResult;
    }

    @Test
    void testAllTestFiles(){
        File testFolder = new File("src\\test\\resources\\ILPGenerationTests\\TestCases");
        File[] testCases = testFolder.listFiles();
        assertNotNull(testCases, "No test folder");
        for (File file : testCases) {
            if (file.isFile()) {
                try {
                    FileReader reader = new FileReader(file);
                    Gson gson = new Gson();
                    TestCase testCase = gson.fromJson(reader, TestCase.class);
                    //find the ILP-Type
                    boolean tryKeepCurrentColors = testCase.tryKeepCurrentColors;
                    boolean preferColor = testCase.preferColor;
                    boolean forceCurrentColors = testCase.forceCurrentColors;
                    ILPType type = ILPType.MINCOLORS;
                    if(tryKeepCurrentColors){
                        type = ILPType.SIMILARCOLORS;
                    } else if(preferColor && !forceCurrentColors){
                        type = ILPType.WITHLOWCOLORS;
                        type.setMaximizeColor(Color.web(testCase.color));
                    } else if(preferColor){
                        type = ILPType.WITHLOWSETCOLORS;
                        type.setMaximizeColor(Color.web(testCase.color));
                    } else if(forceCurrentColors){
                        type = ILPType.WITHSETCOLORS;
                    }
                    //get the test graph
                    File graphFile = new File("src\\test\\resources\\ILPGenerationTests\\TestGraphs\\" +
                            testCase.testGraph);
                    OgrReader graphReader = new OgrReader();
                    Graph testGraph = graphReader.readGraph(graphFile);
                    ILPProblem ilpProblem = ILPGenerator.generateILP(testGraph, type);
                    String result = ilpProblem.getILPAsJsonString();
                    assertEquals(testCase.expectedResult, result, "failed at: " + file.getName());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    void tooManyAlreadySetColors() {
        //K3 graph
        Graph graph = new Graph();
        Node n1 = graph.addNode();
        Node n2 = graph.addNode();
        Node n3 = graph.addNode();
        Edge e1 = graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        Edge e2 = graph.addEdge(graph.getNodes().get(1), graph.getNodes().get(2));
        Edge e3 = graph.addEdge(graph.getNodes().get(2), graph.getNodes().get(0));
        n1.setColor(Color.web("0xff0000"));
        n2.setColor(Color.web("0xf00000"));
        n3.setColor(Color.web("0x00ff00"));
        e1.setColor(Color.web("0x00f000"));
        e2.setColor(Color.web("0x0000ff"));
        e3.setColor(Color.web("0x0000f0"));
        ILPType type = ILPType.WITHLOWSETCOLORS;
        type.setMaximizeColor(Color.web("0xffff00"));
        ILPProblem ilp = ILPGenerator.generateILP(graph, type);

        /* graph has more than (maxDegree + 2) Colors already set => ilp Problem makes no sense and should be null
        according to the javadoc */
        assertNull(ilp);

        ILPProblem ilp2 = ILPGenerator.generateILP(graph, ILPType.WITHSETCOLORS);
        assertNull(ilp2);
    }

}