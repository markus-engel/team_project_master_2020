// combines view & model
package presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.Model;
import view.View;

import java.io.File;
import java.io.IOException;

public class Presenter {
    Model model;
    View view;

    public Presenter(Model model, View view){
        this.model = model;
        this.view = view;
        setUpBindings();
    }

    // Action for the Menu: choose file
    private void setUpBindings() {
        view.getImportMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fc = new FileChooser();
                //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GFA Files", "*.gfa"));
                File f = fc.showOpenDialog(null);
                if (f!=null) {
                    view.setFilenameTextfield("File: " + f.getAbsolutePath());
                }

                try {
                    // is this how to catch the graph correctly to use it further?
                    model.actionParseGraph(f.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
