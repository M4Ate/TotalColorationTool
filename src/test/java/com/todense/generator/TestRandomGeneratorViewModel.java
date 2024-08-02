package com.todense.generator;

import com.todense.TestUtil.TestNotificationCenterDummy;
import com.todense.TestUtil.TestSimilarGenerator;
import com.todense.TestUtil.ValidateGraphEquality;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestRandomGeneratorViewModel {


    @Test
    public void similarGraphCallTest() {
        TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();
        Graph currentGraph = null;

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
