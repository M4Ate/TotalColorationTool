package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import com.todense.viewmodel.scope.GraphScope;
import de.saxsys.mvvmfx.InjectScope;


/**
 * Generates a graph similar to the already loaded one.
 */
public class SimilarGenerator extends RandomEdgeGenerator{

    @InjectScope
    GraphScope graphScope;

    private final Graph graph;


    public SimilarGenerator(){
        super();
        graph = graphScope.getGraphManager().getGraph();
    }

    @Override
    protected void generate() {
        for(int i = 0; i < graph.getNodes().size(); i++){
            for(int j = i+1; j < graph.getNodes().size(); j++){
                if(graph.getEdge(i, j) != null){
                    super.addEdge(i, j);
                }
            }
        }
    }
}
