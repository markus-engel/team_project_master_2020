package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class View {

    @FXML
    private Group viewObjects;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ProgressIndicator progressIndicator;

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

    // getter and setter Methods. More have to be implemented if needed
    public MenuItem getImportMenuItem() {
        return ImportMenuItem;
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

        //not good way, should do this in FXML
        if (viewObjects == null) {
            viewObjects = new Group();
        }
        viewObjects.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        if (viewObjects == null) {
            viewObjects = new Group();
        }
        viewObjects.getChildren().add(viewEdge);
    }

    public void setScrollPane() {this.scrollPane.setContent(this.viewObjects);}

    public ScrollPane getScrollPane(){ return scrollPane;}

    public void makeScrollAndZoomable() {
        EventHandler<ScrollEvent> zoom = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (scrollEvent.getDeltaY() > 0) ? 1.2 : 1 / 1.2; //scaleFactor -> final

                viewObjects.setScaleX(viewObjects.getScaleX() * scaleFactor);
                viewObjects.setScaleY(viewObjects.getScaleY() * scaleFactor);
            }
        };
        scrollPane.setOnKeyPressed(pressedEvent -> {
            if (pressedEvent.isControlDown()) {
                scrollPane.setOnScroll(zoom);
            }
            pressedEvent.consume();
        });
        scrollPane.setOnKeyReleased(releasedEvent -> {
            scrollPane.setOnScroll(null);

        });
    }
}