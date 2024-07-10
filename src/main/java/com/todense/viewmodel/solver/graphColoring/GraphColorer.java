package com.todense.viewmodel.solver.graphColoring;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.solver.ilpGeneration.EdgeColorVar;
import com.todense.viewmodel.solver.ilpGeneration.ILPType;
import com.todense.viewmodel.solver.ilpGeneration.Variable;
import com.todense.viewmodel.solver.ilpGeneration.VertexColorVar;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GraphColorer {

    /**
     * Colors the graph with the jsonResponse
     *
     * @param graph graph that will be colored
     * @param varList list of the Variables so json Variables can be matched with the corresponding edges and nodes
     * @param jsonResponse the String of the json Response
     * @param type the type of Problem
     * @return the colored Graph or null if the jsonResponse has an error
     */
    public static Graph getColoredGraph(Graph graph, List<Variable> varList, String jsonResponse, ILPType type) {
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
        if (type == ILPType.MINCOLORS || type == ILPType.WITHSETCOLORS) {
            return standartColor(graph, vertexVars, edgeVars, resultMap);
        } else if (type == ILPType.WITHLOWCOLORS || type == ILPType.WITHLOWSETCOLORS) {
            return colorWithPreferredColor(graph, vertexVars, edgeVars, type.getMaximizeColor(), resultMap);
        }

        return null;
    }

    private static Graph standartColor(Graph graph, ArrayList<VertexColorVar> vertexVars,
                                       ArrayList<EdgeColorVar> edgeVars, HashMap<String, Integer> resultMap) {

        return null;
    }
    private static Graph colorWithPreferredColor(Graph graph, ArrayList<VertexColorVar> vertexVars,
                                                 ArrayList<EdgeColorVar> edgeVars, Color c,
                                                 HashMap<String, Integer> resultMap) {
        return null;
    }


}
