// combines view & model
package presenter;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Tooltip;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.Node;
import view.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Presenter {
    Model model;
    View view;
    Map<String, ViewVertex> viewVertices = new HashMap<>();  //Hashmap of view vertex objects
    public final Dimension MAX_WINDOW_DIMENSION = new Dimension(1000, 679); //gets passed to model to center layouts, gets passed to view to control size of window
    UndirectedSparseGraph<MyVertex,MyEdge> seleGraph = new UndirectedSparseGraph<>();

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;
        setUpBindings();
    }

    public Presenter() { // second constructor needed for selection presenter to extend
    }

    // Action for the Menu: choose file
    private void setUpBindings() {

        //TODO: For all actions, we need to handle the exceptions, maybe show an error window (Caner)

        view.getOpenFileMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                FileChooser fc = new FileChooser();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("GFA Files", "*.gfa"));
                File f = fc.showOpenDialog(null);

                if (f != null) {
                    view.getProgressIndicator().setVisible(true);
                    view.getProgressIndicator().toFront();

                    if (model.getGraph() != null) {
                        reset();
                        view.getProgressIndicator().toFront();
                        view.getScrollPane().setDisable(true);
                    }
                    // parse gfa file to graph
                    Task<Void> parseGraphTask = new Task<Void>() {
                        @Override
                        protected Void call() throws Exception {
                            model.parseGraph(f.getAbsolutePath());
                            model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height));
                            view.getProgressIndicator().setVisible(false);
                            return null;
                        }
                    };
                    parseGraphTask.setOnSucceeded(e -> {
                        visualizeGraph();
                        view.getScrollPane().setDisable(false);
                        view.makeScrollPaneZoomable();
                        view.getImportTaxonomyMenuItem().setDisable(false);
                        view.getImportCoverageMenuItem().setDisable(false);
                        view.getCustomizeMenuItem().setDisable(false);
                        view.setLayoutRepulsionMultiplierSpinner(model.getRepulsionMultiplier());
                        view.setLayoutAttractionMultiplierSpinner(model.getAttractionMultiplier());
                        MenuItem recentFile = new MenuItem(f.getAbsolutePath());
                        if (!view.getOpenRecentFileMenu().getItems().contains(recentFile)){
                            setOpenRecentFileEventHandler(recentFile);
                            view.getOpenRecentFileMenu().getItems().add(recentFile);
                        }
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
                    view.setTaxaCountTextField("Taxa: " + model.getTaxaCount());
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

        //TODO: does this actually work? :) (Caner)
        view.getCloseMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                reset();
            }
        });

        view.getSaveAsPNGMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                WritableImage toSave = view.getScrollPane().snapshot(new SnapshotParameters(), null);
                FileChooser fc = new FileChooser();
                FileChooser.ExtensionFilter extf = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                fc.getExtensionFilters().add(extf);
                File namePNG = fc.showSaveDialog(null);
                if (!namePNG.getPath().endsWith(".png")) {
                    namePNG = new File(namePNG.getPath() + ".png");
                }
                try
                {
                    if(namePNG != null)
                    {
                        ImageIO.write(SwingFXUtils.fromFXImage(toSave, null), "png", namePNG);
                    }
                } catch (IOException e)
                {
                    System.out.println(e.toString());
                }
            }
        });

        view.getSelectionMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try { // new window for the Selection plot
                    Stage plotWindow = new Stage();
                    FXMLLoader loaderPlot = new FXMLLoader(getClass().getResource("../selection.fxml"));
                    Parent root = loaderPlot.load();
                    ViewSelection viewSelection = loaderPlot.getController();
                    PresenterSelection presenterSelection = new PresenterSelection(model, viewSelection);
                    plotWindow.setScene(new Scene(root));
                    plotWindow.initModality(Modality.APPLICATION_MODAL);
                    plotWindow.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        view.getColoringTaxonomyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                HashMap<Integer, String> taxIDRGBCode = model.createColor(model.getTaxaCount(), model.getTaxaID());

                for (MyVertex v : model.getGraph().getVertices()) {
                    Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
//                    System.out.println(taxIDRGBCode.size());
//                    System.out.println("TaxParser :" + taxNode.getId() + " | " + "TaxIDMap: " + taxIDRGBCode.);
                        if (taxIDRGBCode.keySet().contains(taxNode.getId())) {
                            String rgb = taxIDRGBCode.get(taxNode.getId());
                            String[] rgbCodes = rgb.split("t");
                            viewVertices.get(v.getID()).getCircle().setFill(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), Double.parseDouble(rgbCodes[3]))); //, rgbCodes[3]));
                        }
                        else if (taxNode.getId() == -100) {
                            viewVertices.get(v.getID()).getCircle().setFill(Color.rgb(255, 0, 255));
                        }
                }
            }
        });

        view.getLayoutApplyButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task<Void> layoutApplyTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        model.setRepulsionMultiplier(view.getLayoutRepulsionMultiplierSpinner());
                        model.setAttractionMultiplier(view.getLayoutAttractionMultiplierSpinner());
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height));
                        for (MyVertex mv : model.getGraph().getVertices()) {
                            ViewVertex vv = viewVertices.get(mv.getID());
                            //vv.animate(mv.getX(),mv.getY());
                            vv.setTranslateX(mv.getX());
                            vv.setTranslateY(mv.getY());
                        }
                        return null;
                    }
                };
                Thread layoutApplyThread = new Thread(layoutApplyTask);
                layoutApplyThread.setDaemon(true);
                layoutApplyThread.start();
            }
        });
    }

    private void visualizeGraph() {
        // add view vertices
        for (MyVertex v1 : model.getGraph().getVertices()) {
            // Save v1 in collection to check, it has already been created to avoid redundancies in loop below?
            ViewVertex vv = new ViewVertex(v1.getID(), 5, v1.getX(), v1.getY());
            view.addVertex(vv);
            viewVertices.put(v1.getID(), vv);
            makeDraggable(vv);
            chooseSelectionGraph(vv);
            Tooltip.install(vv, new Tooltip(vv.getID()));
        }
        // add view edges
        for (MyEdge edge : model.getGraph().getEdges()) {
            ViewEdge ve = new ViewEdge(viewVertices.get(edge.getFirst().getID()), viewVertices.get(edge.getSecond().getID()));
            view.addEdge(ve);
            ve.toBack();
        }
    }

    private void makeDraggable(ViewVertex viewVertex) {
        viewVertex.setOnMouseDragged(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            Bounds bounds = view.getInnerViewObjects().localToScene(view.getInnerViewObjects().getBoundsInLocal());
            double minX = bounds.getMinX();
            double minY = bounds.getMinY();
            if (x < minX ) {
                x = minX;
            }
            if (y < minY) {
                y = minY;
            }
            if (x > (MAX_WINDOW_DIMENSION.width + minX)) {
                x = MAX_WINDOW_DIMENSION.width + minX;
            }
            if (y > (MAX_WINDOW_DIMENSION.height + minY)) {
                y = MAX_WINDOW_DIMENSION.height + minY;
            }
            viewVertex.setTranslateX((x - minX) / view.getScaleProperty());
            viewVertex.setTranslateY((y - minY) / view.getScaleProperty());
        });
    }

    private void reset() {
        model.setGraph(null);
        view.getInnerViewObjects().getChildren().clear();
        viewVertices = new HashMap<>();
    }

    private void makeTooltip(ViewVertex viewVertex) {
        Tooltip tp = new Tooltip(viewVertex.getID());
        viewVertex.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                int x = (int) Math.ceil(event.getSceneX());
                int y = (int) Math.ceil(event.getSceneY());
                tp.show(viewVertex, x, y);
                tp.setShowDuration(Duration.seconds(1.0));
            }
        });
    }

    private void chooseSelectionGraph(ViewVertex viewVertex) {

        viewVertex.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                for(MyVertex v : model.getGraph().getVertices()) {
                    if (v.getID().equals(viewVertex.getID())) {
                        if (!seleGraph.containsVertex(v)) {
                            System.out.println("addded test: " + viewVertex.getID());
                            seleGraph.addVertex(new MyVertex(v));
                            for(MyEdge edge : this.model.getGraph().getInEdges(v)){
                                seleGraph.addEdge(edge, edge.getVertices());
                            }
                        } else if (seleGraph.containsVertex(v)) {
                            System.out.println("deleted test: " + viewVertex.getID());
                            seleGraph.removeVertex(new MyVertex(v));
                            for(MyEdge edge : this.model.getGraph().getInEdges(v)){
                                seleGraph.removeEdge(edge);
                            }
                        }

                    }
                }
            }
        });
    }

    private void setOpenRecentFileEventHandler(MenuItem menuItem){
        menuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getProgressIndicator().setVisible(true);

                if (model.getGraph() != null) {
                    reset();
                    view.getProgressIndicator().toFront();
                    view.getScrollPane().setDisable(true);
                }
                //TODO: duplicated code, extract to a method (Caner)
                // parse gfa file to graph
                Task<Void> parseGraphTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        model.parseGraph(menuItem.getText());
                        view.getProgressIndicator().setVisible(false);
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height));
                        return null;
                    }
                };
                parseGraphTask.setOnSucceeded(e -> {
                    visualizeGraph();
                    view.getScrollPane().setDisable(false);
                    view.makeScrollPaneZoomable();
                });

                Thread parseGraphThread = new Thread(parseGraphTask);
                parseGraphThread.setDaemon(true);
                parseGraphThread.start();
            }
        });
    }

}

