package com.todense.util;

import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphCopyTest {

    @Test
    void copyGraphWithColorsAndID() {
        Graph graph = new Graph("testGraph");
        Node n1 = graph.addNode(new Point2D(1.5, 2), 42);
        n1.setColor(Color.web("0x00FF00"));
        Node n2 = graph.addNode(new Point2D(3.5, 2), 57);
        n2.setColor(Color.web("0x123456"));
        graph.addEdge(n1, n2, Color.web("0xFF0000"));

        Graph copyGraph = GraphCopy.copyGraphWithColorsAndID(graph);

        assertEquals(graph.name, copyGraph.name);
        assertEquals(graph.getNodes().get(0).getColor().toString(), copyGraph.getNodes().get(0).getColor().toString());
        assertEquals(graph.getNodes().get(0).getID(), copyGraph.getNodes().get(0).getID());
        assertEquals(graph.getNodes().get(0).getLabelText(), copyGraph.getNodes().get(0).getLabelText());
        assertEquals(graph.getNodes().get(1).getColor().toString(), copyGraph.getNodes().get(1).getColor().toString());
        assertEquals(graph.getNodes().get(1).getID(), copyGraph.getNodes().get(1).getID());
        assertEquals(graph.getNodes().get(1).getLabelText(), copyGraph.getNodes().get(1).getLabelText());
        assertEquals(graph.getEdges().get(0).getId(), copyGraph.getEdges().get(0).getId());
        assertEquals(graph.getEdges().get(0).getColor().toString(), copyGraph.getEdges().get(0).getColor().toString());
        assertEquals(graph.getIdCounter(), copyGraph.getIdCounter());
    }
}