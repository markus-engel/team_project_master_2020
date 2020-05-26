package view;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class View {

    private Stage stage;

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

    public View() {
        this.Pane = new BorderPane();
        this.menuBar = new MenuBar();
        this.fileMenu = new Menu();
        this.ImportMenuItem = new MenuItem();
        this.SaveMenuItem = new MenuItem();
        this.CloseMenuItem = new MenuItem();
        this.helpMenu = new Menu();
        this.AboutMenuItem = new MenuItem();
        this.ResultTextArea = new TextArea();
        this.FilenameTextfield = new TextField();
        this.SequenceCountTextField = new TextField();
        this. OverlapCountTextField = new TextField();
    }

    // Getter methods
    public BorderPane getPane() { return Pane;}

    public MenuItem getImportMenuItem(){ return ImportMenuItem;}

    public MenuItem getSaveMenuItem() { return SaveMenuItem;}

    public MenuItem getCloseMenuItem() { return CloseMenuItem;}

    public MenuItem getAboutMenuItem(){ return AboutMenuItem;}

    public TextField getFilenameTextfield() { return FilenameTextfield;}

    public TextField getSequenceCountTextField() { return SequenceCountTextField;}

    public TextField getOverlapCountTextField() { return OverlapCountTextField;}

    public Stage getStage() { return stage;}

    public void setStage(Stage stage) { this.stage = stage;}
}
