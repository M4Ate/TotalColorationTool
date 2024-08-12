package com.todense.TestUtil;

import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.graph.GraphManager;

public class TestGraphManagerDummy extends GraphManager {

    @Override
    public Graph getGraph() {
        Graph graph = new Graph();
        Node n1 = graph.addNode();
        Node n2 = graph.addNode();
        graph.addEdge(n1, n2);
        return graph;
    }
}
