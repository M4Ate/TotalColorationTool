package com.todense.view;

import com.todense.viewmodel.PropertiesViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PropertiesView implements FxmlView<PropertiesViewModel> {

    @FXML private TextField nodeCountTextField;
    @FXML private TextField edgeCountTextField;
    @FXML private TextField ccTextField;
    @FXML private TextField maxDegreeTextField;
    @FXML private TextField minDegreeTextField;
    @FXML private TextField avgDegreeTextField;
    @FXML private TextField diameterTextField;
    @FXML private TextField radiusTextField;
    @FXML private TextField clusterCoeffTextField;
    @FXML private TextField densityTextField;
    @FXML private TextField colorCountTextField;

    @InjectViewModel
    PropertiesViewModel viewModel;

    public void initialize(){
        nodeCountTextField.textProperty().bindBidirectional(viewModel.sizeProperty());
        edgeCountTextField.textProperty().bindBidirectional(viewModel.orderProperty());
        ccTextField.textProperty().bindBidirectional(viewModel.componentsProperty());
        maxDegreeTextField.textProperty().bindBidirectional(viewModel.maxDegreeProperty());
        minDegreeTextField.textProperty().bindBidirectional(viewModel.minDegreeProperty());
        avgDegreeTextField.textProperty().bindBidirectional(viewModel.avgDegreeProperty());
        diameterTextField.textProperty().bindBidirectional(viewModel.diameterProperty());
        radiusTextField.textProperty().bindBidirectional(viewModel.radiusProperty());
        clusterCoeffTextField.textProperty().bindBidirectional(viewModel.clusterCoeffProperty());
        densityTextField.textProperty().bindBidirectional(viewModel.densityProperty());
        colorCountTextField.textProperty().bindBidirectional(viewModel.colorCountProperty());
    }

    @FXML
    private void propertiesAction() {
        viewModel.start();
    }

    @FXML
    private void stopAction(){
        viewModel.stop();
    }

}
