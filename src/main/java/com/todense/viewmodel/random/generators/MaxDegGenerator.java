package com.todense.viewmodel.random.generators;
import com.todense.viewmodel.random.RandomEdgeGenerator;

/**
 * This class is the MaxDegGenerator. It is a child of the RandomEdgeGenerator Class.
 * Its purpose is to distribute the edges around in a Graph.
 * The target is that every Node has as many adjacent edges as possible or as wished form the user.
 */

public class MaxDegGenerator extends RandomEdgeGenerator {

    private final int maxDegree;

    /**
     * This is the constructor of the maxDegree Class.
     * It stores the give parameter and calls the super constructor.
     *
     * @param maxDegree the target value for the generate method.
     */
    public MaxDegGenerator(int maxDegree){
        super();
        this.maxDegree = maxDegree;
    }

    /**
     * This method generates the edges for the new graph.
     * It uses the attribute set by the constructor.
     */

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
