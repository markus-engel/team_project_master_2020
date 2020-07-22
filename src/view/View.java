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

    public final double MAX_SCALE = 2.d;
    public final double MIN_SCALE = .5d;

    @FXML
    private Group viewObjects;

    @FXML
    private Group innerViewObjects;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private MenuItem customizeMenuItem;

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
    private MenuItem ImportMenuItem;

    @FXML
    private MenuItem ImportTaxonomyMenuItem;

    @FXML
    private MenuItem ImportCoverageMenuItem;

    @FXML
    private MenuItem SaveMenuItem;

    @FXML
    private MenuItem CloseMenuItem;

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
    private Menu PlotMenu;

    @FXML
    private MenuItem CoverageGCMenu;

    @FXML
    private MenuItem SelectionMenuItem;

    // getter and setter Methods. More have to be implemented if needed
    public MenuItem getImportMenuItem() {
        return ImportMenuItem;
    }

    public MenuItem getCustomizeMenuItem() {
        return customizeMenuItem;
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


    // Number of Vertices
    public void setSequenceCountTextField(int sequenceCount) {
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

    public ProgressIndicator getProgressIndicator(){ return progressIndicator;}

    public void setViewObjects(Group viewObjects) { this.viewObjects = viewObjects;}

    public void addVertex(ViewVertex vv) {
        viewObjects.getChildren().add(vv);
        innerViewObjects.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        viewObjects.getChildren().add(viewEdge);
        innerViewObjects.getChildren().add(viewEdge);
    }

    public void setScrollPane() {
        scrollPane.setContent(viewObjects);
        makeZoomable();
    }

    public ScrollPane getScrollPane(){ return scrollPane;}

    private void makeZoomable() {
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
        if (scale <= MIN_SCALE) {
            scale = MIN_SCALE;
        } else if (scale >= MAX_SCALE) {
            scale = MAX_SCALE;
        }
        return scale;
    }
}
