package view;

import java.net.URL;
import java.util.ResourceBundle;

import com.sun.nio.file.SensitivityWatchEventModifier;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

public class View {

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
    private MenuItem SaveMenuItem;

    @FXML
    private MenuItem CloseMenuItem;

    @FXML
    private MenuItem ImportTaxonomyMenuItem;

    @FXML
    private MenuItem ImportCoverageMenuItem;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem AboutMenuItem;

    @FXML
    private TextArea ResultTextArea;

    @FXML
    private TextField FilenameTextfield;

    @FXML
    private TextField SequenceCountTextField;

    @FXML
    private TextField OverlapCountTextField;

    @FXML
    void initialize() {
        assert Pane != null : "fx:id=\"Pane\" was not injected: check your FXML file 'sample.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'sample.fxml'.";
        assert fileMenu != null : "fx:id=\"fileMenu\" was not injected: check your FXML file 'sample.fxml'.";
        assert ImportMenuItem != null : "fx:id=\"ImportMenuItem\" was not injected: check your FXML file 'sample.fxml'.";
        assert SaveMenuItem != null : "fx:id=\"SaveMenuItem\" was not injected: check your FXML file 'sample.fxml'.";
        assert CloseMenuItem != null : "fx:id=\"CloseMenuItem\" was not injected: check your FXML file 'sample.fxml'.";
        assert ImportTaxonomyMenuItem != null : "fx:id=\"ImportTaxonomyMenuItem\" was not injected: check your FXML file 'gui.fxml'.";
        assert ImportCoverageMenuItem != null : "fx:id=\"ImportCoverageMenuItem\" was not injected: check your FXML file 'gui.fxml'.";
        assert helpMenu != null : "fx:id=\"helpMenu\" was not injected: check your FXML file 'sample.fxml'.";
        assert AboutMenuItem != null : "fx:id=\"AboutMenuItem\" was not injected: check your FXML file 'sample.fxml'.";
        assert ResultTextArea != null : "fx:id=\"ResultTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert FilenameTextfield != null : "fx:id=\"FilenameTextfield\" was not injected: check your FXML file 'sample.fxml'.";
        assert SequenceCountTextField != null : "fx:id=\"SequenceCountTextField\" was not injected: check your FXML file 'sample.fxml'.";
        assert OverlapCountTextField != null : "fx:id=\"OverlapCountTextField\" was not injected: check your FXML file 'sample.fxml'.";

    }

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

    public TextArea getResultTextArea() {
        return ResultTextArea;
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
}

