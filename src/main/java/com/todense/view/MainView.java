package com.todense.view;

import com.todense.view.components.ParameterHBox;
import com.todense.viewmodel.AnalysisViewModel;
import com.todense.viewmodel.MainViewModel;
import com.todense.viewmodel.SaveViewModel;
import de.saxsys.mvvmfx.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class MainView implements FxmlView<MainViewModel> {

    @FXML private TextArea eventTextArea;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private FontIcon lockIcon, fullScreenIcon;
    @FXML private ToggleButton lockToggleButton, autoLayoutToggleButton, graphAppearanceMenuButton,
                    propertiesMenuButton, operationsMenuButton, generateGraphMenuButton, solveGraphMenuButton,
                    basicAlgorithmsMenuButton, tspMenuButton, layoutMenuButton, eraseModeToggleButton;
    @FXML private Button stopButton;
    @FXML private VBox leftSideMenuContentBox, rightSideMenuContentBox;
    @FXML private VBox graphAppearanceView, backgroundAppearanceView, propertiesView, operationsView,
            randomGeneratorView, presetGeneratorView, basicAlgorithmsView, tspView, layoutView, solverView;
    @FXML private ScrollPane leftSideMenuContentScrollPane, rightSideMenuContentScrollPane;
    @FXML private AnchorPane mainAnchor;
    @FXML private HBox leftContentHBox, rightContentHBox, layoutMenuHBox;
    @FXML private Pane leftResizeHandle, rightResizeHandle;
    @FXML private Label infoLabel, mousePositionLabel;
    @FXML private ColorPicker appColorPicker;
    @FXML MenuItem fullScreenItem;

    @FXML private HBox animationHBox;
    @FXML private ToggleButton animationToggleButton;
    @FXML private ToggleButton pauseButton;


    @InjectViewModel
    MainViewModel viewModel;

    @InjectContext
    private Context context;

    private Stage mainStage;
    private Stage saveStage;
    private Stage analysisStage;


    public void initialize(){

        HBox.setHgrow(mainAnchor, Priority.ALWAYS);
        VBox.setVgrow(mainAnchor, Priority.ALWAYS);

        eventTextArea.textProperty().bindBidirectional(viewModel.eventTextProperty());
        infoLabel.textProperty().bindBidirectional(viewModel.infoTextProperty());

        viewModel.leftPanelWidthProperty().bind(leftContentHBox.widthProperty());
        viewModel.rightPanelWidthProperty().bind(rightContentHBox.widthProperty());

        //fixes not scrolling down bug
        viewModel.eventTextProperty().addListener((observableValue, s, t1) -> {
            eventTextArea.selectPositionCaret(eventTextArea.getLength());
            eventTextArea.deselect();
        });

        viewModel.workingProperty().addListener((obs, oldVal, newVal) -> Platform.runLater(()->{
            if(newVal) {
                progressIndicator.setProgress(-1);
                progressIndicator.setVisible(true);
            }
            else{
                progressIndicator.setProgress(0);
                progressIndicator.setVisible(false);
            }
        }));

        viewModel.editLockedProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal){
                lockIcon.setIconLiteral("fa-lock");
            }
            else{
                lockIcon.setIconLiteral("fa-unlock");
            }
        });

        var nonLayoutAlgorithmRunning = Bindings.createBooleanBinding(
                () -> viewModel.algorithmRunningProperty().get() && viewModel.layoutRunningProperty().not().get(),
                viewModel.algorithmRunningProperty(),
                viewModel.layoutRunningProperty()
        );

        var nonContinuousLayoutAlgorithmRunning = Bindings.createBooleanBinding(
                () -> viewModel.algorithmRunningProperty().get() && viewModel.continuousLayoutOnProperty().not().get(),
                viewModel.algorithmRunningProperty(),
                viewModel.continuousLayoutOnProperty()
        );

        setScrollSpeed(0.005, leftSideMenuContentScrollPane);
        setScrollSpeed(0.005, rightSideMenuContentScrollPane);

        viewModel.mousePositionProperty().addListener((obs, oldVal, newVal) -> {
            var xString = String.format("%.3f",newVal.getX());
            var yString = String.format("%.3f",newVal.getY());
            mousePositionLabel.setText("X: " + xString + " Y: " + yString);
        });

        setUpSideMenuButton(graphAppearanceMenuButton, "Appearance",
                true, graphAppearanceView, backgroundAppearanceView);
        setUpSideMenuButton(operationsMenuButton, "Graph Operations",
                true, operationsView);
        setUpSideMenuButton(propertiesMenuButton, "Graph Properties",
                true, propertiesView);
        setUpSideMenuButton(solveGraphMenuButton, "Color Graph",
                true, solverView);
        setUpSideMenuButton(generateGraphMenuButton, "Generate Graph",
                true, randomGeneratorView, presetGeneratorView);


        ToggleGroup leftSideMenuButtons = new ToggleGroup();
        leftSideMenuButtons.getToggles().addAll(
                graphAppearanceMenuButton, operationsMenuButton, propertiesMenuButton, generateGraphMenuButton, solveGraphMenuButton
        );

        ToggleGroup rightSideMenuButtons = new ToggleGroup();

        leftSideMenuButtons.selectedToggleProperty().addListener(new ChangeListener<>() {
            double lastWidth = 250;

            @Override
            public void changed(ObservableValue<? extends Toggle> obs, Toggle oldVal, Toggle newVal) {
                if(oldVal == null && newVal != null){
                    leftContentHBox.setVisible(true);
                    leftContentHBox.prefWidthProperty().set(lastWidth);
                }else if(newVal == null){
                    lastWidth = leftContentHBox.getWidth();
                    leftContentHBox.prefWidthProperty().set(0.0);
                    leftContentHBox.setVisible(false);

                }
            }
        });
        leftContentHBox.prefWidthProperty().set(0.0);
        leftContentHBox.setVisible(false);

        rightSideMenuButtons.selectedToggleProperty().addListener(new ChangeListener<>() {
            double lastWidth = 250;

            @Override
            public void changed(ObservableValue<? extends Toggle> obs, Toggle oldVal, Toggle newVal) {
                if (oldVal == null && newVal != null) {
                    rightContentHBox.setVisible(true);
                    rightContentHBox.prefWidthProperty().set(lastWidth);
                } else if (newVal == null) {
                    lastWidth = rightContentHBox.getWidth();
                    rightContentHBox.prefWidthProperty().set(0.0);
                    rightContentHBox.setVisible(false);
                }
            }
        });
        rightContentHBox.prefWidthProperty().set(0.0);
        rightContentHBox.setVisible(false);

        leftResizeHandle.setOnMouseDragged(event -> {
            Node parent = leftContentHBox.getParent();
            Bounds boundsInScene = parent.localToScene(parent.getBoundsInLocal());

            double width = event.getSceneX()-boundsInScene.getMinX();

            leftContentHBox.setPrefWidth(width > 0 ? width : 0);

        });

        rightResizeHandle.setOnMouseDragged(event -> {
            Node parent = rightContentHBox.getParent();
            Bounds boundsInScene = parent.localToScene(parent.getBoundsInLocal());

            double width = boundsInScene.getMaxX()-event.getSceneX();

            rightContentHBox.setPrefWidth(width > 0 ? width : 0);

        });

        rightContentHBox.boundsInParentProperty().addListener((obs, oldVal, newVal) ->
                leftContentHBox.maxWidthProperty().set(newVal.getMinX())
        );

        leftContentHBox.boundsInParentProperty().addListener((obs, oldVal, newVal) -> {
            Region parent = (Region) rightContentHBox.getParent();
            rightContentHBox.maxWidthProperty().set(parent.getWidth() - newVal.getWidth());
        });


        leftSideMenuContentScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.doubleValue() < 15){
                leftSideMenuContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }else{
                leftSideMenuContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }
        });

        rightSideMenuContentScrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
            if(newVal.doubleValue() < 15){
                rightSideMenuContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }else{
                rightSideMenuContentScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            }
        });

        leftResizeHandle.setCursor(Cursor.E_RESIZE);
        rightResizeHandle.setCursor(Cursor.E_RESIZE);

        ParameterHBox layoutStepTimeHBox = new ParameterHBox(
                "Iteration time (ms)",
                viewModel.getLayoutScope().stepTimeProperty(),
                0, 5, 0, Double.POSITIVE_INFINITY
        );
        layoutStepTimeHBox.setHorizontal(true);
        layoutStepTimeHBox.getLabel().alignmentProperty().set(Pos.CENTER_RIGHT);
        layoutStepTimeHBox.setLabelWidth(130);

        var stepTimeHBox = new ParameterHBox("Step time (ms)", viewModel.stepTimeProperty(),
                0, 100, 0 , Double.POSITIVE_INFINITY
        );
        stepTimeHBox.setHorizontal(true);
        stepTimeHBox.getLabel().alignmentProperty().set(Pos.CENTER_RIGHT);
        stepTimeHBox.getTextField().setPrefHeight(22);
        stepTimeHBox.getTextField().setPrefWidth(70);

        initAnalysisStage();

        Platform.runLater(() -> {
            viewModel.setKeyInput(mainStage.getScene());

            appColorPicker.valueProperty().addListener((obs, oldVal, newVal)->{
                String colorText = toRGBCode(newVal);
                mainStage.getScene().getRoot().setStyle("fx-theme: "+colorText+";");
            });
            appColorPicker.valueProperty().bindBidirectional(viewModel.appColorProperty());

            mainStage.fullScreenProperty().addListener((obs, oldVal, newVal) -> {
                if(newVal){
                    fullScreenIcon.setIconLiteral("fas-compress-arrows-alt");
                    fullScreenItem.setText("Exit fullscreen");
                }else{
                    fullScreenIcon.setIconLiteral("fas-expand-arrows-alt");
                    fullScreenItem.setText("Fullscreen");

                }
            });
        });
    }

    private void setUpSideMenuButton(ToggleButton button, String tooltipText, boolean leftSide,Node... menuNodes){
        Tooltip tooltip = new Tooltip(tooltipText);

        VBox contentBox = leftSide? leftSideMenuContentBox: rightSideMenuContentBox;

        button.setOnMouseEntered(mouseEvent -> showSideMenuTooltip(button, tooltip, leftSide));

        button.setOnMouseMoved(mouseEvent -> showSideMenuTooltip(button, tooltip, leftSide));

        button.setOnMouseExited(mouseEvent -> tooltip.hide());

        button.selectedProperty().addListener((obs, oldVal, newVal) -> {
            contentBox.getChildren().forEach((node -> {
                node.setVisible(false);
                node.setManaged(false);
            }));

            Arrays.stream(menuNodes).forEach((node -> {
                node.setVisible(true);
                node.setManaged(true);
            }));
        });
    }

    private void showSideMenuTooltip(ToggleButton button, Tooltip tooltip, boolean leftSide){
        if(tooltip.isShowing()){
            return;
        }
        Point2D p = button.localToScene(0.0, 0.0);

        int xOffset = leftSide? 40: (int) -tooltip.getWidth();
        int yOffset = 5;

        tooltip.show(button,
                xOffset + p.getX() + button.getScene().getX() + button.getScene().getWindow().getX(),
                yOffset + p.getY() + button.getScene().getY() + button.getScene().getWindow().getY());
    }


    //speeds up scrolling (default is too slow)
    private void setScrollSpeed(@SuppressWarnings("SameParameterValue") double speed, ScrollPane scrollPane){
        scrollPane.getContent().setOnScroll(scrollEvent -> {
            double deltaY = scrollEvent.getDeltaY() * speed;
            scrollPane.setVvalue(scrollPane.getVvalue() - deltaY);
        });
    }

    private void initSaveStage(){
        saveStage = new Stage();
        final ViewTuple<SaveView, SaveViewModel> saveViewTuple =
                FluentViewLoader.fxmlView(SaveView.class).context(context).load();
        final Parent root = saveViewTuple.getView();
        SaveView saveView = saveViewTuple.getCodeBehind();

        saveView.setInitialDirectory(viewModel.getFileScope().getInitialDirectory());
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass()
                        .getClassLoader()
                        .getResource(
                                "application.css"))
                        .toExternalForm()
        );
        saveStage.initStyle(StageStyle.UTILITY);
        saveStage.setScene(scene);
        saveStage.initOwner(mainStage);
        saveStage.setTitle("Save Graph");
        saveStage.setIconified(false);
        saveStage.setResizable(false);
        saveStage.initModality(Modality.WINDOW_MODAL);

        Platform.runLater(()->{
            String appThemeColor = toRGBCode(viewModel.getAppColor());
            saveStage.getScene().getRoot().setStyle("fx-theme: "+appThemeColor+";");
        });
    }

    private void initAnalysisStage(){
        analysisStage = new Stage();
        final ViewTuple<AnalysisView, AnalysisViewModel> viewTuple =
                FluentViewLoader.fxmlView(AnalysisView.class).context(context).load();
        final Parent root = viewTuple.getView();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass()
                        .getClassLoader()
                        .getResource("application.css"))
                        .toExternalForm()
        );
        analysisStage.setScene(scene);
        analysisStage.setTitle("Graph Analysis");
    }

    @FXML
    private void saveAction() {
        initSaveStage();
        saveStage.show();
    }

    @FXML
    private void openAction() {
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Graph Files", "*.ogr", "*.mtx", "*.tsp", "*.graphml");
        FileChooser fileChooser = new FileChooser();
        String initialDirectory = viewModel.getFileScope().getInitialDirectory();
        if(!initialDirectory.isEmpty())
            fileChooser.setInitialDirectory(new File(initialDirectory));

        fileChooser.getExtensionFilters().add(fileExtensions);
        File file = fileChooser.showOpenDialog(mainStage);
        if(file != null){
            viewModel.openGraph(file);
            viewModel.getFileScope().setInitialDirectory(file.getParent());
        }
    }

    @FXML
    private void openCompareGraphAction() {
        FileChooser.ExtensionFilter fileExtensions =
                new FileChooser.ExtensionFilter(
                        "Graph Files", "*.ogr");
        FileChooser fileChooser = new FileChooser();
        String initialDirectory = viewModel.getFileScope().getInitialDirectory();
        if(!initialDirectory.isEmpty())
            fileChooser.setInitialDirectory(new File(initialDirectory));

        fileChooser.getExtensionFilters().add(fileExtensions);
        File file = fileChooser.showOpenDialog(mainStage);
        if(file != null){
            viewModel.openCompareGraph(file);
            viewModel.getFileScope().setInitialDirectory(file.getParent());
        }
    }

    @FXML
    private void resetAction() {
        viewModel.resetGraph();
    }

    @FXML
    private void deleteAction() {
        viewModel.deleteGraph();
    }

    @FXML
    private void adjustAction() {
        viewModel.adjustCameraToGraph();
    }

    @FXML
    private void nextStepAction() {
        viewModel.nextStep();
    }

    @FXML
    private void stopAction(){
        viewModel.stopCurrentAlgorithm();
    }

    @FXML
    private void randomGraphAction(){
        viewModel.generateRandomGraph();
    }

    @FXML
    private void presetGraphAction(){
        viewModel.createPresetGraph();
    }

    @FXML
    private void helpAction() {
        Pane pane = new Pane();
        pane.setPrefWidth(700);
        pane.setPrefHeight(500);
        TextArea textArea = new TextArea();
        textArea.prefWidthProperty().bind(pane.widthProperty());
        textArea.prefHeightProperty().bind(pane.heightProperty());
        textArea.setEditable(false);
        textArea.setFont(Font.font ("Verdana", 15));
        textArea.setWrapText(true);
        InputStream is = MainView.class.getResourceAsStream("/manual.txt");
        Scanner s = new Scanner(Objects.requireNonNull(is));
        while(s.hasNextLine()){
            textArea.appendText(s.nextLine()+"\n");
        }
        textArea.setScrollTop(Double.MAX_VALUE);
        pane.getChildren().add(textArea);
        Scene scene = new Scene(pane);
        Stage stage = new Stage();
        stage.setTitle("User Guide");
        stage.initStyle(StageStyle.UTILITY);
        stage.setAlwaysOnTop(true);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void fullScreenAction(){
        mainStage.setFullScreen(!mainStage.isFullScreen());
    }

    private String toRGBCode(Color color) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }
}
