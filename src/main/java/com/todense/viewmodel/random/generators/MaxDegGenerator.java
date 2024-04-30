package com.todense.viewmodel.random.generators;

import com.todense.viewmodel.random.RandomEdgeGenerator;

public class MaxDegGenerator extends RandomEdgeGenerator {

    private final int maxDegree;

    public MaxDegGenerator(int maxDegree){
        super();
        this.maxDegree = maxDegree;
    }

    @Override
    protected void generate() {

        int degree = maxDegree / 2;

        for (int i = 0; i < nodes.size(); i++) {
            for (int d = 1; d <= degree; d++) {
                super.addEdge(i, (i + d) % nodes.size());
            }
        }

        if (maxDegree % 2 != 0) {       // Handle odd degree numbers by adding an edge on every second node
            for (int i = 0; i < nodes.size()/2; i ++) {
                super.addEdge(i, (i + nodes.size()/2)% nodes.size());
            }
        }
    }
}
