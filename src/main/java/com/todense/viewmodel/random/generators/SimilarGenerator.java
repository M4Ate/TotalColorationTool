package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Node;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Philipp Berghoff
 * Generates a graph similar to the already loaded one.
 * TODO add a definition what a similar Graph is
 * TODO add a java doc
 */
public class SimilarGenerator extends RandomEdgeGenerator{

    private final Graph currentGraph;

    public SimilarGenerator(Graph currentGraph) {
        super();
        this.currentGraph = currentGraph;
        super.setNodes(currentGraph.getNodes());
    }

    /**
     * @author Philipp Berghoff
     * TODO add java doc
     *
     * @throws IllegalStateException
     */

    @Override
    protected void generate() throws IllegalStateException {
        //get one random nodes

        Edge edgeOne;

        List<Edge> graphEdgesList = new ArrayList<>(currentGraph.getEdges().getEdgeMap().values());

        //get one edge random
        int randomEdgeIndex = super.rnd.nextInt(graphEdgesList.size());
        edgeOne = graphEdgesList.get(randomEdgeIndex);
        graphEdgesList.remove(randomEdgeIndex);

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

        //in case no fitting Edge is found the exception is thrown
        throw new IllegalStateException("none isomorphic similar Graph");
    }

    //this method adds two new Edges between the first two Nodes and the second two Nodes
    private void addEdges(Node node, Node node1, Node node2, Node node3) {
        //TODO delete
        System.out.println("Edge One: " + node.getID() + " -> " + node1.getID()
                + " EdgeTwo: " + node2.getID() + " -> " + node3.getID());

        super.addEdge(node.getID(), node1.getID());
        super.addEdge(node2.getID(), node3.getID());

        this.currentGraph.addEdge(node, node1);
        //TODO delete
        printNeighbors(node);
        printNeighbors(node1);
        this.currentGraph.addEdge(node2, node3);
        //TODO delete
        printNeighbors(node2);
        printNeighbors(node3);
    }

    //this method returns if 2 Edges have one or more "end" nodes in common
    private boolean edgesAreDifferent (Edge edgeOne, Edge edgeTwo) {
        return (edgeOne.getN1() != edgeTwo.getN1() && edgeOne.getN2() != edgeTwo.getN2()
                && edgeOne.getN1() != edgeTwo.getN2() && edgeOne.getN2() != edgeTwo.getN1());
    }

    //TODO delete
    private void printNeighbors(Node node) {
        System.out.print("Neighbours of " + node.getID() + ": ");
        for (Node neighbour : node.getNeighbours()) {
            System.out.print(neighbour.getID() + " ");
        }
    }
}