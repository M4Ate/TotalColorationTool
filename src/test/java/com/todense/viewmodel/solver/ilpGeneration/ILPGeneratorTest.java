package com.todense.viewmodel.solver.ilpGeneration;

import com.todense.model.graph.Graph;
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
}