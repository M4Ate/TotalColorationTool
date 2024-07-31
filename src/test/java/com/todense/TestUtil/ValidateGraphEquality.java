package com.todense.TestUtil;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.file.GraphReader;
import com.todense.viewmodel.file.format.ogr.OgrReader;

import java.io.File;

public class ValidateGraphEquality {

    public static boolean graphsEqual(Graph graph1, Graph graph2) {
        for(Node node1 : graph1.getNodes()){
            for(Node node2 : graph2.getNodes()){
                if(!nodesEqual(node1, node2)) return false;
            }
        }

        for(Edge edge1 : graph1.getEdges()){
            for(Edge edge2 : graph2.getEdges()){
                if(edgesEqual(edge1, edge2)) return false;
            }
        }
        return true;
    }

    public static Graph loadGraphFile(String path){
        File file = new File(path);
        GraphReader graphReader = new OgrReader();
        return graphReader.readGraph(file);
    }

    private static boolean nodesEqual(Node node1, Node node2){
        if(node1.getID() != node2.getID()) return false;
        if(node1.getColor() != node2.getColor()) return false;
        return node1.getLabelText().equals(node2.getLabelText());
    }

    private static boolean edgesEqual(Edge edge1, Edge edge2){
        if(edge1.getId().equals(edge2.getId())) return false;
        if(edge1.getColor() != edge2.getColor()) return false;
        if(edge1.getN1().getID() != edge2.getN1().getID()) return false;
        return edge1.getN2().getID() == edge2.getN2().getID();
    }
}
