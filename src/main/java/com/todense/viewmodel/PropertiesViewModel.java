package com.todense.viewmodel;

import com.todense.model.graph.Graph;
import com.todense.viewmodel.graph.GraphAnalyzer;
import com.todense.viewmodel.scope.GraphScope;
import com.todense.viewmodel.scope.AlgorithmTaskScope;
import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.inject.Inject;

public class PropertiesViewModel implements ViewModel {

    private final StringProperty orderProperty = new SimpleStringProperty();
    private final StringProperty sizeProperty = new SimpleStringProperty();
    private final StringProperty componentsProperty = new SimpleStringProperty();
    private final StringProperty maxDegreeProperty = new SimpleStringProperty();
    private final StringProperty minDegreeProperty = new SimpleStringProperty();
    private final StringProperty avgDegreeProperty = new SimpleStringProperty();
    private final StringProperty diameterProperty = new SimpleStringProperty();
    private final StringProperty radiusProperty = new SimpleStringProperty();
    private final StringProperty clusterCoeffProperty = new SimpleStringProperty();
    private final StringProperty densityProperty = new SimpleStringProperty();
    private final StringProperty colorCountProperty = new SimpleStringProperty();

    @InjectScope
    GraphScope graphScope;

    @InjectScope
    AlgorithmTaskScope algorithmTaskScope;

    @Inject
    NotificationCenter notificationCenter;

    private Thread propertiesThread = new Thread();

    private void calculate() {
        Graph graph = graphScope.getGraphManager().getGraph();
        int size = graph.getNodes().size();
        int order = graph.getEdges().size();
        int minDegree = GraphAnalyzer.calculateMinDegree(graph);
        int maxDegree = GraphAnalyzer.calculateMaxDegree(graph);
        double avgDegree = GraphAnalyzer.calculateAvgDegree(graph);
        int componentCount = GraphAnalyzer.getComponentCount(graph);
        double clusterCoeff = GraphAnalyzer.calculateClusteringCoefficient(graph);
        double density = GraphAnalyzer.getDensity(graph);
        int[] eccentricityBounds = GraphAnalyzer.calculateEccentricities(graph);
        int colorCount = GraphAnalyzer.getColorCount(graph);

        Platform.runLater(() -> {
            sizeProperty.setValue(validText(size));
            orderProperty.setValue(validText(order));
            componentsProperty.setValue(validText(componentCount));
            densityProperty.setValue(validText(density));
            clusterCoeffProperty.setValue(validText(clusterCoeff));
            minDegreeProperty.setValue(validText(minDegree));
            maxDegreeProperty.setValue(validText(maxDegree));
            avgDegreeProperty.setValue(validText(avgDegree));
            radiusProperty.setValue(validText(eccentricityBounds[0]));
            diameterProperty.setValue(validText(eccentricityBounds[1]));
            colorCountProperty.setValue(validText(colorCount));
        });
    }

    public void start(){
        if(algorithmTaskScope.getAlgorithmTask() != null && algorithmTaskScope.getAlgorithmTask().isRunning())
            return;
        if(!propertiesThread.isAlive()){
            notificationCenter.publish(MainViewModel.TASK_STARTED, "Calculating graph properties...");
            propertiesThread = new Thread(() -> {
                calculate();
                notificationCenter.publish(MainViewModel.TASK_FINISHED, "Calculated Properties");
            });
            //taskScope.setThread(propertiesThread);
            propertiesThread.start();
        }
    }

    public void stop(){
        if(propertiesThread.isAlive()){
            propertiesThread.interrupt();
        }
    }

    private String validText(int i){
        if(i == -1) return "inf";
        else if (i == -2) return "";
        else return String.valueOf(i);
    }

    private String validText(double d){
        if(d < 0) return "";
        else return String.format("%.3f", d);
    }

    public StringProperty orderProperty() {
        return orderProperty;
    }

    public StringProperty sizeProperty() {
        return sizeProperty;
    }

    public StringProperty componentsProperty() {
        return componentsProperty;
    }

    public StringProperty maxDegreeProperty() {
        return maxDegreeProperty;
    }

    public StringProperty minDegreeProperty() {
        return minDegreeProperty;
    }

    public StringProperty avgDegreeProperty() {
        return avgDegreeProperty;
    }

    public StringProperty diameterProperty() {
        return diameterProperty;
    }

    public StringProperty radiusProperty() {
        return radiusProperty;
    }

    public StringProperty clusterCoeffProperty() {
        return clusterCoeffProperty;
    }

    public StringProperty densityProperty() {
        return densityProperty;
    }

    public StringProperty colorCountProperty() {
        return colorCountProperty;
    }
}
