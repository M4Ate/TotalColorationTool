package com.todense.viewmodel;

import com.todense.model.graph.Edge;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.canvas.DisplayMode;
import com.todense.viewmodel.canvas.drawlayer.layers.GraphDrawLayer;
import com.todense.viewmodel.graph.GraphAnalyzer;
import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.*;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import org.apache.commons.math3.util.Precision;

import javax.inject.Inject;

/*
 * ViewModel for the ILP Solver.
 */
public class SolverViewModel implements ViewModel {

    @InjectScope
    GraphScope graphScope;

    @InjectScope
    CanvasScope canvasScope;

    @InjectScope
    BackgroundScope backgroundScope;

    @InjectScope
    AntsScope antsScope;

    @InjectScope
    InputScope inputScope;

    @Inject
    NotificationCenter notificationCenter;

    private GraphManager GM;

    public void initialize(){
        GM = graphScope.getGraphManager();

        Platform.runLater(() -> {
            GraphDrawLayer graphDrawLayer = new GraphDrawLayer(
                    graphScope,
                    backgroundScope,
                    antsScope,
                    inputScope
            );
            canvasScope.getPainter().addDrawLayer(graphDrawLayer);
        });

        notificationCenter.subscribe(GraphViewModel.NEW_GRAPH_REQUEST, (key, payload) -> {
            Graph newGraph = (Graph) payload[0];
            GM.setGraph(newGraph);

            //auto node size
            if(graphScope.isNodeAutoSize()) {
                int sampleSize = Math.min(newGraph.getOrder() - 1, 100);
                Platform.runLater(() -> {
                    if (sampleSize > 0) {
                        double treeLength = GraphAnalyzer.getNearestNeighbourSpanningTreeLength(newGraph, sampleSize);
                        if (treeLength > 0) {
                            double newSize = 0.3 * treeLength / sampleSize;
                            int scale = newSize > 5 ? 1 : 3;
                            newSize = Precision.round(newSize, scale);
                            graphScope.nodeSizeProperty().set(newSize);
                        }
                    }
                });
            }
            notificationCenter.publish(GraphViewModel.NEW_GRAPH_SET);

        });

        notificationCenter.subscribe(MainViewModel.RESET, (key, payload) ->
                graphScope.displayModeProperty().set(DisplayMode.DEFAULT));

        ChangeListener<Object> listener = (obs, oldVal, newVal) -> canvasScope.getPainter().repaint();

    }

}
