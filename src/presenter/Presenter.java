// combines view & model
package presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import model.Model;
import model.graph.MyVertex;
import view.View;
import view.ViewEdge;
import view.ViewVertex;

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
                    model.parseGraph(f.getAbsolutePath());
                    visualizeGraph();

                    // Check if gfa file was imported and parsed
                    //System.out.println(model.getGraph().getVertices());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    public void visualizeGraph(){

        for (MyVertex v1: model.getGraph().getVertices()){

            ViewVertex vv1 = new ViewVertex(v1.getIDprop(), 10, model.getLayout().apply(v1).getX(),model.getLayout().apply(v1).getY());

            view.addVertex(vv1);

            //adding Edges through neighbors
            for (MyVertex v2: model.getGraph().getPredecessors(v1)){
                ViewVertex vv2 = new ViewVertex(v2.getIDprop(), 10, model.getLayout().apply(v2).getX(),model.getLayout().apply(v2).getY());

                ViewEdge viewEdge = new ViewEdge(vv1, vv2);

                view.addEdge(viewEdge);
            }
        }
    }

}
