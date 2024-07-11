package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Node;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        //get aone random nodes

        Edge edgeOne;

        List<Edge> graphEdgesList = new ArrayList<>(currentGraph.copy().getEdges().getEdgeMap().values());

        //get one edge random
        int randomEdgeId = super.rnd.nextInt(graphEdgesList.size());
        edgeOne = graphEdgesList.get(randomEdgeId);
        graphEdgesList.remove(randomEdgeId);

        //Shuffle the List of Edges
        Collections.shuffle(graphEdgesList);

        for(Edge edgeTwo : graphEdgesList){
            if(edgesAreDifferent(edgeOne, edgeTwo)) {

                Node[] array = {edgeOne.getN1(), edgeOne.getN2(), edgeTwo.getN1(), edgeTwo.getN2()};

                if(!currentGraph.getEdges().isEdgeBetween(array[0], array[2])
                        && !currentGraph.getEdges().isEdgeBetween(array[1], array[3])) {

                    currentGraph.removeEdge(edgeOne);
                    currentGraph.removeEdge(edgeTwo);

                    addEdges(array[0], array[2], array[1], array[3]);
                    return;

                } else if(!currentGraph.getEdges().isEdgeBetween(array[0], array[3])
                    && !currentGraph.getEdges().isEdgeBetween(array[1], array[2])) {

                    currentGraph.removeEdge(edgeOne);
                    currentGraph.removeEdge(edgeTwo);

                    addEdges(array[0], array[3], array[1], array[2]);
                    return;
                }
            }
        }
    }

    private void addEdges(Node node, Node node1, Node node2, Node node3) {

        Color greyColor = Color.rgb(105, 105, 105);

        this.currentGraph.addEdge(node, node1, greyColor);
        this.currentGraph.addEdge(node1, node3, greyColor);
    }

    private boolean edgesAreDifferent (Edge edgeOne, Edge edgeTwo) {
        return (edgeOne.getN1() != edgeTwo.getN1() && edgeOne.getN2() != edgeTwo.getN2()
                && edgeOne.getN1() != edgeTwo.getN2() && edgeOne.getN2() != edgeTwo.getN1());
    }
}