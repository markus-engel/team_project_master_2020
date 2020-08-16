// combines view & model
package presenter;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.Node;
import view.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Presenter {
    Model model;
    View view;
    HashMap<Integer, String> taxIDRGBCode, colorIndividualRank;
    HashMap<Integer, ArrayList> rankMembers;
    Map<String, ViewVertex> viewVertices = new HashMap<>();  //Hashmap of view vertex objects
    Map<String, ViewVertex> viewVerticesSelection = new HashMap<>();  //Hashmap of view vertex objects
    public final Dimension MAX_WINDOW_DIMENSION = new Dimension(775, 500); //gets passed to model to center layouts, gets passed to view to control size of window
    UndirectedSparseGraph<MyVertex,MyEdge> seleGraph = new UndirectedSparseGraph<>();
    Boolean rank = false, taxonomy = false;
    int countSelected = 0;


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

        view.getNewFileMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getScrollPane().setDisable(true);
                view.getImportTaxonomyMenuItem().setDisable(true);
                view.getImportCoverageMenuItem().setDisable(true);
                view.getCustomizeMenuItem().setDisable(true);
                view.getSelectAllMenuItem().setDisable(true);
                view.getSelectionMenu().setDisable(true);
                reset();
            }
        });

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
                            model.parseGFA(f.getAbsolutePath());
                            model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), view.getOrderByContigLengthRadioButton().isSelected());
                            view.getProgressIndicator().setVisible(false);
                            return null;
                        }
                    };
                    parseGraphTask.setOnSucceeded(e -> {
                        visualizeGraph(model.getGraph(), view.getInnerViewObjects().getChildren(), view.getInnerViewObjects());
                        view.getScrollPane().setDisable(false);
                        view.makeScrollPaneZoomable(view.getScrollPane());
                        applyDragSelectRectangleFunctionality();
                        view.getImportTaxonomyMenuItem().setDisable(false);
                        view.getImportCoverageMenuItem().setDisable(false);
                        view.getCustomizeMenuItem().setDisable(false);
                        view.getSelectAllMenuItem().setDisable(false);
                        view.getResetSelectionMenuItem().setDisable(false);
                        view.setSequenceCountTextField(model.getGraph().getVertexCount());
                        view.setOverlapCountTextField(model.getGraph().getEdgeCount());
                        view.getColoringGCcontentRadioButton().setDisable(false);
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
                    view.getColoringTaxonomyRadioButton().setDisable(false);
//                    view.getColoringTaxonomyChoiceBox().setDisable(false);
                    view.getColoringRankRadioButton().setDisable(false);
                    view.getColoringTransparencyRadioButton().setDisable(false);
                    updateSelectionInformation();
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
                    view.getNodeSizeCoverageRadioButton().setDisable(false);
                    view.getColoringCoverageRadioButton().setDisable(false);
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
        // for me it does (Anna)
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

        /*
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
        }); */

        view.getTabSelection().setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                resetTab();
                if (view.getTabSelection().isSelected()) {
                    System.out.println("Selection tab recognized");
                    model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), seleGraph, view.getOrderByContigLengthRadioButton().isSelected());
                    visualizeSelectionGraph(seleGraph, view.getInnerViewObjectsSele().getChildren(), view.getInnerViewObjectsSele());
                    view.getScrollPaneSele().setDisable(false);
                    view.makeScrollPaneZoomable(view.getScrollPaneSele());
                }
            }
        });


        view.getColoringTaxonomyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                taxIDRGBCode = model.createColor(model.getTaxaCount(), model.getTaxaID());
                if (rank) {
                    rank = false;
                }

                for (MyVertex v : model.getGraph().getVertices()) {
                    Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
                    if (taxIDRGBCode.keySet().contains(taxNode.getId())) {
                        String rgb = taxIDRGBCode.get(taxNode.getId());
                        String[] rgbCodes = rgb.split("t");
                        viewVertices.get(v.getID()).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2])));

                        //updating legend
                        LegendItem legendItem = new LegendItem(new Circle(5,Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]))), taxNode.getScientificName());
                        if (!view.getLegendItems().contains(legendItem)){
                            view.getLegendItems().add(legendItem);
                        }
                    }
                    else if (taxNode.getId() == -100) {
                        viewVertices.get(v.getID()).setColour(Color.rgb(0, 255, 0));
                    }
                }
                view.updateLabelCol("Taxonomy");
                view.getShowLegendMenuItem().setDisable(false);

                taxonomy = true;
            }
        });

        view.getColoringRankRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList rankNames = FXCollections.observableArrayList();
                if (taxonomy) {
                    taxonomy = false;
                }

                rank = true;
                view.getColoringRankChoiceBox().setDisable(false);
                rankNames.add("none");
                rankNames.add("superkingdom");
                rankNames.add("kingdom");
                rankNames.add("genus");
                rankNames.add("phylum");
                rankNames.add("species group");
                rankNames.add("class");
                rankNames.add("species");
                rankNames.add("order");
                rankNames.add("subspecies");
                rankNames.add("family");
                rankNames.add("variates");

                view.getColoringRankChoiceBox().setItems(rankNames);
                view.getShowLegendMenuItem().setDisable(false);
            }
        });

        view.getColoringRankChoiceBox().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String chosenRank = (String) view.getColoringRankChoiceBox().getValue();
                HashMap<String, ArrayList> allIndividualsPerRank = model.getAllIndividualsPerRank();
                rankMembers = model.getAllMembersPerRankFamily(allIndividualsPerRank, chosenRank);

                colorIndividualRank = model.createColorRank(rankMembers);

                for (MyVertex v : model.getGraph().getVertices()) {
                    viewVertices.get(v.getID()).setColour(Color.rgb(250, 235, 215));
                }

                for (Object i : rankMembers.keySet()) {
                    ArrayList verticesCiontigID = rankMembers.get(i);
                    String rgbCodeTotal = colorIndividualRank.get(i);
                    String[] rgbCodes = rgbCodeTotal.split("t");
                    for (Object j : verticesCiontigID) {
                        for (MyVertex v : model.getGraph().getVertices()) {
                            if (v.getID().equals(j)) {
                                viewVertices.get(v.getID()).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2])));
                            }
                        }
                    }
                }
            }
        });

        view.getColoringDefaultRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                for (MyVertex v : model.getGraph().getVertices()) {
                    viewVertices.get(v.getID()).setColour(Color.CORAL);
                }

                //Updates Legend on the side.
                view.getShowLegendMenuItem().setDisable(true);
                view.getLegendTableView().setPrefWidth(0);
            }
        });

        view.getHelpMenu().setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });

        view.getColoringTransparencyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getColoringTransparencySlider().setValue(1);
            }
        });
        view.getColoringTransparencySlider().disableProperty().bind(view.getColoringTransparencyRadioButton().selectedProperty().not());
        view.getColoringTransparencySlider().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (rank) {
                    for (Object i : rankMembers.keySet()) {
                        ArrayList verticesCiontigID = rankMembers.get(i);
                        String rgbCodeTotal = colorIndividualRank.get(i);
                        String[] rgbCodes = rgbCodeTotal.split("t");
                        for (Object j : verticesCiontigID) {
                            for (MyVertex v : model.getGraph().getVertices()) {
                                if (v.getID().equals(j)) {
                                    viewVertices.get(v.getID()).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue()));
                                }
                            }
                        }
                    }
                }
                else if (taxonomy) {
                    for (MyVertex v : model.getGraph().getVertices()) {
                        Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
                        if (taxIDRGBCode.keySet().contains(taxNode.getId())) {
                            String rgb = taxIDRGBCode.get(taxNode.getId());
                            String[] rgbCodes = rgb.split("t");
                            viewVertices.get(v.getID()).getCircle().setFill(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue()));
                        } else if (taxNode.getId() == -100) {
                            viewVertices.get(v.getID()).getCircle().setFill(Color.rgb(0, 255, 0));
                        }
                    }
                }

            }
        });

        view.getExportSelectionSequencesMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                StringBuilder content = new StringBuilder("");
                for(MyVertex mv : seleGraph.getVertices()){
                    content.append(">"+ mv.getID() + "\n");
                    content.append(mv.getSequenceprop() + "\n");
                }
                FileChooser fc = new FileChooser();
                File file = fc.showSaveDialog(null);
                FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("FASTA files (*.fasta)","*.fasta");
                fc.getExtensionFilters().add(extensionFilter);
               if(file != null){
                   try {
                       FileWriter fileWriter = new FileWriter(file);
                       fileWriter.write(content.toString());
                       fileWriter.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            }
        });

        view.getSelectAllMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                viewVerticesSelection = viewVertices;
                for(ViewVertex vv : viewVertices.values()){
                    if (!vv.isSelected()) {
                        vv.setSelected();
                        updateSelectionGraph(vv);
                    }
                }
            }
        });

        view.getResetSelectionMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //unselects all vertices
                for (ViewVertex vv : viewVertices.values()) {
                    if (vv.isSelected()) {
                        vv.setSelected();
                    }
                }
                seleGraph = new UndirectedSparseGraph<>();
                viewVerticesSelection = new HashMap<>();
                updateSelectionInformation();
                resetTab();
            }
        });

        view.getLayoutApplyButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                changeLayoutParameters(view.getLayoutApplyButton());
            }
        });

        view.getLayoutSettingsMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Stage popupWindow = new Stage();
                popupWindow.initModality(Modality.APPLICATION_MODAL);
                popupWindow.setTitle("Layout Settings");

                Label repulsionLabel = new Label("Repulsion multiplier");
                Label attractionLabel = new Label("Attraction multiplier");
                Button applyButton = new Button("Apply");
                applyButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        changeLayoutParameters(applyButton);

                    }
                });
                VBox layout = new VBox(5);
                layout.getChildren().addAll(repulsionLabel,view.getLayoutRepulsionMultiplierSpinner(),attractionLabel,view.getLayoutAttractionMultiplierSpinner(), applyButton);
                BorderPane bp = new BorderPane();
                bp.setCenter(layout);
                bp.getCenter().setTranslateX(60);
                bp.getCenter().setTranslateZ(60);
                Scene scene = new Scene(bp, 200, 150);
                popupWindow.setScene(scene);
                popupWindow.show();
            }
        });


        view.getNodeSizeScaleChoiceBox().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                view.getNodeSizeGroup().selectToggle(view.getNodeSizeDefaultRadioButton());
                if (!viewVertices.isEmpty()) for (ViewVertex vv : viewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });

        view.getNodeSizeCoverageRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) {
                    double lowestCoverage = model.getLowestCoverage();
                    double highestCoverage = model.getHighestCoverage();
                    double range = highestCoverage - lowestCoverage;
                    for (MyVertex v : model.getGraph().getVertices()) {
                        ViewVertex vv = viewVertices.get(v.getID());
                        double relativeCoverage = ((double) v.getProperty(ContigProperty.COVERAGE) - lowestCoverage) / range;
                        if (view.getNodeSizeScaleChoiceBox().getValue().equals("linear scale")) {
                            vv.setSize(2 + (6 * relativeCoverage));
                        } else if (view.getNodeSizeScaleChoiceBox().getValue().equals("logarithmic scale")) {
                            vv.setSize(Math.log(2 * (double) v.getProperty(ContigProperty.COVERAGE)));
                        }
                    }
                    view.getNodeSizeManualSlider().setValue(5);
                }
            }
        });

        view.getColoringCoverageRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) {
                    HashMap<Object, Double> coverage = model.heatmapColorsCovarge();
                    for (MyVertex v : model.getGraph().getVertices()) {
                        for (Object j : coverage.keySet()) {
                            if (v.getID().equals(j)) {
                                if (coverage.get(j) < 0.5) {
                                    viewVertices.get(v.getID()).setColour(Color.hsb(120, 1 - coverage.get(j), 0.49 + coverage.get(j)));
                                }
                                else if (coverage.get(j) >= 0.5) {
                                    viewVertices.get(v.getID()).setColour(Color.hsb(0, coverage.get(j), 1));
                                }
//                                viewVertices.get(v.getID()).setColour(Color.DARKBLUE);
//                                viewVertices.get(v.getID()).setColour(Color.hsb(240, gcCoverage.get(j), 1));
                            }
                        }
                    }
                }
            }
        });

        view.getColoringGCcontentRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) {
                    HashMap<Object, Double> gcContent = model.heatmapColorsGCContent();
                    for (MyVertex v : model.getGraph().getVertices()) {
                        for (Object i : gcContent.keySet()) {
                            if (v.getID().equals(i)) {
                                if (gcContent.get(i) < 0.5) {
                                    viewVertices.get(v.getID()).setColour(Color.hsb(120, 1 - gcContent.get(i), 0.49 + gcContent.get(i)));
                                }
                                else if (gcContent.get(i) >= 0.5) {
                                    viewVertices.get(v.getID()).setColour(Color.hsb(0, gcContent.get(i), 1));
                                }
                            }
                        }
                    }
                }
            }
        });

        view.getNodeSizeContigLengthRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) {
                    double smallestContigLength = model.getSmallestContigLength();
                    double largestContigLength = model.getLargestContigLength();
                    double range = largestContigLength - smallestContigLength;
                    for (MyVertex v : model.getGraph().getVertices()) {
                        ViewVertex vv = viewVertices.get(v.getID());
                        double contigLength = (double) v.getProperty(ContigProperty.LENGTH);
                        double relativeContigLength = (contigLength - smallestContigLength) / range;
                        if (view.getNodeSizeScaleChoiceBox().getValue().equals("linear scale")) {
                            vv.setSize(2 + (6 * relativeContigLength));
                        } else if (view.getNodeSizeScaleChoiceBox().getValue().equals("logarithmic scale")) {
                            vv.setSize(Math.log(contigLength / 1000.0));
                        }
                    }
                    view.getNodeSizeManualSlider().setValue(5);
                }
            }
        });
        view.getNodeSizeDefaultRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) for (ViewVertex vv : viewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });

        view.getNodeSizeManualSlider().disableProperty().bind(view.getNodeSizeManualRadioButton().selectedProperty().not());
        view.getNodeSizeManualRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!viewVertices.isEmpty()) for (ViewVertex vv : viewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });
        view.getNodeSizeManualSlider().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (!viewVertices.isEmpty() & view.getNodeSizeScaleChoiceBox().getValue().equals("linear scale"))
                    for (ViewVertex vv : viewVertices.values()) {
                        vv.setSize(view.getNodeSizeManualSlider().getValue());
                    }
                else if (!viewVertices.isEmpty() & view.getNodeSizeScaleChoiceBox().getValue().equals("logarithmic scale"))
                    for (ViewVertex vv : viewVertices.values()) {
                        vv.setSize(Math.log(view.getNodeSizeManualSlider().getValue()));
                    }
            }
        });

        view.getOrderByNodeNumbersRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task<Void> layoutApplyTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), false);
                        return null;
                    }
                };
                layoutApplyTask.setOnSucceeded(e -> {
                    for (MyVertex mv : model.getGraph().getVertices()) {
                        ViewVertex vv = viewVertices.get(mv.getID());
                        vv.animate(mv.getX(), mv.getY());
                    }
                });
                Thread layoutApplyThread = new Thread(layoutApplyTask);
                layoutApplyThread.setDaemon(true);
                layoutApplyThread.start();
            }
        });

        view.getOrderByContigLengthRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Task<Void> layoutApplyTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), true);
                        return null;
                    }
                };
                layoutApplyTask.setOnSucceeded(e -> {
                    for (MyVertex mv : model.getGraph().getVertices()) {
                        ViewVertex vv = viewVertices.get(mv.getID());
                        vv.animate(mv.getX(), mv.getY());
                    }
                    System.out.println("new layout with order by contig length done");
                });
                Thread layoutApplyThread = new Thread(layoutApplyTask);
                layoutApplyThread.setDaemon(true);
                layoutApplyThread.start();
            }
        });

        view.getShowLegendMenuItem().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (view.getShowLegendMenuItem().isSelected()) {
                    view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                } else {
                    view.getLegendTableView().setPrefWidth(0);
                }
            }
        });

    }


    private void visualizeGraph(UndirectedSparseGraph<MyVertex,MyEdge> currentGraph, ObservableList observableList, Group innerObjects) {
        // add view vertices
        for (MyVertex v1 : currentGraph.getVertices()) {
            // Save v1 in collection to check, it has already been created to avoid redundancies in loop below?
            ViewVertex vv = new ViewVertex(v1.getID(), 5, v1.getX(), v1.getY());
            view.addVertex(vv, observableList);
            viewVertices.put(v1.getID(), vv);
            makeDraggable(vv, innerObjects);
            makeSelectable(vv);
            Tooltip.install(vv, new Tooltip(vv.getID()));
        }
        // add view edges
        for (MyEdge edge : currentGraph.getEdges()) {
            ViewEdge ve = new ViewEdge(viewVertices.get(edge.getFirst().getID()), viewVertices.get(edge.getSecond().getID()));
            view.addEdge(ve, observableList);
            ve.toBack();
        }
    }

    private void visualizeSelectionGraph(UndirectedSparseGraph<MyVertex, MyEdge> currentGraph, ObservableList observableList, Group innerObjects) {
        // add view vertices
        for (MyVertex v1 : currentGraph.getVertices()) {
            // Save v1 in collection to check, it has already been created to avoid redundancies in loop below?
            ViewVertex vv = new ViewVertex(v1.getID(), 5, v1.getX(), v1.getY());
            view.addVertex(vv, observableList);
            viewVerticesSelection.put(v1.getID(), vv);
            makeDraggable(vv, innerObjects);
            makeSelectable(vv);
            Tooltip.install(vv, new Tooltip(vv.getID()));
        }
        // add view edges
        for (MyEdge edge : currentGraph.getEdges()) {
            ViewEdge ve = new ViewEdge(viewVerticesSelection.get(edge.getFirst().getID()), viewVerticesSelection.get(edge.getSecond().getID()));
            view.addEdge(ve, observableList);
            ve.toBack();
        }
    }

    private void makeDraggable(ViewVertex viewVertex, Group innerObjects) {
        viewVertex.setOnMouseDragged(event -> {
            double x = event.getSceneX();
            double y = event.getSceneY();
            Bounds bounds = innerObjects.localToScene(view.getInnerViewObjects().getBoundsInLocal());
            double minX = bounds.getMinX();
            double minY = bounds.getMinY();
            if (x < minX) {
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
        seleGraph = new UndirectedSparseGraph<>();
        view.removeAllFromInfoTable();
    }

    private void resetTab(){
        view.getInnerViewObjectsSele().getChildren().clear();

    }

    /*
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
    } */

    private void makeSelectable(ViewVertex viewVertex) {
        viewVertex.setOnMouseClicked(event -> {
            viewVertex.setSelected();
            updateSelectionGraph(viewVertex);
            view.getSelectionMenu().setDisable(false);
            view.setSelectionTextfield(seleGraph.getVertexCount(),seleGraph.getEdgeCount(),0);
        });
    }

    private void updateSelectionGraph(ViewVertex viewVertex){
        List<MyVertex> removeList = new ArrayList<>();
        for(MyVertex v : model.getGraph().getVertices()) {
            if (v.getID().equals(viewVertex.getID())) {
                if (!seleGraph.containsVertex(v)) {
                    System.out.println("addded test: " + viewVertex.getID());
                    seleGraph.addVertex(v);
                    countSelected += 1;
                    view.setselectionTextfield(String.valueOf(countSelected));
                    view.addToInfoTable(v);
                    for(MyEdge edge : this.model.getGraph().getInEdges(v)){
                        if (seleGraph.containsVertex(edge.getFirst()) && seleGraph.containsVertex(edge.getSecond())) {
                            seleGraph.addEdge(edge, edge.getVertices());
                            System.out.println("edge added");
                        }
                    }
                } else if (seleGraph.containsVertex(v)) {
                    System.out.println("deleted test: " + viewVertex.getID());
                    seleGraph.removeVertex(v);
                    countSelected -= 1;
                    view.setselectionTextfield(String.valueOf(countSelected));
                    view.removeFromInfoTable(v.getID());
                    for(MyEdge edge : this.model.getGraph().getInEdges(v)){
                        seleGraph.removeEdge(edge);
                    }
                }
            }
        removeList.add(v);
        //for(MyEdge edge : this.model.getGraph().getInEdges(v)){
        //  seleGraph.removeEdge(edge);
        //}
        }
    }



    private void changeLayoutParameters(Button button) {
        Task<Void> layoutApplyTask = new Task<Void>() {
            @Override
            protected Void call() {
                button.setDisable(true);
                model.setRepulsionMultiplier(view.getLayoutRepulsionMultiplierSpinnerValue());
                model.setAttractionMultiplier(view.getLayoutAttractionMultiplierSpinnerValue());
                model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), view.getOrderByContigLengthRadioButton().isSelected());
                return null;
            }
        };
        layoutApplyTask.setOnSucceeded(e ->{
            for (MyVertex mv : model.getGraph().getVertices()) {
                ViewVertex vv = viewVertices.get(mv.getID());
                vv.animate(mv.getX(), mv.getY());
            }
            button.setDisable(false);
        });
        Thread layoutApplyThread = new Thread(layoutApplyTask);
        layoutApplyThread.setDaemon(true);
        layoutApplyThread.start();
    }

    public void applyDragSelectRectangleFunctionality(){
        view.getScrollPane().addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                view.initSelectionRectangle(0,0);
                view.getSelectionRectangle().setStroke(Color.BLACK);
                view.getSelectionRectangle().setTranslateX(event.getX());
                view.getSelectionRectangle().setTranslateY(event.getY());
                view.getInnerViewObjects().getChildren().add(view.getSelectionRectangle());
            }
        });
        view.getScrollPane().addEventFilter(MouseEvent.MOUSE_DRAGGED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(view.getSelectionRectangle() != null){
                    view.getSelectionRectangle().widthProperty().set(event.getX() - view.getSelectionRectangle().getTranslateX());
                    view.getSelectionRectangle().heightProperty().set(event.getY() - view.getSelectionRectangle().getTranslateY());
                }
            }
        });
        view.getScrollPane().addEventFilter(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(view.getSelectionRectangle() != null){
                    for(ViewVertex vv : viewVertices.values()){
                        if(view.getSelectionRectangle().getBoundsInParent().intersects(vv.getBoundsInParent())) {
                            vv.setSelected();
                            updateSelectionGraph(vv);
                        }
                    }
                    view.getInnerViewObjects().getChildren().remove(view.getSelectionRectangle());
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
                        model.parseGFA(menuItem.getText());
                        view.getProgressIndicator().setVisible(false);
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), view.getOrderByContigLengthRadioButton().isSelected());
                        return null;
                    }
                };
                parseGraphTask.setOnSucceeded(e -> {
                    visualizeGraph(model.getGraph(), view.getInnerViewObjects().getChildren(), view.getInnerViewObjects());
                    view.getScrollPane().setDisable(false);
                    view.makeScrollPaneZoomable(view.getScrollPane());
                });

                Thread parseGraphThread = new Thread(parseGraphTask);
                parseGraphThread.setDaemon(true);
                parseGraphThread.start();
            }
        });
    }

    public void updateSelectionInformation() {
        view.removeAllFromInfoTable();
        for (MyVertex v : seleGraph.getVertices()){
            view.addToInfoTable(v);
        }
    }
}

