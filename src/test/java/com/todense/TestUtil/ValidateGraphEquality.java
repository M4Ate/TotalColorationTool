package com.todense.TestUtil;

import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import java.util.ArrayList;

public class ValidateGraphEquality {

    public static boolean graphsEqual(Graph graph1, Graph graph2) {

        if(graph1.getNodes().size() != graph2.getNodes().size()) return false;

        ArrayList<Node> nodesOfGraph1 = new ArrayList<>(graph1.getNodes());
        ArrayList<Node> nodesOfGraph2 = new ArrayList<>(graph2.getNodes());

        for (int i = 0; i < nodesOfGraph1.size(); i++) {
            if(!nodesEqual(nodesOfGraph1.get(i), nodesOfGraph2.get(i))) return false;
        }

        EdgeList edgesOfGraph1 = graph1.getEdges();
        EdgeList edgesOfGraph2 = graph2.getEdges();

        for (int i = 0; i < graph1.getSize(); i++) {
            if(!edgesEqual(edgesOfGraph1.get(i), edgesOfGraph2.get(i))) return false;
        }

        return true;
    }

    private static boolean nodesEqual(Node node1, Node node2){
        if(node1.getID() != node2.getID()) return false;
        if(!node1.getColor().equals(node2.getColor())) return false;
        return node1.getLabelText().equals(node2.getLabelText());
    }

    private static boolean edgesEqual(Edge edge1, Edge edge2){
        if(!edge1.getId().equals(edge2.getId())) return false;
        if(!edge1.getColor().equals(edge2.getColor())) return false;
        if(edge1.getN1().getID() != edge2.getN1().getID()) return false;
        return edge1.getN2().getID() == edge2.getN2().getID();
    }
}
