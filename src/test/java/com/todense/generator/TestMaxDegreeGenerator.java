package com.todense.generator;

import com.todense.model.graph.Node;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import com.todense.viewmodel.random.generators.MaxDegGenerator;
import javafx.geometry.Point2D;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import com.todense.TestUtil.TestNode;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class TestMaxDegreeGenerator {

    @Test
    void testGenerateMaxDegGenerator() {
        int maxDeg = 1;

        RandomEdgeGenerator generator = new MaxDegGenerator(maxDeg);

        List<Node> nodes = new LinkedList<>();
        nodes.add(new TestNode(new Point2D(1, 2) ,1, 1));
        nodes.add(new TestNode(new Point2D(1, 3) ,2, 2));

        generator.setNodes(nodes);
        Pair<Stack<Integer>, Stack<Integer>> connections = generator.generateConnections();

        Stack<Integer> keys = connections.getKey();
        Stack<Integer> values = connections.getValue();

        assertFalse(connections.getKey().isEmpty());
        assertNotEquals(keys.get(0), values.get(0));
        assertEquals(1, keys.size());
        assertEquals(1, values.size());
    }

    /*
    private List<Node> getTestNodes(){
        List<Node> nodes = new ArrayList<>();
        nodes.add(new TestNodes(new Point2D(1, 2) ,1, 1));
        nodes.add(new TestNodes(new Point2D(1, 3) ,2, 2));
        return nodes;
    }
    */

}

