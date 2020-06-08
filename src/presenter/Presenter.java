// combines view & model
package presenter;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.FileChooser;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.MainParserGraph;
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
                    // the parsing method MainParserGraph.readFile() returns a graph
                    // is this how to catch the graph correctly to use it further?
                    UndirectedSparseGraph<MyVertex, MyEdge> currGraph = new UndirectedSparseGraph<MyVertex, MyEdge>();
                    currGraph = MainParserGraph.readFile(f.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
