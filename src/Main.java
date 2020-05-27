import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;
import model.Model;
import presenter.Presenter;
import view.View;

public class Main extends Application {

    public void start(Stage stage) throws Exception {
        // Import FXML file
        Parent root = FXMLLoader.load(Main.class.getResource("gui.fxml"));

        Scene scene = new Scene(root);
        stage.setTitle("Test");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
