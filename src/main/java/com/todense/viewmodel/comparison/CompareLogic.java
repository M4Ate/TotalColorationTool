package com.todense.viewmodel.comparison;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import javafx.scene.paint.Color;

/**
 * This class contains the logic to compare two graphs and uncolor all nodes and edges that are the same in both graphs
 */
public class CompareLogic {

    public static Color GREY_COLOR = Color.rgb(105, 105, 105);
    public static Color ONLY_IN_ONE_GRAPH_COLOR = Color.rgb(252, 15, 192);

    /**
     * Compares two graphs and uncolors all nodes and edges that are the same in both graphs
     *
     * @param currentGraph the current graph
     * @param compareGraph the graph to compare with
     */
    public static void compareAndUncolor(Graph currentGraph, Graph compareGraph) {
        compareAndUncolorNodes(currentGraph, compareGraph);
        compareAndUncolorEdges(currentGraph, compareGraph);
    }

    /**
     * Nodes need to be done at this point. Such that new Nodes from the compare Graph are already added to the currentNodes.
     *
     * @param currentGraph the current graph
     * @param comparisonGraph the graph to compare with
     */
    private static void compareAndUncolorEdges(Graph currentGraph, Graph comparisonGraph) {
        for (Edge currentEdge : currentGraph.getEdges().getEdgeMap().values()) {
            Node node1Comp = comparisonGraph.getNodeById(currentEdge.getN1().getID());
            Node node2Comp = comparisonGraph.getNodeById(currentEdge.getN2().getID());
            if(node1Comp == null ||
                    node2Comp == null ||
                    comparisonGraph.getEdges().getEdge(node1Comp, node2Comp) == null)
            {
                currentEdge.setColor(ONLY_IN_ONE_GRAPH_COLOR);
            }
        }

        for (Edge compEdge : comparisonGraph.getEdges().getEdgeMap().values()) {
            Edge currentEdge;
            Node node1Current = currentGraph.getNodeById(compEdge.getN1().getID());
            Node node2Current = currentGraph.getNodeById(compEdge.getN2().getID());
            currentEdge = currentGraph.getEdges().getEdge(node1Current, node2Current);

            if (currentEdge == null) {
                currentGraph.addEdge(node1Current, node2Current, ONLY_IN_ONE_GRAPH_COLOR);
                node1Current.getNeighbours().add(node2Current);
                node2Current.getNeighbours().add(node1Current);
            } else if (currentEdge.getColor().equals(compEdge.getColor())) {
                currentEdge.setColor(GREY_COLOR);
            }

        }
    }

    private static void compareAndUncolorNodes(Graph currentGraph, Graph comparisonGraph) {
        for (Node currentNode: currentGraph.getNodes()) {
            if (comparisonGraph.getNodeById(currentNode.getID()) == null) {
                currentNode.setColor(ONLY_IN_ONE_GRAPH_COLOR);
            }
        }

        for (Node compNode : comparisonGraph.getNodes()) {
            Node currentNode = currentGraph.getNodeById(compNode.getID());

            if (currentNode == null) {
                Node node = currentGraph.addNode(compNode.getPos() ,compNode.getID());
                node.setColor(ONLY_IN_ONE_GRAPH_COLOR);
            } else {
                // Set color to gray if colors are the same
                if (currentNode.getColor().equals(compNode.getColor())) {
                    currentNode.setColor(GREY_COLOR);
                }
            }

        }
    }

}
