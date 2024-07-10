package com.todense.viewmodel.solver.ilpGeneration;

import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import javafx.scene.paint.Color;
import org.checkerframework.checker.units.qual.C;

import java.util.*;

/**
 * ILPGenetrator offers a static function that creates an ILP-problem for a total coloring problem
 */
public class ILPGenerator {

    /**
     * creates an ILP-Problem of the specified ILPType on the basis of a given Graph
     *
     * @param graph graph to convert into ILP-Problem
     * @param type type of ILP-Problem
     * @return complete ILPProblem class or null if there are too many Colors used
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
            case WITHLOWSETCOLORS:
                result = withLowSetColors(graph, type.getMaximizeColor());
                break;
        }
        return result;
    }

    private static ILPProblem minColors(Graph graph) {
        int maxColorNumber = getMaxDegree(graph) + 2; // resulting from total coloring conjecture assumed to be true
        ILPProblem ilp = new ILPProblem();

        List<Node> nodes = graph.getNodes();
        EdgeList edges = graph.getEdges();

        //create Variables;
        VertexColorVar[][] vertexColors = getAndSetVertexColorVars(ilp, maxColorNumber, nodes);
        EdgeColorVar[][] edgeColors = getAndSetEdgeColorVars(ilp, maxColorNumber, edges);
        ColorVar[] colors = getAndSetColorVars(ilp, maxColorNumber);

        //create Constraints
        setNodeColorConstraint(ilp, vertexColors, nodes, null);
        setEdgeColorConstraint(ilp, edgeColors, edges, null);
        setConnectedVertexConstraint(ilp, vertexColors, nodes, edges, maxColorNumber);
        setAdjacentEdgeConstraint(ilp, vertexColors, edgeColors, nodes, edges, maxColorNumber);
        setSetColorConstraint(ilp, vertexColors, edgeColors, colors);

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
        // max amount of different colors
        int maxColorNumber = getMaxDegree(graph) + 2; // resulting from total coloring conjecture assumed to be true
        ILPProblem ilp = new ILPProblem();
        HashMap<Color, Integer> colorMapping = new HashMap<Color, Integer>();

        List<Node> nodes = graph.getNodes();
        EdgeList edges = graph.getEdges();

        int colorIndex = 0;
        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).getColor().equals(Node.DEFAULT_COLOR)) {
                if (!colorMapping.containsKey(nodes.get(i).getColor())){
                    colorMapping.put(nodes.get(i).getColor(), colorIndex);
                    colorIndex++;
                }
            }
        }
        for (int i = 0; i < edges.size(); i++) {
            if (!edges.get(i).getColor().equals(Edge.DEFAULT_COLOR)) {
                if (!colorMapping.containsKey(edges.get(i).getColor())){
                    colorMapping.put(edges.get(i).getColor(), colorIndex);
                    colorIndex++;
                }
            }
        }
        if (colorIndex >= maxColorNumber) {
            return null;
        }

        //create Variables;
        VertexColorVar[][] vertexColors = getAndSetVertexColorVars(ilp, maxColorNumber, nodes);
        EdgeColorVar[][] edgeColors = getAndSetEdgeColorVars(ilp, maxColorNumber, edges);
        ColorVar[] colors = getAndSetColorVars(ilp, maxColorNumber);

        //create Constraints
        setNodeColorConstraint(ilp, vertexColors, nodes, colorMapping);
        setEdgeColorConstraint(ilp, edgeColors, edges, colorMapping);
        setConnectedVertexConstraint(ilp, vertexColors, nodes, edges, maxColorNumber);
        setAdjacentEdgeConstraint(ilp, vertexColors, edgeColors, nodes, edges, maxColorNumber);
        setSetColorConstraint(ilp, vertexColors, edgeColors, colors);

        //create opt Function
        String optFunc = colors[0].getAsString(); //this entry exists, because maxColorNumber >= 2
        for (int cNum = 1; cNum < maxColorNumber; cNum++) {
            optFunc += " + " + colors[cNum].getAsString();
        }
        Optfunction opt = new Optfunction(Arrays.asList(colors), optFunc, true);
        ilp.setOptfunction(opt);

        return ilp;
    }

    private static ILPProblem withLowColors(Graph graph) {
        int maxColorNumber = getMaxDegree(graph) + 2; // resulting from total coloring conjecture assumed to be true
        ILPProblem ilp = new ILPProblem();

        List<Node> nodes = graph.getNodes();
        EdgeList edges = graph.getEdges();

        //create Variables;
        VertexColorVar[][] vertexColors = getAndSetVertexColorVars(ilp, maxColorNumber, nodes);
        EdgeColorVar[][] edgeColors = getAndSetEdgeColorVars(ilp, maxColorNumber, edges);
        ColorVar[] colors = getAndSetColorVars(ilp, maxColorNumber);

        //create Constraints
        setNodeColorConstraint(ilp, vertexColors, nodes, null);
        setEdgeColorConstraint(ilp, edgeColors, edges, null);
        setConnectedVertexConstraint(ilp, vertexColors, nodes, edges, maxColorNumber);
        setAdjacentEdgeConstraint(ilp, vertexColors, edgeColors, nodes, edges, maxColorNumber);
        setSetColorConstraint(ilp, vertexColors, edgeColors, colors);

        //create opt Function
        int bigConstant = nodes.size() + edges.size() + 1;
        String optFunc = bigConstant + " * " + colors[0].getAsString(); //this entry exists, because maxColorNumber >= 2
        for (int cNum = 1; cNum < maxColorNumber; cNum++) {
            optFunc += " + " + bigConstant + " * " + colors[cNum].getAsString();
        }
        //maximize color 0;
        for (int vNum = 0; vNum < vertexColors.length; vNum++) {
            optFunc += " + " + vertexColors[vNum][0].getAsString();
        }
        for (int eNum = 0; eNum < edgeColors.length; eNum++) {
            optFunc += " + " + edgeColors[eNum][0].getAsString();
        }
        Optfunction opt = new Optfunction(Arrays.asList(colors), optFunc, true);
        ilp.setOptfunction(opt);

        return ilp;
    }

    private static ILPProblem withLowSetColors(Graph graph, Color maximizeColor) {
        // max amount of different colors
        int maxColorNumber = getMaxDegree(graph) + 2; // resulting from total coloring conjecture assumed to be true
        ILPProblem ilp = new ILPProblem();
        HashMap<Color, Integer> colorMapping = new HashMap<Color, Integer>();

        List<Node> nodes = graph.getNodes();
        EdgeList edges = graph.getEdges();

        colorMapping.put(maximizeColor, 0);
        int colorIndex = 1;
        for (int i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).getColor().equals(Node.DEFAULT_COLOR)) {
                if (!colorMapping.containsKey(nodes.get(i).getColor())){
                    colorMapping.put(nodes.get(i).getColor(), colorIndex);
                    colorIndex++;
                }
            }
        }
        for (int i = 0; i < edges.size(); i++) {
            if (!edges.get(i).getColor().equals(Edge.DEFAULT_COLOR)) {
                if (!colorMapping.containsKey(edges.get(i).getColor())){
                    colorMapping.put(edges.get(i).getColor(), colorIndex);
                    colorIndex++;
                }
            }
        }
        if (colorIndex >= maxColorNumber) {
            return null;
        }

        //create Variables;
        VertexColorVar[][] vertexColors = getAndSetVertexColorVars(ilp, maxColorNumber, nodes);
        EdgeColorVar[][] edgeColors = getAndSetEdgeColorVars(ilp, maxColorNumber, edges);
        ColorVar[] colors = getAndSetColorVars(ilp, maxColorNumber);

        //create Constraints
        setNodeColorConstraint(ilp, vertexColors, nodes, colorMapping);
        setEdgeColorConstraint(ilp, edgeColors, edges, colorMapping);
        setConnectedVertexConstraint(ilp, vertexColors, nodes, edges, maxColorNumber);
        setAdjacentEdgeConstraint(ilp, vertexColors, edgeColors, nodes, edges, maxColorNumber);
        setSetColorConstraint(ilp, vertexColors, edgeColors, colors);

        //create opt Function
        int bigConstant = nodes.size() + edges.size() + 1;
        String optFunc = bigConstant + " * " + colors[0].getAsString(); //this entry exists, because maxColorNumber >= 2
        for (int cNum = 1; cNum < maxColorNumber; cNum++) {
            optFunc += " + " + bigConstant + " * " + colors[cNum].getAsString();
        }
        //maximize color 0;
        for (int vNum = 0; vNum < vertexColors.length; vNum++) {
            optFunc += " + " + vertexColors[vNum][0].getAsString();
        }
        for (int eNum = 0; eNum < edgeColors.length; eNum++) {
            optFunc += " + " + edgeColors[eNum][0].getAsString();
        }
        Optfunction opt = new Optfunction(Arrays.asList(colors), optFunc, true);
        ilp.setOptfunction(opt);

        return ilp;
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

    private static VertexColorVar[][] getAndSetVertexColorVars(ILPProblem ilp, int maxColorNumber, List<Node> nodes) {
        int maxNodeNumber = nodes.size();
        VertexColorVar[][] vertexColors = new VertexColorVar[maxNodeNumber][maxColorNumber];
        for (int vNum = 0; vNum < maxNodeNumber; vNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                vertexColors[vNum][cNum] = new VertexColorVar("x_v" + vNum + "_c" +cNum, nodes.get(vNum).getID());
                ilp.addVariable(vertexColors[vNum][cNum]);
            }
        }
        return vertexColors;
    }

    private static EdgeColorVar[][] getAndSetEdgeColorVars(ILPProblem ilp, int maxColorNumber, EdgeList edges) {
        int maxEdgeNumber = edges.size();
        EdgeColorVar[][] edgeColors = new EdgeColorVar[maxEdgeNumber][maxColorNumber];
        for (int eNum = 0; eNum < maxEdgeNumber; eNum++) {
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                edgeColors[eNum][cNum] = new EdgeColorVar("y_e" + eNum + "_c" +cNum, edges.get(eNum).getId());
                ilp.addVariable(edgeColors[eNum][cNum]);
            }
        }
        return edgeColors;
    }

    private static ColorVar[] getAndSetColorVars(ILPProblem ilp, int maxColorNumber) {
        ColorVar[] colors = new ColorVar[maxColorNumber];
        for (int cNum = 0; cNum < maxColorNumber; cNum++) {
            colors[cNum] = new ColorVar("z_c" + cNum);
            ilp.addVariable(colors[cNum]);
        }
        return colors;
    }

    private static void setNodeColorConstraint(ILPProblem ilp, VertexColorVar[][] vertexColors, List<Node> nodes,
                                               HashMap<Color, Integer> colorMapping) {
        // if colorMapping is null, predefined Colors will be ignored
        for (int vNum = 0; vNum < vertexColors.length; vNum++) {
            String constraint = vertexColors[vNum][0].getAsString();
            for (int cNum = 1; cNum < vertexColors[vNum].length; cNum++) {
                constraint += " + " + vertexColors[vNum][cNum].getAsString();
            }
            constraint += " = 1";
            ilp.addConstraint(new NodeColorConstraint(constraint));
            if (colorMapping != null) {
                Color nodeColor = nodes.get(vNum).getColor();
                if (!nodeColor.equals(Node.DEFAULT_COLOR)) {    //if node was colored
                    String fixColor = vertexColors[vNum][colorMapping.get(nodeColor)].getAsString() + " = 1";
                    ilp.addConstraint(new NodeColorConstraint(fixColor));
                }
            }
        }
    }

    private static void setEdgeColorConstraint(ILPProblem ilp, EdgeColorVar[][] edgeColors, EdgeList edges,
                                               HashMap<Color, Integer> colorMapping) {
        // if colorMapping is null, predefined Colors will be ignored
        for (int eNum = 0; eNum < edgeColors.length; eNum++) {
            String constraint = edgeColors[eNum][0].getAsString();
            for (int cNum = 1; cNum < edgeColors[eNum].length; cNum++) {
                constraint += " + " + edgeColors[eNum][cNum].getAsString();
            }
            constraint += " = 1";
            ilp.addConstraint(new EdgeColorConstraint(constraint));
            if (colorMapping != null) {
                Color edgeColor = edges.get(eNum).getColor();
                if (!edgeColor.equals(Edge.DEFAULT_COLOR)) {    //if node was colored
                    String fixColor = edgeColors[eNum][colorMapping.get(edgeColor)].getAsString() + " = 1";
                    ilp.addConstraint(new EdgeColorConstraint(fixColor));
                }
            }
        }
    }

    private static void setConnectedVertexConstraint(ILPProblem ilp, VertexColorVar[][] vertexColors, List<Node> nodes,
                                                     EdgeList edges, int maxColorNumber) {
        for (int eNum = 0; eNum < edges.size(); eNum++) {
            Edge e = edges.get(eNum);
            int n1Pos = nodes.indexOf(e.getN1());
            int n2Pos = nodes.indexOf(e.getN2());
            for (int cNum = 0; cNum < maxColorNumber; cNum++) {
                String constraint = vertexColors[n1Pos][cNum].getAsString()
                        + " + " + vertexColors[n2Pos][cNum].getAsString() + " <= 1";
                ilp.addConstraint(new ConnectedVertexConstraint(constraint));
            }
        }
    }

    private static void setAdjacentEdgeConstraint(ILPProblem ilp, VertexColorVar[][] vertexColors,
                                                  EdgeColorVar[][] edgeColors, List<Node> nodes,
                                                  EdgeList edges, int maxColorNumber) {
        for (int vNum = 0; vNum < nodes.size(); vNum++) {
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
                ilp.addConstraint(new AdjacentEdgeConstraint(constraint));
            }
        }
    }

    private static void setSetColorConstraint(ILPProblem ilp, VertexColorVar[][] vertexColors,
                                              EdgeColorVar[][] edgeColors, ColorVar[] colors) {
        for (int vNum = 0; vNum < vertexColors.length; vNum++) {
            for (int cNum = 0; cNum < colors.length; cNum++) {
                String constraint = vertexColors[vNum][cNum].getAsString() + " - "
                        + colors[cNum].getAsString() + " <= 0";
                ilp.addConstraint(new SetColorConstraint(constraint));
            }
        }
        for (int eNum = 0; eNum < edgeColors.length; eNum++) {
            for (int cNum = 0; cNum < colors.length; cNum++) {
                String constraint = edgeColors[eNum][cNum].getAsString() + " - "
                        + colors[cNum].getAsString() + " <= 0";
                ilp.addConstraint(new SetColorConstraint(constraint));
            }
        }
    }

}
