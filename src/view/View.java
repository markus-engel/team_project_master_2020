package view;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class View {

    @FXML
    public Group vertices;

    @FXML
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

    public TextField getFilenameTextfield() {
        return FilenameTextfield;
    }

    public void setFilenameTextfield(String filename) {
        FilenameTextfield.setText(filename);
    }

    public TextField getSequenceCountTextField() {
        return SequenceCountTextField;
    }

    public void setSequenceCountTextField(int sequenceCount) {
        SequenceCountTextField.setText(Integer.toString(sequenceCount));
    }

    public TextField getOverlapCountTextField() {
        return OverlapCountTextField;
    }
    public void setOverlapCountTextField(int overlapCount) {OverlapCountTextField.setText(Integer.toString(overlapCount));}



    public void addVertex(ViewVertex vv){

        //not good way, should do this in FXML
        if(vertices==null){
            vertices = new Group();
        }
        vertices.getChildren().add(vv);
        pane.getChildren().add(vv);


        //add bindings--> where??
    }

    public void addEdge(ViewEdge viewEdge){
        pane.getChildren().add(viewEdge);
    }

}

