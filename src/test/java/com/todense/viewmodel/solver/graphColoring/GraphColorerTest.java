package com.todense.viewmodel.solver.graphColoring;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.solver.ilpGeneration.ILPGenerator;
import com.todense.viewmodel.solver.ilpGeneration.ILPProblem;
import com.todense.viewmodel.solver.ilpGeneration.ILPType;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphColorerTest {

    @Test
    void getColoredGraphTest() {
        //K3 graph
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        graph.addEdge(graph.getNodes().get(1), graph.getNodes().get(2));
        graph.addEdge(graph.getNodes().get(2), graph.getNodes().get(0));
        graph.getNodes().get(2).setColor(Color.web("0x00FF00"));
        ILPType type = ILPType.WITHLOWSETCOLORS;
        type.setMaximizeColor(Color.web("0x00FF00"));
        ILPProblem ilp = ILPGenerator.generateILP(graph, type);
        String jsonResponse = "{\"error\":false,\"errorMessage\":\"\",\"result\":[{\"variable\":\"x_v0_c0\","+
                "\"value\":1},{\"variable\":\"x_v0_c1\",\"value\":0},{\"variable\":\"x_v0_c2\",\"value\":0},"+
                "{\"variable\":\"x_v0_c3\",\"value\":0},{\"variable\":\"x_v1_c0\",\"value\":0},{\"variable\":"+
                "\"x_v1_c1\",\"value\":0},{\"variable\":\"x_v1_c2\",\"value\":1},{\"variable\":\"x_v1_c3\","+
                "\"value\":0},{\"variable\":\"x_v2_c0\",\"value\":0},{\"variable\":\"x_v2_c1\",\"value\":1},"+
                "{\"variable\":\"x_v2_c2\",\"value\":0},{\"variable\":\"x_v2_c3\",\"value\":0},{\"variable\":"+
                "\"y_e0_c0\",\"value\":0},{\"variable\":\"y_e0_c1\",\"value\":1},{\"variable\":\"y_e0_c2\","+
                "\"value\":0},{\"variable\":\"y_e0_c3\",\"value\":0},{\"variable\":\"y_e1_c0\",\"value\":1},"+
                "{\"variable\":\"y_e1_c1\",\"value\":0},{\"variable\":\"y_e1_c2\",\"value\":0},{\"variable\":"+
                "\"y_e1_c3\",\"value\":0},{\"variable\":\"y_e2_c0\",\"value\":0},{\"variable\":\"y_e2_c1\","+
                "\"value\":0},{\"variable\":\"y_e2_c2\",\"value\":1},{\"variable\":\"y_e2_c3\",\"value\":0},"+
                "{\"variable\":\"z_c0\",\"value\":1},{\"variable\":\"z_c1\",\"value\":1},{\"variable\":\"z_c2\","+
                "\"value\":1},{\"variable\":\"z_c3\",\"value\":0}]}";
        Graph result = GraphColorer.getColoredGraph(graph, ilp, jsonResponse);
        assertEquals("0x00ff00ff", result.getNodes().get(0).getColor().toString());
        assertEquals("0xffe502ff", result.getNodes().get(1).getColor().toString());
        assertEquals("0xff00f6ff", result.getNodes().get(2).getColor().toString());
        assertEquals("0xff00f6ff", result.getEdges().get(0).getColor().toString());
        assertEquals("0x00ff00ff", result.getEdges().get(1).getColor().toString());
        assertEquals("0xffe502ff", result.getEdges().get(2).getColor().toString());
    }

    @Test
    void getColoredGraphErrorTest() {
        //K3 graph
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        graph.addEdge(graph.getNodes().get(1), graph.getNodes().get(2));
        graph.addEdge(graph.getNodes().get(2), graph.getNodes().get(0));
        ILPType type = ILPType.MINCOLORS;
        ILPProblem ilp = ILPGenerator.generateILP(graph, type);
        //JsonString has an error and not all Variables have been set
        String jsonResponse = "{\"error\":true,\"errorMessage\":\"something went wrong\"," +
                "\"result\":[{\"variable\":\"x_v0_c0\",\"value\":1}]}";
        Graph result = GraphColorer.getColoredGraph(graph, ilp, jsonResponse);
        //result should be null according to the Javadoc
        assertNull(result);
    }
}