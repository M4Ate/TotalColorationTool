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

        Edge[] edges = getRandomEdges();
        Edge edgeOne = edges[0];
        Edge edgeTwo = edges[1];


        
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

    private Edge[] getRandomEdges() {

        Edge[] edges = new Edge[2];
        
        int randomEdgeId = super.rnd.nextInt(this.currentGraph.getEdges().size());
        edges[0] = currentGraph.getEdges().get(randomEdgeId);

        System.out.println("selected " + edges[0].getN1() + " and " + edges[0].getN2());


        do {
            randomEdgeId = super.rnd.nextInt(this.currentGraph.getEdges().size());
            edges[1] = currentGraph.getEdges().get(randomEdgeId);
        } while (edges[0].getN1().getIndex() == edges[1].getN2().getIndex()
                | edges[1].getN1().getIndex() == edges[0].getN2().getIndex()
                | edges[0].getId().equals(edges[1].getId()));

        System.out.println("selected " + edges[1].getN1() + " and " + edges[1].getN2());
        return edges;
    }
}


