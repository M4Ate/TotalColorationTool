package com.todense.viewmodel.comparison;

import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the logic to compare two graphs and uncolor all nodes and edges that are the same in both graphs
 */
public class CompareLogic {

    public static Color GREY_COLOR = Color.rgb(105, 105, 105);

    /**
     * Compares two graphs and uncolors all nodes and edges that are the same in both graphs
     *
     * @param currentGraph the current graph
     * @param compareGraph the graph to compare with
     */
    public static void compareAndUncolor(Graph currentGraph, Graph compareGraph) {
        List<Node> currentNodes = currentGraph.getNodes();
        List<Node> compareNodes = compareGraph.getNodes();
        EdgeList compareEdges = compareGraph.getEdges();

        compareAndUncolorNodes(currentNodes, new ArrayList<Node>(compareNodes));
        compareAndUncolorEdges(currentGraph, compareEdges);
    }

    /**
     * Nodes need to be done at this point. Such that new Nodes from the compare Graph are already added to the currentNodes.
     *
     * @param currentGraph the current graph
     * @param compareEdges list of edges of the graph to compare with
     */
    private static void compareAndUncolorEdges(Graph currentGraph, EdgeList compareEdges) {
        List<Node> currentNodes = currentGraph.getNodes();
        EdgeList currentEdges = currentGraph.getEdges();

        for (Node node: currentNodes) {
            for (Node neighbor: node.getNeighbours()) {
                if (node.getID() > neighbor.getID()) continue;
                Edge currentEdge = currentEdges.getEdge(node, neighbor);
                Edge compareEdge = compareEdges.getEdge(node, neighbor);

                // Case: Edge doesn't exist in either Graphs -> leave it as is
                if (currentEdge == null && compareEdge == null) continue;

                // Case: Edge exists in both Graphs -> compare color
                if (currentEdge != null && compareEdge != null) {
                    if (currentEdge.getColor().equals(compareEdge.getColor())){
                        currentEdge.setColor(GREY_COLOR);
                    }
                    continue;
                }

                // Case: Edge only exists in current Graph -> leave it as is
                if (currentEdge != null) continue;

                // Case: Edge only exists in compare Graph -> Add Edge to currentEdges
                if (compareEdge != null) {
                    Edge newEdge = currentGraph.addEdge(node, neighbor);
                    newEdge.setColor(compareEdge.getColor());
                    newEdge.setWeight(compareEdge.getWeight());
                    newEdge.setStatus(compareEdge.getStatus());
                }
            }
        }
    }

    private static void compareAndUncolorNodes(List<Node> currentNodes, List<Node> compareNodes) {
        for (Node node: currentNodes) {
            Node compareNode = getNodeWithID(node.getID(), compareNodes);

            if (compareNode != null && node.getColor().equals(compareNode.getColor())) {
                node.setColor(GREY_COLOR);
            }
        }

        // Add all remaining nodes from compareNodes to currentNodes and add them as neighbors
        for (Node node: compareNodes) {
            currentNodes.add(node);
            for (Node neighbor: node.getNeighbours()) {
                neighbor.getNeighbours().add(node);
            }
        }
    }

    private static Node getNodeWithID(int id, List<Node> nodes) {
        for (Node node: nodes) {
            if (node.getID() == id) {
                nodes.remove(node);
                return node;
            }
        }
        return null;
    }
}
