package com.todense.generator;

import com.todense.TestUtil.LoadUtil;
import com.todense.TestUtil.TestSimilarGenerator;
import com.todense.TestUtil.ValidateGraphEquality;
import com.todense.model.graph.Graph;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimilarGraphGenerator {

    @Test
    void testSimilarGraphGeneratorDefault() {
        Graph currentGraph = LoadUtil.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_N5_D3_E7.ogr");

        TestSimilarGenerator generator = new com.todense.TestUtil.TestSimilarGenerator(currentGraph, new int[] {4,3});
        generator.testGenerate();

        Graph compareGraphCorrect = LoadUtil.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_result_4_3.ogr");

        assertTrue(ValidateGraphEquality.graphsEqual(currentGraph, compareGraphCorrect));

        Graph compareGraphFalse = LoadUtil.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_result_0_5.ogr");

        assertFalse(ValidateGraphEquality.graphsEqual(currentGraph, compareGraphFalse));
    }

    @Test
    void testSimilarGraphGeneratorNoGraphLoaded() {

        Graph currentGraph =  LoadUtil.loadGraphFile("src/test/resources/SimilarGraphGenTesting/Empty_Graph.ogr");

        TestSimilarGenerator testSimilarGraphGenerator = new TestSimilarGenerator(currentGraph);

        Exception e = assertThrows(IllegalStateException.class, testSimilarGraphGenerator::testGenerate);

        assertEquals(e.getMessage(), "no valid similar graph");
    }
}
