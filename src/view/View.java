package view;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class View {

    public final double MAX_ZOOM_SCALE = 2.d;
    public final double MIN_ZOOM_SCALE = .5d;

    @FXML
    private BorderPane borderPane;

    @FXML
    private StackPane stackPane;

    @FXML
    private StackPane stackPaneSele;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ScrollPane scrollPaneSele;

    @FXML
    private Group viewObjects;

    @FXML
    private Group innerViewObjects;

    @FXML
    private Group viewObjectsSele;

    @FXML
    private Group innerViewObjectsSele;

    @FXML
    private TextField sequenceCountTextField;

    @FXML
    private TextField overlapCountTextField;

    @FXML
    private TextField taxaCountTextfield;

    @FXML
    private Tab tabSelection;

    @FXML
    private Tab tabMain;

    @FXML
    private TextField selectionTextfield;

    @FXML
    private ToggleGroup nodeSizeGroup;

    @FXML
    private ChoiceBox<String> nodeSizeScaleChoiceBox;

    @FXML
    private Button resetSelectionButton;

    @FXML
    private RadioButton nodeSizeCoverageRadioButton;

    @FXML
    private RadioButton nodeSizeContigLengthRadioButton;

    @FXML
    private RadioButton nodeSizeDefaultRadioButton;

    @FXML
    private RadioButton nodeSizeManualRadioButton;

    @FXML
    private Slider nodeSizeManualSlider;

    @FXML
    private Slider coloringTransparencySlider;

    @FXML
    private ChoiceBox<?> coloringRankChoiceBox;

    @FXML
    private RadioButton coloringCoverageRadioButton;

    @FXML
    private RadioButton coloringGCcontentRadioButton;

    @FXML
    private RadioButton coloringDefaultRadioButton;

    @FXML
    private RadioButton coloringTaxonomyRadioButton;

    @FXML
    private RadioButton coloringRankRadioButton;

    @FXML
    private RadioButton coloringTransparencyRadioButton;

    @FXML
    private RadioButton orderByNodeNumbersRadioButton;

    @FXML
    private RadioButton orderByContigLengthRadioButton;

    @FXML
    private Button layoutApplyButton;

    @FXML
    private Spinner<?> layoutRepulsionMultiplierSpinner;

    @FXML
    private Spinner<?> layoutAttractionMultiplierSpinner;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Menu fileMenu;

    @FXML
    private MenuItem newFileMenuItem;

    @FXML
    private MenuItem openFileMenuItem;

    @FXML
    private Menu openRecentFileMenu;

    @FXML
    private Menu importMenu;

    @FXML
    private MenuItem importTaxonomyMenuItem;

    @FXML
    private MenuItem importCoverageMenuItem;

    @FXML
    private MenuItem saveMenuItem;

    @FXML
    private MenuItem saveAsPNGMenuItem;

    @FXML
    private MenuItem closeMenuItem;

    @FXML
    private Menu editMenu;

    @FXML
    private Menu selectionMenu;

    @FXML
    private MenuItem exportSelectionSequencesMenuItem;

    @FXML
    private MenuItem layoutSettingsMenuItem;

    @FXML
    private Menu viewMenu;

    @FXML
    private MenuItem showTaxLegend;

    @FXML
    private Menu plotMenu;

    @FXML
    private MenuItem coverageGCMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    private CheckMenuItem showLegendMenuItem;

    @FXML
    private MenuItem showTaxLegendMenuItem;

    @FXML
    private ScrollPane legendScrollPane;

    @FXML
    private TreeView<String> legendTreeView;

    @FXML
    private TextArea graphInformationTextArea;

    private Rectangle selectionRectangle;

    // getter and setter Methods. More have to be implemented if needed
    public MenuItem getNewFileMenuItem() {
        return newFileMenuItem;
    }

    public MenuItem getOpenFileMenuItem() {
        return openFileMenuItem;
    }

    public Menu getOpenRecentFileMenu() {
        return openRecentFileMenu;
    }

    public Tab getTabSelection() {
        return tabSelection;
    }

    public Tab getTabMain() {
        return tabMain;
    }

    public MenuItem getCustomizeMenuItem() {
        return layoutSettingsMenuItem;
    }

    public MenuItem getImportTaxonomyMenuItem() {
        return importTaxonomyMenuItem;
    }

    public MenuItem getImportCoverageMenuItem() {
        return importCoverageMenuItem;
    }

    public MenuItem getCoverageGCMenu() {
        return coverageGCMenu;
    }

    public Menu getSelectionMenu() {
        return selectionMenu;
    }

    public MenuItem getExportSelectionSequencesMenuItem() {
        return exportSelectionSequencesMenuItem;
    }

    public MenuItem getSaveAsPNGMenuItem() {
        return saveAsPNGMenuItem;
    }

    public MenuItem getCloseMenuItem() {
        return closeMenuItem;
    }

    public RadioButton getColoringTaxonomyRadioButton() {
        return coloringTaxonomyRadioButton;
    }

    public RadioButton getColoringDefaultRadioButton() {
        return coloringDefaultRadioButton;
    }

    public RadioButton getColoringRankRadioButton () { return coloringRankRadioButton;}

    public RadioButton getColoringTransparencyRadioButton () { return  coloringTransparencyRadioButton; }

    public ChoiceBox<?> getColoringRankChoiceBox() {
        return coloringRankChoiceBox;
    }

    public void setColoringRankChoiceBox(ChoiceBox<?> coloringTaxonomyChoiceBox) {
        this.coloringRankChoiceBox = coloringRankChoiceBox;
    }

    public RadioButton getColoringCoverageRadioButton() {
        return coloringCoverageRadioButton;
    }

    public void setColoringCoverageRadioButton(RadioButton coloringCoverageRadioButton) {
        this.coloringCoverageRadioButton = coloringCoverageRadioButton;
    }

    public RadioButton getColoringGCcontentRadioButton() {
        return coloringGCcontentRadioButton;
    }

    public void setColoringGCcontentRadioButton(RadioButton coloringGCcontentRadioButton) {
        this.coloringGCcontentRadioButton = coloringGCcontentRadioButton;
    }

    public TextField getSequenceCountTextField() {
        return sequenceCountTextField;
    }

    public RadioButton getNodeSizeManualRadioButton() {
        return nodeSizeManualRadioButton;
    }

    public Slider getNodeSizeManualSlider() {
        return nodeSizeManualSlider;
    }

    public Slider getColoringTransparencySlider () { return coloringTransparencySlider; }

    public RadioButton getNodeSizeContigLengthRadioButton() {
        return nodeSizeContigLengthRadioButton;
    }

    public RadioButton getNodeSizeCoverageRadioButton() {
        return nodeSizeCoverageRadioButton;
    }

    public RadioButton getNodeSizeDefaultRadioButton() {
        return nodeSizeDefaultRadioButton;
    }

    public ChoiceBox<?> getNodeSizeScaleChoiceBox() {
        return nodeSizeScaleChoiceBox;
    }

    public ToggleGroup getNodeSizeGroup() {
        return nodeSizeGroup;
    }

    public RadioButton getOrderByNodeNumbersRadioButton() {
        return orderByNodeNumbersRadioButton;
    }

    public RadioButton getOrderByContigLengthRadioButton() {
        return orderByContigLengthRadioButton;
    }

    public void setTaxaCountTextField(String size) {
        taxaCountTextfield.setText(String.valueOf(size));
    }

    // Number of Vertices
    public void setSequenceCountTextField(int sequenceCount) {
        sequenceCountTextField.setText("Sequences: " + sequenceCount);
    }

    public TextField getOverlapCountTextField() {
        return overlapCountTextField;
    }

    // Number of Edges
    public void setOverlapCountTextField(int overlapCount) {
        overlapCountTextField.setText("Overlaps: " + overlapCount);
    }

    public Group getViewObjects() {
        return viewObjects;
    }

    public Group getViewObjectsSele() {
        return viewObjectsSele;
    }

    public Group getInnerViewObjects() {
        return innerViewObjects;
    }

    public Group getInnerViewObjectsSele() {
        return innerViewObjectsSele;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public void setInnerViewObjects(Group innerViewObjects) {
        this.innerViewObjects = viewObjects;
    }

    public void setInnerViewObjectsSele(Group innerViewObjectsSele) {
        this.innerViewObjectsSele = viewObjectsSele;
    }

    public MenuItem getLayoutSettingsMenuItem() {
        return layoutSettingsMenuItem;
    }

    public double getLayoutRepulsionMultiplierSpinnerValue() {
        return Double.parseDouble(layoutRepulsionMultiplierSpinner.getValueFactory().getValue().toString());
    }

    public Spinner getLayoutRepulsionMultiplierSpinner() {
        return layoutRepulsionMultiplierSpinner;
    }

    public double getLayoutAttractionMultiplierSpinnerValue() {
        return Double.parseDouble(layoutAttractionMultiplierSpinner.getValueFactory().getValue().toString());
    }

    public Spinner getLayoutAttractionMultiplierSpinner() {
        return layoutAttractionMultiplierSpinner;
    }

    public Button getLayoutApplyButton() {
        return layoutApplyButton;
    }

    public void setScaleProperty(double scale) {
        innerViewObjects.setScaleX(scale);
    }

    public Button getResetSelectionButton() {
        return resetSelectionButton;
    }

    public double getScaleProperty() {
        return innerViewObjects.getScaleX();
    }

    public CheckMenuItem getShowLegendMenuItem() {
        return showLegendMenuItem;
    }

    public ScrollPane getLegendScrollPane() {
        return legendScrollPane;
    }

    public TreeView<String> getLegendTreeView() {
        return legendTreeView;
    }

    public void setLegendItems(String... items) {
        legendTreeView.setRoot(new TreeItem<>("Legend"));
    }

    public void addVertex(ViewVertex vv, ObservableList observableList) {
        observableList.add(vv);
    }

    public void addEdge(ViewEdge viewEdge, ObservableList observableList) {
        observableList.add(viewEdge);
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public ScrollPane getScrollPaneSele() {
        return scrollPaneSele;
    }

    public void makeScrollPaneZoomable(ScrollPane sp) {
        sp.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.isControlDown()) { // wenn scrollen disabled werden soll, dann hier !scrollevent.isConsumed()
                    double scale = calculateScaleForZooming(scrollEvent);
                    innerViewObjects.setScaleX(scale);
                    innerViewObjects.setScaleY(scale);
                    scrollEvent.consume();
                }
            }
        });
    }

    private double calculateScaleForZooming(ScrollEvent scrollEvent) {
        double scale = innerViewObjects.getScaleX() + scrollEvent.getDeltaY() / 100;
        if (scale <= MIN_ZOOM_SCALE) {
            scale = MIN_ZOOM_SCALE;
        } else if (scale >= MAX_ZOOM_SCALE) {
            scale = MAX_ZOOM_SCALE;
        }
        return scale;
    }

    public void applyDragSelectRectangleFunctionality(){
       scrollPane.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                selectionRectangle = new Rectangle(0,0, Color.TRANSPARENT);
                selectionRectangle.setStroke(Color.BLACK);
                selectionRectangle.setTranslateX(event.getX());
                selectionRectangle.setTranslateY(event.getY());
                innerViewObjects.getChildren().add(selectionRectangle);
            }
        });
        scrollPane.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectionRectangle != null){
                    selectionRectangle.widthProperty().set(event.getX() - selectionRectangle.getTranslateX());
                    selectionRectangle.heightProperty().set(event.getY() - selectionRectangle.getTranslateY());
                    System.out.println("Width: "+ event.getX());
                    System.out.println("Height: "+ event.getY());
                }
            }
        });
        scrollPane.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(selectionRectangle != null){
                    selectionRectangle = null;
                    System.out.println("released");
                }
            }
        });
    }
}