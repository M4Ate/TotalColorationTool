package com.todense.viewmodel;

import com.todense.model.EdgeList;
import com.todense.model.graph.Graph;
import com.todense.model.graph.Node;
import com.todense.viewmodel.comparison.CompareLogic;
import com.todense.viewmodel.file.format.ogr.OgrReader;
import com.todense.viewmodel.graph.GraphManager;
import com.todense.viewmodel.scope.GraphScope;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;

import javax.inject.Inject;
import java.io.File;
import java.util.List;

/*
 * ViewModel for the Comparison.
 */

public class ComparisonViewModel implements ViewModel {
    @InjectScope
    GraphScope graphScope;
    @Inject
    static NotificationCenter notificationCenter;

    private OgrReader ogrReader;
    private GraphManager graphManager;


    public void initialize() {
        ogrReader = new OgrReader();
        graphManager = graphScope.getGraphManager();
    }

    public void doComparison(File file) {
        Graph graph = ogrReader.readGraph(file);
        List<Node> currentNodes = graphManager.getGraph().getNodes();
        List<Node> compareNodes = graph.getNodes();
        EdgeList currentEdges = graphManager.getGraph().getEdges();
        EdgeList compareEdges = graph.getEdges();

        CompareLogic.compareAndUncolor(currentNodes, currentEdges, compareNodes, compareEdges);

        notificationCenter.publish(CanvasViewModel.REPAINT_REQUEST);
    }
}
