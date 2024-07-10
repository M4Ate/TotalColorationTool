package com.todense.viewmodel.solver.ilpGeneration;

import com.todense.model.graph.Graph;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ILPGeneratorTest {

    @Test
    void generateMinColorsILP() {
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        ILPProblem ilp = ILPGenerator.generateILP(graph, ILPType.MINCOLORS);
        String jsonString = ilp.getILPAsJsonString();
        String expected = "{\"variables\":[\"x_v0_c0\",\"x_v0_c1\",\"x_v0_c2\",\"x_v1_c0\",\"x_v1_c1\"," +
                "\"x_v1_c2\",\"y_e0_c0\",\"y_e0_c1\",\"y_e0_c2\",\"z_c0\",\"z_c1\",\"z_c2\"],\"constraints\"" +
                ":[\"x_v0_c0 + x_v0_c1 + x_v0_c2 = 1\",\"x_v1_c0 + x_v1_c1 + x_v1_c2 = 1\"," +
                "\"y_e0_c0 + y_e0_c1 + y_e0_c2 = 1\",\"x_v0_c0 + x_v1_c0 <= 1\",\"x_v0_c1 + x_v1_c1 <= 1\"," +
                "\"x_v0_c2 + x_v1_c2 <= 1\",\"x_v0_c0 + y_e0_c0 <= 1\",\"x_v0_c1 + y_e0_c1 <= 1\"," +
                "\"x_v0_c2 + y_e0_c2 <= 1\",\"x_v1_c0 + y_e0_c0 <= 1\",\"x_v1_c1 + y_e0_c1 <= 1\"," +
                "\"x_v1_c2 + y_e0_c2 <= 1\",\"x_v0_c0 - z_c0 <= 0\",\"x_v0_c1 - z_c1 <= 0\",\"x_v0_c2 - z_c2 <= 0\"," +
                "\"x_v1_c0 - z_c0 <= 0\",\"x_v1_c1 - z_c1 <= 0\",\"x_v1_c2 - z_c2 <= 0\",\"y_e0_c0 - z_c0 <= 0\"," +
                "\"y_e0_c1 - z_c1 <= 0\",\"y_e0_c2 - z_c2 <= 0\"],\"optimizationFunction\":\"z_c0 + z_c1 + z_c2\"," +
                "\"minimize\":false}";
        assertEquals(expected, jsonString);
    }

    @Test
    void generateSetColorILP(){
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        graph.getNodes().get(0).setColor(Color.web("0x00FF00"));
        graph.getEdges().get(0).setColor(Color.web("0x0000FF"));
        ILPProblem ilp = ILPGenerator.generateILP(graph, ILPType.WITHSETCOLORS);
        String jsonString = ilp.getILPAsJsonString();
        String expected = "{\"variables\":[\"x_v0_c0\",\"x_v0_c1\",\"x_v0_c2\",\"x_v1_c0\",\"x_v1_c1\",\"x_v1_c2\"," +
                "\"y_e0_c0\",\"y_e0_c1\",\"y_e0_c2\",\"z_c0\",\"z_c1\",\"z_c2\"],\"constraints\":" +
                "[\"x_v0_c0 + x_v0_c1 + x_v0_c2 = 1\",\"x_v0_c0 = 1\",\"x_v1_c0 + x_v1_c1 + x_v1_c2 = 1\"," +
                "\"y_e0_c0 + y_e0_c1 + y_e0_c2 = 1\",\"y_e0_c1 = 1\",\"x_v0_c0 + x_v1_c0 <= 1\"," +
                "\"x_v0_c1 + x_v1_c1 <= 1\",\"x_v0_c2 + x_v1_c2 <= 1\",\"x_v0_c0 + y_e0_c0 <= 1\"," +
                "\"x_v0_c1 + y_e0_c1 <= 1\",\"x_v0_c2 + y_e0_c2 <= 1\",\"x_v1_c0 + y_e0_c0 <= 1\"," +
                "\"x_v1_c1 + y_e0_c1 <= 1\",\"x_v1_c2 + y_e0_c2 <= 1\",\"x_v0_c0 - z_c0 <= 0\"," +
                "\"x_v0_c1 - z_c1 <= 0\",\"x_v0_c2 - z_c2 <= 0\",\"x_v1_c0 - z_c0 <= 0\",\"x_v1_c1 - z_c1 <= 0\"," +
                "\"x_v1_c2 - z_c2 <= 0\",\"y_e0_c0 - z_c0 <= 0\",\"y_e0_c1 - z_c1 <= 0\",\"y_e0_c2 - z_c2 <= 0\"]," +
                "\"optimizationFunction\":\"z_c0 + z_c1 + z_c2\",\"minimize\":false}";
        assertEquals(expected, jsonString);
    }

    @Test
    void generateLowColorILP(){
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        ILPProblem ilp = ILPGenerator.generateILP(graph, ILPType.WITHLOWCOLORS);
        String jsonString = ilp.getILPAsJsonString();
        String expected = "{\"variables\":[\"x_v0_c0\",\"x_v0_c1\",\"x_v0_c2\",\"x_v1_c0\",\"x_v1_c1\",\"x_v1_c2\"," +
                "\"y_e0_c0\",\"y_e0_c1\",\"y_e0_c2\",\"z_c0\",\"z_c1\",\"z_c2\"],\"constraints\":" +
                "[\"x_v0_c0 + x_v0_c1 + x_v0_c2 = 1\",\"x_v1_c0 + x_v1_c1 + x_v1_c2 = 1\"," +
                "\"y_e0_c0 + y_e0_c1 + y_e0_c2 = 1\",\"x_v0_c0 + x_v1_c0 <= 1\",\"x_v0_c1 + x_v1_c1 <= 1\"," +
                "\"x_v0_c2 + x_v1_c2 <= 1\",\"x_v0_c0 + y_e0_c0 <= 1\",\"x_v0_c1 + y_e0_c1 <= 1\"," +
                "\"x_v0_c2 + y_e0_c2 <= 1\",\"x_v1_c0 + y_e0_c0 <= 1\",\"x_v1_c1 + y_e0_c1 <= 1\"," +
                "\"x_v1_c2 + y_e0_c2 <= 1\",\"x_v0_c0 - z_c0 <= 0\",\"x_v0_c1 - z_c1 <= 0\",\"x_v0_c2 - z_c2 <= 0\"," +
                "\"x_v1_c0 - z_c0 <= 0\",\"x_v1_c1 - z_c1 <= 0\",\"x_v1_c2 - z_c2 <= 0\",\"y_e0_c0 - z_c0 <= 0\"," +
                "\"y_e0_c1 - z_c1 <= 0\",\"y_e0_c2 - z_c2 <= 0\"],\"optimizationFunction\":" +
                "\"4 * z_c0 + 4 * z_c1 + 4 * z_c2 + x_v0_c0 + x_v1_c0 + y_e0_c0\",\"minimize\":false}";
        assertEquals(expected, jsonString);
    }

    @Test
    void generateLowSetColorILP(){
        Graph graph = new Graph();
        graph.addNode();
        graph.addNode();
        graph.addEdge(graph.getNodes().get(0), graph.getNodes().get(1));
        graph.getNodes().get(0).setColor(Color.web("0x00FF00"));
        graph.getNodes().get(1).setColor(Color.web("0xFF0000"));
        ILPType type = ILPType.WITHLOWSETCOLORS;
        type.setMaximizeColor(Color.web("0xFF0000"));
        ILPProblem ilp = ILPGenerator.generateILP(graph, type);
        String jsonString = ilp.getILPAsJsonString();
        String expected = "{\"variables\":[\"x_v0_c0\",\"x_v0_c1\",\"x_v0_c2\",\"x_v1_c0\",\"x_v1_c1\",\"x_v1_c2\"," +
                "\"y_e0_c0\",\"y_e0_c1\",\"y_e0_c2\",\"z_c0\",\"z_c1\",\"z_c2\"],\"constraints\":" +
                "[\"x_v0_c0 + x_v0_c1 + x_v0_c2 = 1\",\"x_v0_c1 = 1\",\"x_v1_c0 + x_v1_c1 + x_v1_c2 = 1\"," +
                "\"x_v1_c0 = 1\",\"y_e0_c0 + y_e0_c1 + y_e0_c2 = 1\",\"x_v0_c0 + x_v1_c0 <= 1\"," +
                "\"x_v0_c1 + x_v1_c1 <= 1\",\"x_v0_c2 + x_v1_c2 <= 1\",\"x_v0_c0 + y_e0_c0 <= 1\"," +
                "\"x_v0_c1 + y_e0_c1 <= 1\",\"x_v0_c2 + y_e0_c2 <= 1\",\"x_v1_c0 + y_e0_c0 <= 1\"," +
                "\"x_v1_c1 + y_e0_c1 <= 1\",\"x_v1_c2 + y_e0_c2 <= 1\",\"x_v0_c0 - z_c0 <= 0\"," +
                "\"x_v0_c1 - z_c1 <= 0\",\"x_v0_c2 - z_c2 <= 0\",\"x_v1_c0 - z_c0 <= 0\",\"x_v1_c1 - z_c1 <= 0\"," +
                "\"x_v1_c2 - z_c2 <= 0\",\"y_e0_c0 - z_c0 <= 0\",\"y_e0_c1 - z_c1 <= 0\",\"y_e0_c2 - z_c2 <= 0\"]," +
                "\"optimizationFunction\":\"4 * z_c0 + 4 * z_c1 + 4 * z_c2 + x_v0_c0 + x_v1_c0 + y_e0_c0\"," +
                "\"minimize\":false}";
        assertEquals(expected, jsonString);
    }
}