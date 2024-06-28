package com.todense.view;

import com.todense.viewmodel.SolverViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import org.controlsfx.control.ToggleSwitch;
import javafx.scene.paint.Color;
/*
 * View for the ILP Solver.
 */

public class SolverView implements FxmlView<SolverViewModel> {

    @FXML private ToggleSwitch preferColorToggleSwitch, brightColoringToggleSwitch, currentColorsToggleSwitch;

    @FXML private ColorPicker preferColorPicker;

    @InjectViewModel
    SolverViewModel viewModel;

    public void initialize(){}

    @FXML
    private void ilpAction() {

        boolean preferColor = preferColorToggleSwitch.isSelected();
        Color preferredColor = preferColorPicker.getValue();
        boolean brightColoring = brightColoringToggleSwitch.isSelected();
        boolean currentColorsToggle = currentColorsToggleSwitch.isSelected();

        viewModel.start(preferColor, preferredColor, brightColoring, currentColorsToggle);

    }

    @FXML
    private void stopAction(){
    }
}
