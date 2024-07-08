package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.random.RandomEdgeGenerator;

/**
 * Generates a graph similar to the already loaded one.
 * TODO add a definiton what a simla Graph is
 */
public class SimilarGenerator extends RandomEdgeGenerator{

    private final Graph currentGraph;

    public SimilarGenerator(Graph currentGraph){
        super();
        this.currentGraph = currentGraph;
    }

    @Override
    protected void generate() {
        //get a random node
        int randomEdgeId = super.rnd.nextInt(this.currentGraph.getEdges().size());
        Edge edgeOne = currentGraph.getEdges().get(randomEdgeId);

        //get a second random node with different end nodes
        Edge edgeTow;
        do {
            randomEdgeId = super.rnd.nextInt(this.currentGraph.getEdges().size());
            edgeTow = currentGraph.getEdges().get(randomEdgeId);
        } while (edgeOne.getN1() == edgeTow.getN1() && edgeOne.getN2() == edgeTow.getN2());

        //switch the ends of the edges
        
        //validate if a Graph is already loaded
        System.out.println("Current graph size: " + size);
    }

    /*
    TODO delete this after alternative is constructed
    @Override
    protected void generate() {
        for(int i = 0; i < graph.getNodes().size(); i++){
            for(int j = i+1; j < graph.getNodes().size(); j++){
                if(graph.getEdge(i, j) != null){
                    super.addEdge(i, j);
                }
            }
        }
    }

     */
}
