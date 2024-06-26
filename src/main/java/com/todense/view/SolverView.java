package com.todense.view;

import com.todense.viewmodel.SolverViewModel;
import de.saxsys.mvvmfx.FxmlView;
import de.saxsys.mvvmfx.InjectViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/*
 * View for the ILP Solver.
 */

public class SolverView implements FxmlView<SolverViewModel> {


    @InjectViewModel
    SolverViewModel viewModel;

    public void initialize(){

    }
}
