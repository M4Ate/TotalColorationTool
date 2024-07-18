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
        List<Node> inputNodes = inputGraph.getNodes();
        for (Node n : inputNodes) {
            Node newNode = outputGraph.addNode(n.getPos(), n.getID());
            newNode.setColor(n.getColor());
        }
        EdgeList inputEdges = inputGraph.getEdges();
        for (Edge e : inputEdges) {
            int i = e.getN1().getIndex();
            int j = e.getN2().getIndex();
            outputGraph.addEdge(outputGraph.getNodes().get(i), outputGraph.getNodes().get(j), e.getColor());
        }

        return outputGraph;
    }

}