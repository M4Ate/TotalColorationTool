package com.todense.viewmodel.solver.graphColoring;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.todense.model.EdgeList;
import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.comparison.CompareLogic;
import com.todense.viewmodel.solver.ilpGeneration.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * GraphColorer class can be used to color a Graph with the result of an ILP-Problem
 */
public class GraphColorer {

    /**
     * Colors the graph with the jsonResponse
     *
     * @param graph graph that will be colored
     * @param ilp the corresponding ILPProblem
     * @param jsonResponse the String of the json Response
     * @return the colored Graph or null if the jsonResponse has an error
     */
    public static Graph getColoredGraph(Graph graph, ILPProblem ilp, String jsonResponse) {

        // unpack jsonResponse
        JsonObject responseObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
        boolean error = responseObject.get("error").getAsBoolean();
        if (error) {
            return null;
        }
        JsonArray resultArray = responseObject.getAsJsonArray("result");
        HashMap<String, Integer> resultMap = new HashMap<>();
        for (JsonElement e : resultArray) {
            JsonObject varObject = e.getAsJsonObject();
            String varName = varObject.get("variable").getAsString();
            int value = varObject.get("value").getAsInt();
            resultMap.put(varName, value);
        }
        // extract VertexColorVars and EdgeColorVars
        List<Variable> varList = ilp.getVarList();
        ArrayList<VertexColorVar> vertexVars = new ArrayList<>();
        ArrayList<EdgeColorVar> edgeVars = new ArrayList<>();
        for (Variable var : varList) {
            if (var instanceof VertexColorVar) {
                vertexVars.add((VertexColorVar)var);
            } else if (var instanceof  EdgeColorVar) {
                edgeVars.add((EdgeColorVar)var);
            }
        }
        //color Graph
        HashMap<Integer, Color> colorMap;
        colorMap = ilp.getReverseColorMapping();
        return minColorsColorer(graph, vertexVars, edgeVars, resultMap, colorMap);
    }

    private static Graph minColorsColorer(Graph graph, ArrayList<VertexColorVar> vertexVars,
                                          ArrayList<EdgeColorVar> edgeVars, HashMap<String, Integer> resultMap,
                                          HashMap<Integer, Color> colorMap) {
        //configure ColorGenerator
        ColorGenerator cg = new ColorGenerator();
        //exclude some already used colors
        cg.addUsedColor(Node.DEFAULT_COLOR);
        cg.addUsedColor(Edge.DEFAULT_COLOR);
        cg.addUsedColor(CompareLogic.GREY_COLOR);
        //exclude background color
        cg.addUsedColor(Color.hsb(.0, .0, .13));
        for (Color c : colorMap.values()) {
            cg.addUsedColor(c);
        }
        //color Nodes
        for (VertexColorVar vertexVar : vertexVars) {
            int value = resultMap.get(vertexVar.getAsString());
            if (value == 1) {
                int colorNumber = getColorNumber(vertexVar.getAsString());
                if (!colorMap.containsKey(colorNumber)) {
                    colorMap.put(colorNumber, cg.getUniqueColor());
                }
                Color color = colorMap.get(colorNumber);
                graph.getNodeById(vertexVar.getVertexId()).setColor(color);
            }
        }
        //color Edges
        EdgeList edges = graph.getEdges();
        for (EdgeColorVar edgeVar: edgeVars) {
            int value = resultMap.get(edgeVar.getAsString());
            if (value == 1) {
                int colorNumber = getColorNumber(edgeVar.getAsString());
                if (!colorMap.containsKey(colorNumber)) {
                    colorMap.put(colorNumber, cg.getUniqueColor());
                }
                Color color = colorMap.get(colorNumber);
                String edgeId = edgeVar.getEdgeId();
                for (Edge edge : edges) {
                    if (edge.getId().equals(edgeId)) {
                        edge.setColor(color);
                    }
                }
            }
        }
        return graph;
    }

    private static int getColorNumber(String varName) {
        //pattern that matches the last integer of the String, which is the Color number
        Pattern pattern = Pattern.compile("(\\d+)$");
        Matcher matcher = pattern.matcher(varName);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return -1;
        }
    }


}
