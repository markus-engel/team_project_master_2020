// MainGUI class of the JavaFX GUI

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Model;
import presenter.Presenter;
import view.View;

public class MainGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Import FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui.fxml"));
        Parent root = loader.load();
        View view = loader.getController();
        Model model = new Model();
        Presenter presenter = new Presenter(model,view);

        stage.setTitle("Long-read assembly visualization");
        stage.setScene(new Scene(root, 600,275));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}