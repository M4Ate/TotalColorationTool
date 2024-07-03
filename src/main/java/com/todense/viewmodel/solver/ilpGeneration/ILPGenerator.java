package com.todense.viewmodel.solver.ilpGeneration;

import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        int maxColorNumber = getMaxDegree(graph) + 2; // resulting from total coloring conjecture assumed to be true
        int maxNodeNumber = graph.getOrder();
        int maxEdgeNumber = graph.getSize();
        ILPProblem ilp = new ILPProblem();

        //create Variables;
        VertexColorVar[][] vertexColors = new VertexColorVar[maxNodeNumber][maxColorNumber];
        EdgeColorVar[][] edgeColors = new EdgeColorVar[maxEdgeNumber][maxColorNumber];
        ColorVar[] colors = new ColorVar[maxColorNumber];
        List<Node> nodes = graph.getNodes();
        EdgeList edges = graph.getEdges();
        for (int vNum = 0; vNum < maxNodeNumber; vNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                vertexColors[vNum][cNum] = new VertexColorVar("x_v" + vNum + "_c" +cNum, nodes.get(vNum).getID());
                ilp.addVariable(vertexColors[vNum][cNum]);
            }
        }
        for (int eNum = 0; eNum < maxEdgeNumber; eNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                edgeColors[eNum][cNum] = new EdgeColorVar("y_e" + eNum + "_c" +cNum, edges.get(eNum).getId());
                ilp.addVariable(edgeColors[eNum][cNum]);
            }
        }
        for (int cNum = 0; cNum < maxColorNumber; cNum++) {
            colors[cNum] = new ColorVar("z_c" + cNum);
            ilp.addVariable(colors[cNum]);
        }
        //create Constraints
        NodeColorConstraint[] ncc = new NodeColorConstraint[maxNodeNumber];
        for (int vNum = 0; vNum < maxNodeNumber; vNum++) {
            String constraint = vertexColors[vNum][0].getAsString(); //this entry exists, because maxColorNumber >= 2
            for (int cNum = 1; cNum < maxColorNumber; cNum++) {
                constraint += " + " + vertexColors[vNum][cNum].getAsString();
            }
            constraint += " = 1";
            ncc[vNum] = new NodeColorConstraint(constraint);
            ilp.addConstraint(ncc[vNum]);
        }
        EdgeColorConstraint[] ecc = new EdgeColorConstraint[maxEdgeNumber];
        for (int eNum = 0; eNum < maxEdgeNumber; eNum++) {
            String constraint = edgeColors[eNum][0].getAsString(); //this entry exists, because maxColorNumber >= 2
            for (int cNum = 1; cNum < maxColorNumber; cNum++) {
                constraint += " + " + edgeColors[eNum][cNum].getAsString();
            }
            constraint += " = 1";
            ecc[eNum] = new EdgeColorConstraint(constraint);
            ilp.addConstraint(ecc[eNum]);
        }
        ConnectedVertexConstraint[] cvc = new ConnectedVertexConstraint[maxEdgeNumber * maxColorNumber];
        for (int eNum = 0; eNum < maxEdgeNumber; eNum++) {
            Edge e = edges.get(eNum);
            int n1Pos = nodes.indexOf(e.getN1());
            int n2Pos = nodes.indexOf(e.getN2());
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                String constraint = vertexColors[n1Pos][cNum].getAsString()
                        + " + " + vertexColors[n2Pos][cNum].getAsString() + " <= 1";
                cvc[eNum * maxColorNumber + cNum] = new ConnectedVertexConstraint(constraint);
                ilp.addConstraint(cvc[eNum * maxColorNumber + cNum]);
            }
        }
        AdjacentEdgeConstraint[] aec = new AdjacentEdgeConstraint[maxNodeNumber * maxColorNumber];
        for (int vNum = 0; vNum < maxNodeNumber; vNum++) {
            List<Node> neighborNodes = nodes.get(vNum).getNeighbours();
            int[] edgePosition = new int[neighborNodes.size()];
            for (int i = 0; i < edgePosition.length; i++) {
                Edge neighborEdge = edges.getEdge(nodes.get(vNum), neighborNodes.get(i));
                edgePosition[i] = edges.indexOf(neighborEdge);
            }

            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                String constraint = vertexColors[vNum][cNum].getAsString();
                for (int i = 0; i < edgePosition.length; i++) {
                    constraint += " + " + edgeColors[edgePosition[i]][cNum].getAsString();
                }
                constraint += " <= 1";
                aec[vNum * maxColorNumber + cNum] = new AdjacentEdgeConstraint(constraint);
                ilp.addConstraint(aec[vNum * maxColorNumber + cNum]);
            }
        }
        SetColorConstraint[] cc = new SetColorConstraint[(maxEdgeNumber + maxNodeNumber) * maxColorNumber];
        for (int vNum = 0; vNum < maxNodeNumber; vNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                String constraint = vertexColors[vNum][cNum].getAsString() + " - "
                        + colors[cNum].getAsString() + " <= 0";
                cc[vNum * maxColorNumber + cNum] = new SetColorConstraint(constraint);
                ilp.addConstraint(cc[vNum * maxColorNumber + cNum]);
            }
        }
        for (int eNum = 0; eNum < maxEdgeNumber; eNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                String constraint = edgeColors[eNum][cNum].getAsString() + " - "
                        + colors[cNum].getAsString() + " <= 0";
                int index = eNum * maxColorNumber + cNum + (maxNodeNumber * maxColorNumber);
                cc[index]
                        = new SetColorConstraint(constraint);
                ilp.addConstraint(cc[index]);
            }
        }
        //create opt Function
        String optFunc = colors[0].getAsString(); //this entry exists, because maxColorNumber >= 2
        for (int cNum = 1; cNum < maxColorNumber; cNum++) {
            optFunc += " + " + colors[cNum].getAsString();
        }
        Optfunction opt = new Optfunction(Arrays.asList(colors), optFunc, true);
        ilp.setOptfunction(opt);

        return ilp;
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

    private static int getMaxDegree(Graph graph){
        int maxDegree = 0;
        List<Node> nodes = graph.getNodes();
        for (Node n : nodes) {
            if (n.getDegree() > maxDegree) {
                maxDegree = n.getDegree();
            }
        }
        return maxDegree;
    }
    
}
