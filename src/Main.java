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

        View view = new View();
        view.setStage(stage);
        Model model = new Model();

        Presenter presenter = new Presenter(model, view);
        stage.setTitle("Test");
        stage.setScene(new Scene(root, 600, 275));
        stage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
