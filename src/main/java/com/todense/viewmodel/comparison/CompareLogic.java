package com.todense.viewmodel.comparison;

import com.todense.model.EdgeList;
import com.todense.model.graph.Node;
import com.todense.viewmodel.CanvasViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;


public class CompareLogic {
    @Inject
    static NotificationCenter notificationCenter;

    public static Color GREY_COLOR = Color.rgb(105, 105, 105);

    /**
     * Compares two graphs and uncolors all nodes and edges that are the same in both graphs
     *
     * @param currentNodes list of nodes of the current graph
     * @param currentEdges list of edges of the current graph
     * @param compareNodes list of nodes of the graph to compare with
     * @param compareEdges list of edges of the graph to compare with
     */
    public static void compareAndUncolor(List<Node> currentNodes, EdgeList currentEdges, List<Node> compareNodes, EdgeList compareEdges) {
        compareAndUncolorNodes(currentNodes, new ArrayList<Node>(compareNodes));
        compareAndUncolorEdges(currentEdges, compareEdges);



        notificationCenter.publish(CanvasViewModel.REPAINT_REQUEST);
    }

    private static void compareAndUncolorEdges(EdgeList currentEdges, EdgeList compareEdges) {

    }

    private static void compareAndUncolorNodes(List<Node> currentNodes, List<Node> compareNodes) {
        for (Node node: currentNodes) {
            Node compareNode = getNodeWithID(node.getID(), compareNodes);

            if (compareNode != null && node.getColor().equals(compareNode.getColor())) {
                node.setColor(GREY_COLOR);
            }
        }
        currentNodes.addAll(compareNodes);
    }

    private static Node getNodeWithID(int id, List<Node> nodes) {
        for (Node node: nodes) {
            if (node.getID() == id) {
                nodes.remove(node);
                return node;
            }
        }
        return null;
    }
}
