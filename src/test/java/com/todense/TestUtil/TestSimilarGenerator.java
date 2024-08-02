package com.todense.TestUtil;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.generators.SimilarGenerator;

public class TestSimilarGenerator extends SimilarGenerator {

    int[] pseudoRandomNumbers;

    int callCounter = 0;

    public TestSimilarGenerator(Graph currentgraph, int [] pseudoRandomNumbers) {
        super(currentgraph);
        this.pseudoRandomNumbers = pseudoRandomNumbers;
    }

    public TestSimilarGenerator(Graph currentGraph) {
        super(currentGraph);
        this.pseudoRandomNumbers = null;
    }

    public void testGenerate() {
        super.generate();
    }

    @Override
    protected int getRandomEdgeIndex(int max) {
        if(this.pseudoRandomNumbers == null){
            return super.getRandomEdgeIndex(max);
        } else {
            int returnValue = pseudoRandomNumbers[callCounter];
            callCounter++;
            return returnValue;
        }
    }
}