// combines view & model
package presenter;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
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

                if (f != null) try {
                    view.setFilenameTextfield("File: " + f.getAbsolutePath());

                    // parse gfa file to graph
                    model.parseGraph(f.getAbsolutePath());
                    visualizeGraph();
                    view.setSequenceCountTextField(model.getGraph().getVertexCount());
                    view.setOverlapCountTextField(model.getGraph().getEdgeCount());

                    // after the graph is parsed, further optional imports can be enabled:
                    view.getImportTaxonomyMenuItem().setDisable(false);
                    view.getImportCoverageMenuItem().setDisable(false);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        view.getImportTaxonomyMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fc = new FileChooser();
                File f = fc.showOpenDialog(null);
                if (f != null) try {
                    model.parseTaxId(f.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        view.getImportCoverageMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fc = new FileChooser();
                File f = fc.showOpenDialog(null);
                if (f != null) try {
                    model.parseCoverage(f.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        view.getCoverageGCMenu().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Stage dialog = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("../plot.fxml"));
                    dialog.setTitle("Plots");
                    dialog.setScene(new Scene(root));
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    //dialog.initOwner(); ?
                    dialog.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public void visualizeGraph(){

        for (MyVertex v1: model.getGraph().getVertices()){

            ViewVertex vv1 = new ViewVertex(v1.getIDprop(), 3, model.getLayout().apply(v1).getX(),model.getLayout().apply(v1).getY());

            view.addVertex(vv1);

            //adding Edges through neighbors
            for (MyVertex v2: model.getGraph().getPredecessors(v1)){
                ViewVertex vv2 = new ViewVertex(v2.getIDprop(), 3, model.getLayout().apply(v2).getX(),model.getLayout().apply(v2).getY());

                ViewEdge viewEdge = new ViewEdge(vv1, vv2);

                view.addEdge(viewEdge);
            }
        }
    }

}

