package com.todense.view;

import com.todense.viewmodel.SolverViewModel;
import com.todense.viewmodel.random.GeneratorModel;
import com.todense.viewmodel.solver.Server;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.controlsfx.control.ToggleSwitch;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
/*
 * View for the ILP Solver.
 */

public class SolverView implements FxmlView<SolverViewModel> {

    @FXML private ToggleSwitch preferColorToggleSwitch, brightColoringToggleSwitch, currentColorsToggleSwitch, useServerToggleSwitch;

    @FXML private ColorPicker preferColorPicker;

    @FXML private TextField serverIP, serverPort;
    @FXML private Label serverIPLabel, serverPortLabel;
    @FXML private ChoiceBox<String> serverSelect;

    @InjectViewModel
    SolverViewModel viewModel;

    public void initialize(){

        preferColorPicker.valueProperty().set(Color.rgb(50,90,170));

        serverIP.setPrefHeight(25);
        serverIP.setPrefWidth(40);
        serverIP.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(serverIP, Priority.ALWAYS);
        serverIP.setAlignment(Pos.CENTER);

        serverIPLabel.setPrefWidth(100);
        HBox.setHgrow(serverIPLabel, Priority.ALWAYS);

        serverIP.disableProperty().bind(useServerToggleSwitch.selectedProperty().not());
        serverIPLabel.disableProperty().bind(useServerToggleSwitch.selectedProperty().not());

        serverPort.setPrefHeight(25);
        serverPort.setPrefWidth(40);
        serverPort.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(serverPort, Priority.ALWAYS);
        serverPort.setAlignment(Pos.CENTER);

        serverPortLabel.setPrefWidth(100);
        HBox.setHgrow(serverPortLabel, Priority.ALWAYS);

        serverPort.disableProperty().bind(useServerToggleSwitch.selectedProperty().not());
        serverPortLabel.disableProperty().bind(useServerToggleSwitch.selectedProperty().not());


        //serverSelect.getItems().addAll(getSavedServers());
        //serverSelect.getItems().add("No Servers");

    }

    @FXML
    private void ilpAction() {

        boolean preferColor = preferColorToggleSwitch.isSelected();
        Color preferredColor = preferColorPicker.getValue();
        boolean brightColoring = brightColoringToggleSwitch.isSelected();
        boolean currentColorsToggle = currentColorsToggleSwitch.isSelected();

        boolean useServerToggle = useServerToggleSwitch.isSelected();
        String ipInput = serverIP.getText();
        String portInput = serverPort.getText();

        System.out.println("Server Toggle: " + useServerToggle);
        System.out.println("IP Input: " + ipInput);
        System.out.println("Port Input: " + portInput);

        //viewModel.start(preferColor, preferredColor, brightColoring, currentColorsToggle);

    }

    private List<Server> getSavedServers(){

        List<Server> servers = new ArrayList<>();

        return servers;

    }

    @FXML
    private void stopAction(){
    }
}
