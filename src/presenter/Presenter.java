// combines view & model
package presenter;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.TaxonomyTree;
import view.View;
import view.ViewEdge;
import view.ViewVertex;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Presenter {
    Model model;
    View view;
    HashMap<String, view.ViewVertex> viewVertices = new HashMap<>();  //Hashmap of view vertex objects

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

                if (f != null) {

                    view.setFilenameTextfield("File: " + f.getAbsolutePath());


                    // parse gfa file to graph
                    Task<Void> parseGraphTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            model.parseGraph(f.getAbsolutePath());
                            return null;
                        }
                    };
                    parseGraphTask.setOnSucceeded(e -> visualizeGraph());

                    Thread parseGraphThread = new Thread(parseGraphTask);
                    parseGraphThread.setDaemon(true);
                    parseGraphThread.start();


                    // Check if gfa file was imported and parsed:
                    //System.out.print(model.getGraph().getVertices());

                    // after the graph is parsed, further optional imports can be enabled:
                    view.getImportTaxonomyMenuItem().setDisable(false);
                    view.getImportCoverageMenuItem().setDisable(false);

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

    private void visualizeGraph(){

        //add view vertices
        for (MyVertex v1: model.getGraph().getVertices()){
            // Save v1 in collection to check, it has already been created to avoid redundancies in loop below?
            ViewVertex vv = new ViewVertex(v1.getIDprop(), 5, model.getLayout().apply(v1).getX(),model.getLayout().apply(v1).getY());

            view.addVertex(vv);
            viewVertices.put(v1.getIDprop(),vv);
            makeDraggable(vv);
        }

        //add view edges
        for (MyEdge edge: model.getGraph().getEdges()){
            ViewEdge ve = new ViewEdge(viewVertices.get(edge.getFirst().getIDprop()),viewVertices.get(edge.getSecond().getIDprop()));
            view.addEdge(ve);
        }
    }

    private void makeDraggable(ViewVertex viewVertex){
        viewVertex.setOnMouseDragged(event -> {
            int x = (int)Math.ceil(event.getX());
            int y = (int)Math.ceil(event.getY());

            viewVertex.setCoords(x,y);
            viewVertex.toFront();
        });
    }

}

