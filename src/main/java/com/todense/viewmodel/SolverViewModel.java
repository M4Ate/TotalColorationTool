package com.todense.viewmodel;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.GraphScope;
import com.todense.viewmodel.solver.JsonBuilder;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import javafx.scene.paint.Color;

/*
 * ViewModel for the ILP Solver.
 */
public class SolverViewModel implements ViewModel {

    private GraphManager graphManager;

    @InjectScope
    GraphScope graphScope;

    public void initialize() {
        graphManager = graphScope.getGraphManager();
    }


    public void start(Boolean preferColor, Color preferredColor, Boolean brightColoring, Boolean currentColorsToggle){
        System.out.println(preferColor);
        System.out.println(preferredColor);
        System.out.println(brightColoring);
        System.out.println(currentColorsToggle);
    }

    /**
     *  uses an ILP solver to generate a minimal total coloring of the currently displayed graph
     */
    public void colorWithILPSolver(){
        Graph graph = graphManager.getGraph();
        String jsonRequest = JsonBuilder.generateConstraintsJson(graph);

        //request to server

        //color graph according to ilp solution

    }

}
