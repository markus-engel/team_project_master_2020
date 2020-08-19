package view;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
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
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.Node;

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
    private TextField selectionTextField;

    @FXML
    private ToggleGroup nodeSizeGroup;

    @FXML
    private ToggleGroup coloringGroup;

    @FXML
    private ToggleGroup orderGroup;

    @FXML
    private ChoiceBox<String> nodeSizeScaleChoiceBox;

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
    private ChoiceBox<String> coloringRankChoiceBox;

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
    private TextField searchField;

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
    private MenuItem selectAllMenuItem;

    @FXML
    private MenuItem resetSelectionMenuItem;

    @FXML
    private MenuItem layoutSettingsMenuItem;

    @FXML
    private Menu viewMenu;

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
    private TableColumn<SelectedContig, String> contigIdCol;

    @FXML
    private TableColumn<SelectedContig, String> nameCol;

    @FXML
    private SimpleListProperty<SelectedContig> selectedContigs;;

    @FXML
    private TableView<LegendItem> legendTableView;

    @FXML
    private TableColumn<LegendItem, Circle> colourCol;

    @FXML
    private TableColumn<LegendItem, String> labelCol;

    @FXML
    private ObservableList<LegendItem> legendItems;

    private Rectangle selectionRectangle;

    // getter and setter Methods. More have to be implemented if needed
    public MenuItem getNewFileMenuItem() {
        return newFileMenuItem;
    }

    public MenuItem getOpenFileMenuItem() {
        return openFileMenuItem;
    }

    public MenuItem getHelpMenu(){
        return helpMenu;
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

    public MenuItem getSelectAllMenuItem() {
        return selectAllMenuItem;
    }

    public MenuItem getResetSelectionMenuItem() {
        return resetSelectionMenuItem;
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

    public ChoiceBox<String> getColoringRankChoiceBox() {
        return coloringRankChoiceBox;
    }

    public RadioButton getColoringCoverageRadioButton() {
        return coloringCoverageRadioButton;
    }

    public RadioButton getColoringGCcontentRadioButton() {
        return coloringGCcontentRadioButton;
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

    public ChoiceBox<String> getNodeSizeScaleChoiceBox() {
        return nodeSizeScaleChoiceBox;
    }

    public ToggleGroup getNodeSizeGroup() {
        return nodeSizeGroup;
    }

    public ToggleGroup getColoringGroup() {
        return coloringGroup;
    }

    public ToggleGroup getOrderGroup(){
        return orderGroup;
    }

    public RadioButton getOrderByNodeNumbersRadioButton() {
        return orderByNodeNumbersRadioButton;
    }

    public RadioButton getOrderByContigLengthRadioButton() {
        return orderByContigLengthRadioButton;
    }

    public void setTaxaCountTextField(int taxaCount) {
        taxaCountTextfield.setText("Taxa: " + taxaCount);
    }

    // Number of Vertices
    public void setSequenceCountTextField(int sequenceCount) {
        sequenceCountTextField.setText("Sequences: " + sequenceCount);
    }

    // Number of Edges
    public void setOverlapCountTextField(int overlapCount) {
        overlapCountTextField.setText("Overlaps: " + overlapCount);
    }

    public void setSelectionTextField(int selectedSequenceCount) {
        selectionTextField.setText("Selected: " + selectedSequenceCount);
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

    public double getScaleProperty() {
        return innerViewObjects.getScaleX();
    }

    public Rectangle getSelectionRectangle() {
        return selectionRectangle;
    }

    public void initSelectionRectangle(double width, double height){
        selectionRectangle = new Rectangle(width,height,Color.TRANSPARENT);
    }

    public void removeSelectionRectangle() {
        selectionRectangle = null;
    }

    public CheckMenuItem getShowLegendMenuItem() {
        return showLegendMenuItem;
    }

    public TableView<LegendItem> getLegendTableView() {
        return legendTableView;
    }

    public TableColumn<LegendItem,String> getLabelCol() { return labelCol; }

    public ObservableList<LegendItem> getLegendItems(){
        if (legendItems==null){
            legendItems = FXCollections.observableArrayList();
        }
        return legendItems;
    }

    public void setLegendItems(ObservableList<LegendItem> legendItems){
        this.legendItems = legendItems;
    }

    public SimpleListProperty<SelectedContig> getSelectedContigs() {
        if (selectedContigs == null) selectedContigs = new SimpleListProperty<>(FXCollections.observableArrayList());
        return selectedContigs;
    }

    public void addToInfoTable(MyVertex vertex) {
        if (vertex.getProperty(ContigProperty.TAXONOMY) instanceof Node) {
            Node taxonomyNode = (Node) vertex.getProperty(ContigProperty.TAXONOMY);
            getSelectedContigs().add(new SelectedContig(vertex.getID(), taxonomyNode.getScientificName()));
        }
        else getSelectedContigs().add(new SelectedContig(vertex.getID(), ""));
    }

    public void removeFromInfoTable(String unselectedNodeId) {
        getSelectedContigs().removeIf(sc -> sc.getID().equals(unselectedNodeId));
    }

    public TextField getSearchField(){ return searchField; }

    public void removeAllFromInfoTable() {
        getSelectedContigs().clear();
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

    public void initNewScrollPane() {scrollPane =  new ScrollPane();}

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

public void updateLabelCol(String labelType){
    labelCol.setText(labelType);
    }
}