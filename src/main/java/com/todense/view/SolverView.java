package com.todense.view;

import com.todense.viewmodel.SolverViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import de.saxsys.mvvmfx.utils.notifications.NotificationCenter;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.controlsfx.control.ToggleSwitch;
import javafx.scene.paint.Color;

import javax.inject.Inject;

/**
 * View for the ILP Solver.
 */
public class SolverView implements FxmlView<SolverViewModel> {

    @FXML private ToggleSwitch preferColorToggleSwitch, similarColoringToggleSwitch, currentColorsToggleSwitch, useServerToggleSwitch;
    @FXML private ColorPicker preferColorPicker;
    @FXML private TextField serverIP, serverPort;
    @FXML private Label serverIPLabel, serverPortLabel, preferredColorLabel;

    @InjectViewModel
    SolverViewModel viewModel;

    @Inject
    NotificationCenter notificationCenter;

    /**
     * This method is used to initialize the ILP-Solver View
     * It provides functionalities for selecting ILP-Constraints as well, as
     * providing functionality in order to use and save an external server.
     */
    public void initialize(){

        preferColorPicker.valueProperty().set(Color.rgb(230,150,80));

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

        String[] savedValues = viewModel.loadSavedServerValues();

        serverIP.setText(savedValues[0]);
        serverPort.setText(savedValues[1]);

        preferColorToggleSwitch.disableProperty().bind(similarColoringToggleSwitch.selectedProperty());
        preferColorPicker.disableProperty().bind(similarColoringToggleSwitch.selectedProperty());
        preferredColorLabel.disableProperty().bind(similarColoringToggleSwitch.selectedProperty());
        currentColorsToggleSwitch.disableProperty().bind(similarColoringToggleSwitch.selectedProperty());

    }

    /**
     * This method is used when the user wants a colored version with or without the constraints.
     * It retrieves the parameters and then calls the SolverViewModel.
     */
    @FXML
    private void ilpAction() {

        boolean preferColor = preferColorToggleSwitch.isSelected();
        Color preferredColor = preferColorPicker.getValue();
        boolean similarColoring = similarColoringToggleSwitch.isSelected();
        boolean currentColors = currentColorsToggleSwitch.isSelected();

        boolean useServerToggle = useServerToggleSwitch.isSelected();
        String ipInput = serverIP.getText();
        String portInput = serverPort.getText();

        if(similarColoring){
            preferColor = false;
            currentColors = false;
        }

        viewModel.start(preferColor, preferredColor, similarColoring, currentColors, useServerToggle , ipInput, portInput);

    }

    /**
     * This method is used in order to exit the solving method directly.
     */
    @FXML
    private void stopAction(){
        viewModel.stop();
    }

    /**
     * This method is used to save a new value for the server and port.
     */
    @FXML
    private void saveServer(){

        boolean savedServer = viewModel.saveServerConfig(serverIP.getText(), serverPort.getText());

        System.out.println(savedServer);
    }
}

