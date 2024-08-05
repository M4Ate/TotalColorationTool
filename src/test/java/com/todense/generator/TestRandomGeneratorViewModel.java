package com.todense.generator;

import com.todense.TestUtil.TestNotificationCenterDummy;
import com.todense.TestUtil.TestRandomGeneratorViewModelDummy;
import com.todense.TestUtil.TestSimilarGenerator;
import com.todense.TestUtil.ValidateGraphEquality;
import com.todense.model.graph.Graph;
import com.todense.viewmodel.RandomGeneratorViewModel;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import com.todense.viewmodel.random.generators.SimilarGenerator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestRandomGeneratorViewModel {


    @Test
    public void similarGraphCallTest() {

        Graph currentGraph = ValidateGraphEquality.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_2_N7_D5_E14.ogr");

        TestNotificationCenterDummy notificationCenter = new TestNotificationCenterDummy();

        TestRandomGeneratorViewModelDummy viewModelDummy = new TestRandomGeneratorViewModelDummy();

        TestSimilarGenerator similarGenerator = new TestSimilarGenerator(currentGraph, new int[] {0,10});

        viewModelDummy.callGenerateAndPublishASimilarGraph(notificationCenter, similarGenerator, currentGraph);

        Graph resultGraph = ValidateGraphEquality.loadGraphFile(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_2_result_0_10.ogr");

        assertTrue(ValidateGraphEquality.graphsEqual(resultGraph, notificationCenter.getGraphToPublish()));
        assertEquals(3, notificationCenter.getMessagesToPublish().size());
        assertEquals("NEW_GRAPH_REQUEST", notificationCenter.getMessagesToPublish().get(0));
        assertEquals("TASK_FINISHED", notificationCenter.getMessagesToPublish().get(1));
        assertEquals("Similar graph generated", notificationCenter.getMessagesToPublish().get(2));



    }

    @Test
    void testSimilarGraphGeneratorNoGraphLoaded() {

        Graph currentGraph = ValidateGraphEquality.loadGraphFile("src/test/resources/SimilarGraphGenTesting/Empty_Graph.ogr");

        TestNotificationCenterDummy testNotificationCenterDummy = new TestNotificationCenterDummy();

        TestRandomGeneratorViewModelDummy viewModelDummy = new TestRandomGeneratorViewModelDummy();

        viewModelDummy.callGenerateAndPublishASimilarGraph(testNotificationCenterDummy, new SimilarGenerator(currentGraph), currentGraph);

        assertEquals(testNotificationCenterDummy.getMessagesToPublish().get(1), "A Graph needs to be loaded to perform this action");
    }

    @Test
    void testSimilarGraphGeneratorNoSimilarGraphFound() {
        Graph currentGraph = ValidateGraphEquality.loadGraphFile("src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_3_3N_D2_E3.ogr");

        TestNotificationCenterDummy testNotificationCenterDummy = new TestNotificationCenterDummy();

        TestRandomGeneratorViewModelDummy viewModelDummy = new TestRandomGeneratorViewModelDummy();

        viewModelDummy.callGenerateAndPublishASimilarGraph(testNotificationCenterDummy, new SimilarGenerator(currentGraph), currentGraph);

        assertEquals(testNotificationCenterDummy.getMessagesToPublish().get(1), "Could not find a valid similar graph");

    }

}
