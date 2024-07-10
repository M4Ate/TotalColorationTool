package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import javafx.scene.paint.Color;

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
        Edge edgeTwo;
        do {
            randomEdgeId = super.rnd.nextInt(this.currentGraph.getEdges().size());
            edgeTwo = currentGraph.getEdges().get(randomEdgeId);
        } while (edgeOne.getN1().getIndex() == edgeTwo.getN2().getIndex()
                | edgeTwo.getN1().getIndex() == edgeOne.getN2().getIndex());

        Color greyColor = Color.rgb(105, 105, 105);

        currentGraph.removeEdge(edgeOne);
        currentGraph.removeEdge(edgeTwo);

        System.out.println("add edge from " + edgeOne.getN1() + " to " + edgeTwo.getN2());
        currentGraph.addEdge(edgeOne.getN1(), edgeTwo.getN2(), greyColor);

        System.out.println("add edge from " + edgeTwo.getN1() + " to " + edgeOne.getN2());
        currentGraph.addEdge(edgeTwo.getN1(), edgeOne.getN2(), greyColor);
        //validate if a Graph is already loaded
        System.out.println("generate called");
    }

    private boolean nodesDifferent (Edge edgeOne, Edge edgeTwo){
        if(edgeOne.getN1().getIndex() != edgeTwo.getN1().getIndex()
                && edgeOne.getN2().getIndex() != edgeTwo.getN2().getIndex()){}
        return true;
    }

}


