package com.todense.generator;

import com.todense.TestUtil.TestSimilarGenerator;
import com.todense.TestUtil.ValidateGraphEquality;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSimilarGraphGenerator {

    @Test
    //Attention: This Test ist related to specific files located in the repo
    //If These Files are incorrectly placed or do contain not monitored values the test case will not be meaningful
    void testSimilarGraphGeneratorDefault() {
        Graph currentGraph = ValidateGraphEquality.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_N5_D3_E7.ogr");

        TestSimilarGenerator generator = new com.todense.TestUtil.TestSimilarGenerator(currentGraph, new int[] {4,3});
        generator.testGenerate();

        Graph compareGraphCorrect = ValidateGraphEquality.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_result_4_3.ogr");

        assertTrue(ValidateGraphEquality.graphsEqual(currentGraph, compareGraphCorrect));

        Graph compareGraphFalse = ValidateGraphEquality.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_result_0_5.ogr");

        assertFalse(ValidateGraphEquality.graphsEqual(currentGraph, compareGraphFalse));
    }

    @Test
    void testSimilarGraphGeneratorNoGraphLoaded() {
        OgrReader ogrReader = new OgrReader();
        Graph currentGraph =  ValidateGraphEquality.loadGraphFile("src/test/resources/SimilarGraphGenTesting/Empty_Graph.ogr");

        TestSimilarGenerator testSimilarGraphGenerator = new TestSimilarGenerator(currentGraph);

        Exception e = assertThrows(IllegalStateException.class, testSimilarGraphGenerator::testGenerate);

        assertEquals(e.getMessage(), "no valid similar graph");
    }
}
