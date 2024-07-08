package com.todense.viewmodel.comparison;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompareLogicTest {

    Color testColor = Color.YELLOW;

    @Test
    void testNodeUncoloring() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode = currentGraph.addNode();
        Node compareNode = compareGraph.addNode();
        currentNode.setColor(testColor);
        compareNode.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentNode.getColor(), CompareLogic.GREY_COLOR);
    }

    @Test
    void testEdgeUncoloring() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode1 = currentGraph.addNode();
        Node currentNode2 = currentGraph.addNode();
        Node compareNode1 = compareGraph.addNode();
        Node compareNode2 = compareGraph.addNode();
        Edge currentEdge = currentGraph.addEdge(currentNode1, currentNode2);
        Edge compareEdge = compareGraph.addEdge(compareNode1, compareNode2);
        currentEdge.setColor(testColor);
        compareEdge.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentEdge.getColor(), CompareLogic.GREY_COLOR);
    }

    @Test
    void testAddNode() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode = currentGraph.addNode();
        Node compareNode = compareGraph.addNode();
        Node compareNode2 = compareGraph.addNode();
        compareNode2.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentGraph.getNodes().size(), 2);
        assertEquals(currentGraph.getNodeById(1).getColor(), testColor);
    }

    @Test
    void testAddEdge() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode = currentGraph.addNode();
        Node currentNode2 = currentGraph.addNode();
        Node compareNode = compareGraph.addNode();
        Node compareNode2 = compareGraph.addNode();
        Edge compareEdge = compareGraph.addEdge(compareNode, compareNode2);
        compareEdge.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertTrue(currentGraph.getEdges().isEdgeBetween(currentNode, currentNode2));
        assertEquals(currentGraph.getEdge(0, 1).getColor(), testColor);
    }

    @Test
    void testAddNodeAndEdge() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode = currentGraph.addNode();
        Node compareNode = compareGraph.addNode();
        Node compareNode2 = compareGraph.addNode();
        Edge compareEdge = compareGraph.addEdge(compareNode, compareNode2);
        compareEdge.setColor(testColor);
        compareNode2.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentGraph.getEdges().size(), 1);
        assertEquals(currentGraph.getNodes().size(), 2);
        assertEquals(currentGraph.getEdge(0, 1).getColor(), testColor);
        assertEquals(currentGraph.getNodeById(1).getColor(), testColor);
    }

    @Test
    void testDifferentColorNode() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode = currentGraph.addNode();
        Node compareNode = compareGraph.addNode();
        currentNode.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentNode.getColor(), testColor);
    }

    @Test
    void testDifferentColorEdge() {
        Graph currentGraph = new Graph();
        Graph compareGraph = new Graph();
        Node currentNode1 = currentGraph.addNode();
        Node currentNode2 = currentGraph.addNode();
        Node compareNode1 = compareGraph.addNode();
        Node compareNode2 = compareGraph.addNode();
        Edge currentEdge = currentGraph.addEdge(currentNode1, currentNode2);
        Edge compareEdge = compareGraph.addEdge(compareNode1, compareNode2);
        currentEdge.setColor(testColor);

        CompareLogic.compareAndUncolor(currentGraph, compareGraph);

        assertEquals(currentEdge.getColor(), testColor);

    }

}
