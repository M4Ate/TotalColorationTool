package com.todense.viewmodel.random.generators;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Node;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates a graph similar to the current graph in the editor.
 * A similar graph is a graph that results after two edges have been swapped.
 * A swap in that case is the exchange of two end nodes.
 */
public class SimilarGenerator extends RandomEdgeGenerator{

    private final Graph currentGraph;

    /**
     * This is the constructor of the SimilarGenerator.
     * It calls the super constructor, and sets the current graph to the given attribute
     *
     * @param currentGraph the graph on which base the generate method will generate the similar graph
     */
    public SimilarGenerator(Graph currentGraph) {
        super();
        this.currentGraph = currentGraph;
        super.setNodes(currentGraph.getNodes());
    }

    /**
     * This method changes the graph, that ist stored in the currentGraph attribute,
     * to a graph that is similar to the current graph.
     *
     * @throws IllegalStateException if no Edges can be swapped this Exception is thrown
     * to notify the RandomGeneratorViewModel to print an Error and quit the generation
     */
    @Override
    protected void generate() throws IllegalStateException {

        List<Edge> graphEdgesList = new ArrayList<>(currentGraph.getEdges().getEdgeMap().values());

        for (int i = 0; i < graphEdgesList.size(); i++) {
            //get one random nodes
            Edge edgeOne;

            //get one edge random
            int randomEdgeIndex = getRandomEdgeIndex(graphEdgesList.size());
            edgeOne = graphEdgesList.get(randomEdgeIndex);
            graphEdgesList.remove(randomEdgeIndex);

            for (Edge edgeTwo : graphEdgesList) {
                if (edgesAreDifferent(edgeOne, edgeTwo)) {

                    Node[] array = {edgeOne.getN1(), edgeOne.getN2(), edgeTwo.getN1(), edgeTwo.getN2()};

                    if (!currentGraph.getEdges().isEdgeBetween(array[0], array[2])
                            && !currentGraph.getEdges().isEdgeBetween(array[1], array[3])) {

                        currentGraph.removeEdge(edgeOne);
                        currentGraph.removeEdge(edgeTwo);

                        addEdges(array[0], array[2], array[1], array[3]);
                        return;

                    }
                }
            }
        }
        //in case no fitting Edge is found the exception is thrown
        throw new IllegalStateException("no valid similar graph");
    }

    //this method adds two new Edges between the first two Nodes and the second two Nodes
    private void addEdges(Node node, Node node1, Node node2, Node node3) {
        super.addEdge(node.getID(), node1.getID());
        super.addEdge(node2.getID(), node3.getID());

        this.currentGraph.addEdge(node, node1);
        this.currentGraph.addEdge(node2, node3);
    }

    //this method returns if 2 Edges have one or more "end" nodes in common
    private boolean edgesAreDifferent (Edge edgeOne, Edge edgeTwo) {
        return (edgeOne.getN1() != edgeTwo.getN1() && edgeOne.getN2() != edgeTwo.getN2()
                && edgeOne.getN1() != edgeTwo.getN2() && edgeOne.getN2() != edgeTwo.getN1());
    }

    //this method generates a random number between 0 and max.
    // it will be Override in a test case variant which is the reason for it to be protected
    protected int getRandomEdgeIndex(int max) {
        return super.rnd.nextInt(max);
    }
}