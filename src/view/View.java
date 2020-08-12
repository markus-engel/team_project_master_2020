package view;

import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

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
    private ChoiceBox<?> coloringTaxonomyChoiceBox;

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
    private MenuItem openFileMenuItem;

    @FXML
    private Menu openRecentFileMenu;

    @FXML
    private Menu importMenu;

    @FXML
    private MenuItem ImportTaxonomyMenuItem;

    @FXML
    private MenuItem ImportCoverageMenuItem;

    @FXML
    private MenuItem SaveMenuItem;

    @FXML
    private MenuItem SaveAsPNGMenuItem;

    @FXML
    private MenuItem CloseMenuItem;

    @FXML
    private Menu editMenu;

    @FXML
    private MenuItem SelectionMenuItem;

    @FXML
    private MenuItem layoutSettingsMenuItem;

    @FXML
    private Menu viewMenu;

    @FXML
    private MenuItem showTaxLegend;

    @FXML
    private Menu PlotMenu;

    @FXML
    private MenuItem CoverageGCMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem AboutMenuItem;

    // getter and setter Methods. More have to be implemented if needed
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
        return ImportTaxonomyMenuItem;
    }

    public MenuItem getImportCoverageMenuItem() {
        return ImportCoverageMenuItem;
    }

    public MenuItem getCoverageGCMenu() {
        return CoverageGCMenu;
    }

    public MenuItem getSelectionMenuItem() {
        return SelectionMenuItem;
    }

    public MenuItem getSaveAsPNGMenuItem() {return SaveAsPNGMenuItem;}

    public MenuItem getCloseMenuItem() {
        return CloseMenuItem;
    }

    public RadioButton getColoringTaxonomyRadioButton() {
        return coloringTaxonomyRadioButton;
    }

    public RadioButton getColoringDefaultRadioButton() {
        return coloringDefaultRadioButton;
    }

    public RadioButton getColoringRankRadioButton () { return coloringRankRadioButton;}

    public ChoiceBox<?> getColoringTaxonomyChoiceBox() {
        return coloringTaxonomyChoiceBox;
    }

    public void setColoringTaxonomyChoiceBox(ChoiceBox<?> coloringTaxonomyChoiceBox) {
        this.coloringTaxonomyChoiceBox = coloringTaxonomyChoiceBox;
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

    public RadioButton getNodeSizeContigLengthRadioButton() {
        return nodeSizeContigLengthRadioButton;
    }

    public RadioButton getNodeSizeCoverageRadioButton() {
        return nodeSizeCoverageRadioButton;
    }

    public RadioButton getNodeSizeDefaultRadioButton() {
        return nodeSizeDefaultRadioButton;
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

    public Group getViewObjects(){ return viewObjects;}

    public Group getViewObjectsSele(){ return viewObjectsSele;}

    public Group getInnerViewObjects() { return innerViewObjects;}

    public Group getInnerViewObjectsSele() { return innerViewObjectsSele;}

    public ProgressIndicator getProgressIndicator(){ return progressIndicator;}

    public void setInnerViewObjects(Group innerViewObjects) { this.innerViewObjects = viewObjects;}

    public void setInnerViewObjectsSele(Group innerViewObjectsSele) { this.innerViewObjectsSele = viewObjectsSele;}

    public MenuItem getLayoutSettingsMenuItem(){ return layoutSettingsMenuItem;}

    public double getLayoutRepulsionMultiplierSpinner(){ return Double.parseDouble(layoutRepulsionMultiplierSpinner.getValueFactory().getValue().toString());}

    public double getLayoutAttractionMultiplierSpinner() {return Double.parseDouble(layoutAttractionMultiplierSpinner.getValueFactory().getValue().toString());}

    public Button getLayoutApplyButton() { return layoutApplyButton;}

    public void setScaleProperty(double scale){ innerViewObjects.setScaleX(scale); }

    public double getScaleProperty(){ return innerViewObjects.getScaleX();}

    /*
    public void addVertex(ViewVertex vv) {
         innerViewObjects.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        innerViewObjects.getChildren().add(viewEdge);
    } */

    public void addVertex(ViewVertex vv, ObservableList observableList) {
         observableList.add(vv);
    }

    public void addEdge(ViewEdge viewEdge, ObservableList observableList) {
        observableList.add(viewEdge);
    }

    public ScrollPane getScrollPane(){ return scrollPane;}

    public ScrollPane getScrollPaneSele(){ return scrollPaneSele;}

    public void makeScrollPaneZoomable(ScrollPane sp) {
        sp.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.isControlDown()){ // wenn scrollen disabled werden soll, dann hier !scrollevent.isConsumed()
                    double scale = calculateScaleForZooming(scrollEvent);
                    innerViewObjects.setScaleX(scale);
                    innerViewObjects.setScaleY(scale);
                    scrollEvent.consume();
                }
            }
        });
    }

    private double calculateScaleForZooming(ScrollEvent scrollEvent) {
        double scale = innerViewObjects.getScaleX() + scrollEvent.getDeltaY()/100;
        if (scale <= MIN_ZOOM_SCALE) {
            scale = MIN_ZOOM_SCALE;
        } else if (scale >= MAX_ZOOM_SCALE) {
            scale = MAX_ZOOM_SCALE;
        }
        return scale;
    }
}
