package com.todense.viewmodel.random.generators;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.random.RandomEdgeGenerator;
import com.todense.viewmodel.scope.GraphScope;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;

import javax.inject.Inject;

/**
 * Generates a graph similar to the already loaded one.
 * TODO add a definiton what a simla Graph is
 */
public class SimilarGenerator extends RandomEdgeGenerator{

    private final Graph currentGraph;

    public SimilarGenerator(Graph currentGraph){
        super();
        this.currentGraph = currentGraph;
    }

    @Override
    protected void generate() {

        //validate if a Graph is already loaded
        System.out.println("SimilarGenerator called");
    }

    /*
    TODO delete this after alternative is constructed
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

     */
}
