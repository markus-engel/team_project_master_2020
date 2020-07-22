// combines view & model
package presenter;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.GraphParser;
import view.View;
import view.ViewEdge;
import view.ViewPlot;
import view.ViewVertex;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Presenter {
    Model model;
    View view;
    HashMap<String, view.ViewVertex> viewVertices = new HashMap<>();  //Hashmap of view vertex objects
    public final Dimension MAX_WINDOW_DIMENSION = new Dimension(2000,1200); //gets passed to model to center layouts, gets passed to view to control size of window

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

                    view.setFilenameTextfield("File: " + f.getName());
                    view.getProgressIndicator().setVisible(true);

                    if(model.getGraph() != null){
                        reset();
                        view.getProgressIndicator().toFront();
                        view.getScrollPane().setDisable(true);
                    }
                    // parse gfa file to graph
                    Task<Void> parseGraphTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            model.parseGraph(f.getAbsolutePath(), new Dimension(MAX_WINDOW_DIMENSION.width,MAX_WINDOW_DIMENSION.height));
                            view.getProgressIndicator().setVisible(false);
                            return null;
                        }
                    };
                    parseGraphTask.setOnSucceeded(e -> {
                        visualizeGraph(5);
                        view.getScrollPane().setDisable(false);
                        view.getImportTaxonomyMenuItem().setDisable(false);
                        view.getImportCoverageMenuItem().setDisable(false);
                        view.getCustomizeMenuItem().setDisable(false);
                    });

                    Thread parseGraphThread = new Thread(parseGraphTask);
                    parseGraphThread.setDaemon(true);
                    parseGraphThread.start();
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
                    view.setDifferentTaxaCount("diff. taxa count: " + model.getTaxaCount());
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
                    view.getCoverageGCMenu().setDisable(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        view.getCoverageGCMenu().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    // Open new window for Coverage-GC plots with plot.fxml
                    Stage plotWindow = new Stage();
                    FXMLLoader loaderPlot = new FXMLLoader(getClass().getResource("../plot.fxml"));
                    Parent root = loaderPlot.load();
                    ViewPlot viewplot = loaderPlot.getController();
                    PresenterPlot presenterPlot = new PresenterPlot(model, viewplot);
                    plotWindow.setTitle("Plots");
                    plotWindow.setScene(new Scene(root));
                    plotWindow.initModality(Modality.APPLICATION_MODAL);
                    plotWindow.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        view.getCustomizeMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.print("JHHIEA");
            }
        });
    }

    private void visualizeGraph(int size){
        // add view vertices
        for (MyVertex v1: model.getGraph().getVertices()){
            // Save v1 in collection to check, it has already been created to avoid redundancies in loop below?
            ViewVertex vv = new ViewVertex(v1.getIDprop(), size, v1.getX(),v1.getY());
            view.addVertex(vv);
            viewVertices.put(v1.getIDprop(),vv);
            selectNode(vv);
            makeDraggable(vv, size);

        }
        // add view edges
        for (MyEdge edge: model.getGraph().getEdges()){
            ViewEdge ve = new ViewEdge(viewVertices.get(edge.getFirst().getIDprop()),viewVertices.get(edge.getSecond().getIDprop()));
            view.addEdge(ve);
        }
        /*
        // add lonely view vertices
        for (MyVertex v: model.getLonelyGraph().getVertices()){
            ViewVertex vv = new ViewVertex(v.getIDprop(), size, model.getLonelyLayout().apply(v).getX(), model.getLonelyLayout().apply(v).getY());
            view.addVertex(vv);
            selectNode(vv);
            makeDraggable(vv, size);

        } */
        // apply viewObjects onto Scrollpane
        view.setScrollPane();
    }

    private void makeDraggable(ViewVertex viewVertex, int size){
        viewVertex.setOnMouseDragged(event -> {
            int x = (int)Math.ceil(event.getX());
            int y = (int)Math.ceil(event.getY());
            if (x < 0 + size){
                x = 0 + size;
            }
            if (y < 0 + size){
                y = 0 + size;
            }
            if (x > MAX_WINDOW_DIMENSION.width - size){
                x= MAX_WINDOW_DIMENSION.width - size;
            }
            if (y > MAX_WINDOW_DIMENSION.height - size){
                y = MAX_WINDOW_DIMENSION.height - size;
            }
            viewVertex.setCoords(x,y);
            viewVertex.toFront();
        });
    }

    private void selectNode(ViewVertex viewVertex) {

        viewVertex.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {

                view.setCurrentSequenceTextField(viewVertex.getID());

                final Tooltip tp = new Tooltip(viewVertex.getID());
                tp.setShowDelay(Duration.seconds(3.0));
                tp.show(viewVertex,
                        viewVertex.getLayoutX() + viewVertex.getScene().getX() + viewVertex.getScene().getWindow().getX(),
                        viewVertex.getLayoutY() + viewVertex.getScene().getY() + viewVertex.getScene().getWindow().getY());
                // this didn't work:
                //int x = (int)Math.ceil(event.getSceneX());
                //int y = (int)Math.ceil(event.getSceneY());
                //tp.show(viewVertex, x, y);

                //tp.setAnchorX(viewVertex.getLayoutX());
                //tp.setAnchorY(viewVertex.getLayoutY());

                //Tooltip.install(viewVertex, tp);
            }
        });
    }

    private void reset(){
        model.setGraph(null);
        view.setViewObjects(null);
        viewVertices = new HashMap<>();
    }
}

