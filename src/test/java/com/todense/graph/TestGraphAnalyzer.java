package com.todense.graph;

import com.todense.TestUtil.LoadUtil;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphAnalyzer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TestGraphAnalyzer {

    @Test
    public void testGraphAnalyzerColorCountZero() {
        Graph graph = LoadUtil.loadGraphFile("src/test/resources/SimilarGraphGenTesting/Empty_Graph.ogr");

        assertEquals(0, GraphAnalyzer.getColorCount(graph));
    }

    @Test
    public void testGraphAnalyzerColorCount() {
        Graph graph = LoadUtil.loadGraphFile("src/test/resources/ColoredGraphs/7Colors.ogr");

        assertEquals(7, GraphAnalyzer.getColorCount(graph));

        graph = LoadUtil.loadGraphFile("src/test/resources/ColoredGraphs/8Colors.ogr");

        assertEquals(8, GraphAnalyzer.getColorCount(graph));
    }

    @Test
    public void ignoreDefaultColor() {
        Graph graph = LoadUtil.loadGraphFile("src/test/resources/ColoredGraphs/Uncolored and not Empty.ogr");

        assertEquals(0, GraphAnalyzer.getColorCount(graph));

        graph = LoadUtil.loadGraphFile("src/test/resources/ColoredGraphs/Uncolored and no Edges.ogr");

        assertEquals(0, GraphAnalyzer.getColorCount(graph));

        graph = LoadUtil.loadGraphFile("src/test/resources/ColoredGraphs/Partly colored and no Edges.ogr");

        assertEquals(2, GraphAnalyzer.getColorCount(graph));

    }
}
