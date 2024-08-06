package com.todense.util;

import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;

import java.util.List;


/**
 * Utility class for copying a graph with more information than the standard copy function of Graph allows
 */
public class GraphCopy {

    /**
     * Copies the graph and returns a new Graph instance with the same nodes, edges, ids, and colors
     *
     * @param inputGraph Graph that needs to be copied
     * @return new instance of a copied Graph
     */
    public static Graph copyGraphWithColorsAndID(Graph inputGraph){
        Graph outputGraph = new Graph(inputGraph.name);
        outputGraph.setIdCounter(inputGraph.getIdCounter());
        List<Node> inputNodes = inputGraph.getNodes();
        for (Node n : inputNodes) {
            Node newNode = outputGraph.addNode(n.getPos(), n.getID());
            newNode.setColor(n.getColor());
            newNode.setLabelText(n.getLabelText());
        }
        EdgeList inputEdges = inputGraph.getEdges();
        for (Edge e : inputEdges) {
            int i = e.getN1().getID();
            int j = e.getN2().getID();
            outputGraph.addEdge(outputGraph.getNodeById(i), outputGraph.getNodeById(j), e.getColor());
        }

        return outputGraph;
    }

}
