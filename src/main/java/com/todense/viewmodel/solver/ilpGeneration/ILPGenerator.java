package com.todense.viewmodel.solver.ilpGeneration;

import com.todense.model.graph.Graph;

/**
 * ILPGenetrator offers a static function that creates an ILP-problem for a total coloring problem
 */
public class ILPGenerator {

    /**
     * creates an ILP-Problem of the specified ILPType on the basis of a given Graph
     *
     * @param graph graph to convert into ILP-Problem
     * @param type type of ILP-Problem
     * @return complete ILPProblem class
     */
    public static ILPProblem generateILP(Graph graph, ILPType type) {
        ILPProblem result = null;
        switch (type) {
            case MINCOLORS:
                result = minColors(graph);
                break;
            case SIMILARCOLORS:
                result = similarColors(graph);
                break;
            case WITHSETCOLORS:
                result = withSetColors(graph);
                break;
            case WITHLOWCOLORS:
                result = withLowColors(graph);
                break;
        }
        return result;
    }

    private static ILPProblem minColors(Graph graph) {
        //To be Implemented
        return null;
    }

    private static ILPProblem similarColors(Graph graph) {
        //To be Implemented
        return null;
    }

    private static ILPProblem withSetColors(Graph graph) {
        //To be Implemented
        return null;
    }

    private static ILPProblem withLowColors(Graph graph) {
        //To be Implemented
        return null;
    }
}
