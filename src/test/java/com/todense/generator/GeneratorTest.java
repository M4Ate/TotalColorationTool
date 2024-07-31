package com.todense.generator;

import com.todense.TestUtil.TestSimilarGenerator;
import com.todense.TestUtil.ValidateGraphEquality;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.TestUtil.TestNodes;
import com.todense.viewmodel.RandomGeneratorViewModel;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import com.todense.viewmodel.random.generators.MaxDegGenerator;
import com.todense.viewmodel.random.generators.SimilarGenerator;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

public class GeneratorTest {

    @Inject
    NotificationCenter notificationCenter;

    RandomGeneratorViewModel randomGeneratorViewModel = new RandomGeneratorViewModel();

    @Test
    void testGenerateMaxDegGenerator() {
        int maxDeg = 1;

        RandomEdgeGenerator generator = new MaxDegGenerator(maxDeg);

        generator.setNodes(getTestNodes());
        Pair<Stack<Integer>, Stack<Integer>> connections = generator.generateConnections();

        Stack<Integer> keys = connections.getKey();
        Stack<Integer> values = connections.getValue();



        assert !connections.getKey().isEmpty();
        assert !Objects.equals(keys.get(0), values.get(0));
        assert keys.size() == 1;
        assert values.size() == 1;
    }


    private List<Node> getTestNodes(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new TestNodes(new Point2D(1, 2) ,1, 1));
        nodes.add(new TestNodes(new Point2D(1, 3) ,2, 2));
        return nodes;
    }

    @Test
    //Attention: This Test ist related to specific files located in the repo
    //If These Files are incorrectly placed or do contain not monitored values the test case will not be meaningful
    void testSimilarGraphGenerator() {
        OgrReader ogrReader = new OgrReader();
        Graph currentGraph = ogrReader.readGraph(new File(
                "src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_N5_D3_E7.ogr"));

        TestSimilarGenerator generator = new TestSimilarGenerator(currentGraph, new int[] {1, 2, 3, 4});
        generator.testGenerate();

        Graph compareGraph = ogrReader.readGraph(new File("src/test/resources/SimilarGraphGenTesting/Similar_Graph_Gen_Test_Graph_1_result_4_3.ogr"));

        assert !ValidateGraphEquality.graphsEqual(compareGraph, currentGraph);
    }

}