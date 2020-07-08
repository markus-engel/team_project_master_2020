package view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class View {

    @FXML
    public Group vertices;

    @FXML
    private ScrollPane scrollPane;

    private Pane pane;

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
    private TabPane tabPanePlots;

    @FXML
    private Tab tabGcCoverage;

    @FXML
    private Tab tabElse;

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

    public void addVertex(ViewVertex vv) {

        //not good way, should do this in FXML
        if (vertices == null) {
            vertices = new Group();
        }
        vertices.getChildren().add(vv);
        //pane.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        if (vertices == null) {
            vertices = new Group();
        }
        vertices.getChildren().add(viewEdge);
        //pane.getChildren().add(viewEdge);
    }

    public void setScrollPane() {
        scrollPane.setContent(this.vertices);
        EventHandler<ScrollEvent> zoom = new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if (scrollEvent.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor = (scrollEvent.getDeltaY() > 0) ? 1.2 : 1 / 1.2; //scaleFactor -> final

                vertices.setScaleX(vertices.getScaleX() * scaleFactor);
                vertices.setScaleY(vertices.getScaleY() * scaleFactor);
            }
        };
        scrollPane.setOnKeyPressed(pressedEvent -> {
            if (pressedEvent.isControlDown()) {
                System.out.print("yes");
                scrollPane.setOnScroll(zoom);
            }
            pressedEvent.consume();
        });
        scrollPane.setOnKeyReleased(releasedEvent -> {
            System.out.print("nop");
            scrollPane.removeEventHandler(ScrollEvent.SCROLL,zoom);

        });
    }
}