package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class View {

    public final double MAX_ZOOM_SCALE = 2.d;
    public final double MIN_ZOOM_SCALE = .5d;

    @FXML
    private Group viewObjects;

    @FXML
    private Group innerViewObjects;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private MenuItem layoutSettingsMenuItem;

    @FXML
    private MenuItem colorPlate;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private BorderPane Pane;

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
    private Menu viewMenu;

    @FXML
    private MenuItem showTaxLegend;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem AboutMenuItem;

    @FXML
    private TextField FilenameTextfield;

    @FXML
    private TextField SequenceCountTextField;

    @FXML
    private TextField OverlapCountTextField;

    @FXML
    private TextField currentSequenceTextfield;

    @FXML
    private TextField differentTaxaCount;

    @FXML
    private Menu PlotMenu;

    @FXML
    private MenuItem CoverageGCMenu;

    @FXML
    private MenuItem SelectionMenuItem;

    // getter and setter Methods. More have to be implemented if needed
    public MenuItem getOpenFileMenuItem() {
        return openFileMenuItem;
    }

    public Menu getOpenRecentFileMenu() {
        return openRecentFileMenu;
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

    public TextField getFilenameTextfield() {
        return FilenameTextfield;
    }

    public void setFilenameTextfield(String filename) {
        FilenameTextfield.setText(filename);
    }

    public TextField getSequenceCountTextField() {
        return SequenceCountTextField;
    }

    public void setDifferentTaxaCount(String size) {differentTaxaCount.setText(String.valueOf(size));}

    public void setCurrentSequenceTextField(String currentSeq) {
        currentSequenceTextfield.setText("Sequences: " + currentSeq);
    }
    // Number of Vertices
    public void setSequenceCountTextField(String sequenceCount) {
        SequenceCountTextField.setText("Sequences: " + sequenceCount);
    }
    public TextField getOverlapCountTextField() {
        return OverlapCountTextField;
    }

    // Number of Edges
    public void setOverlapCountTextField(int overlapCount) {
        OverlapCountTextField.setText("Overlaps: " + overlapCount);
    }

    public Group getViewObjects(){ return viewObjects;}

    public Group getInnerViewObjects() { return innerViewObjects;}

    public ProgressIndicator getProgressIndicator(){ return progressIndicator;}

    public void setInnerViewObjects(Group innerViewObjects) { this.innerViewObjects = viewObjects;}

    public MenuItem getLayoutSettingsMenuItem(){ return layoutSettingsMenuItem;}

    public void addVertex(ViewVertex vv) {
         innerViewObjects.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        innerViewObjects.getChildren().add(viewEdge);
    }

    public ScrollPane getScrollPane(){ return scrollPane;}

    public void makeScrollPaneZoomable() {
        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.isControlDown()){ // wenn scrollen disabled werden soll, dann hier !scrollevent.isConsumed()
                    final double scale = calculateScaleForZooming(scrollEvent);
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
