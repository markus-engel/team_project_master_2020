// combines view & model
package presenter;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.*;
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
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Presenter {
    Model model;
    View view;
    Presenter self;
    private Map<Integer, String> taxIDRGBCode;
    private Map<Object, Double> gcContent;
    Map<String, String> colorIndividualRank;
    private Map<Object, Double> coverageColor;
    Map<String, List<String>> contigsOrderedByChosenRank;
    Map<String, ViewVertex> viewVertices = new HashMap<>();  //Hashmap of view vertex objects
    Map<String, ViewVertex> viewVerticesSelection = new HashMap<>();  //Hashmap of view vertex objects
    public final Dimension MAX_WINDOW_DIMENSION = new Dimension(775, 500); //gets passed to model to center layouts, gets passed to view to control size of window
    private UndirectedSparseGraph<MyVertex,MyEdge> seleGraph = new UndirectedSparseGraph<>();
    IntegerProperty countSelected = new SimpleIntegerProperty();
    UndirectedSparseGraph<MyVertex, MyEdge> currentGraph;
    Map<String, ViewVertex> currentViewVertices;
    Boolean rankBool = false, taxonomyBool = false, gcBool = false, coverageBool = false;
    Map<String, Object> menuSettingsMain = new HashMap<>(); //hashMap holding colourGroup, OrderGroup, NodeGroup
    private Boolean taxonomyFileLoaded = false, gcContentReady = false, coverageReadyBool = false;

    public Presenter(Model model, View view) {
        this.model = model;
        this.view = view;
        this.self = this;
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
                        view.getSelectionMenu().setDisable(false);
                        view.setSequenceCountTextField(model.getGraph().getVertexCount());
                        view.setOverlapCountTextField(model.getGraph().getEdgeCount());
                        view.getColoringGCcontentRadioButton().setDisable(false);
                        MenuItem recentFile = new MenuItem(f.getAbsolutePath());
                        gcContent = model.heatmapColorsGCContent();
                        gcContentReady = true;
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
                    for (ViewVertex vv : viewVertices.values()) Tooltip.install(vv, new Tooltip(createTooltip(vv)));
                    view.setTaxaCountTextField("Taxa: " + model.getTaxaCount());
                    view.getColoringTaxonomyRadioButton().setDisable(false);
//                    view.getColoringTaxonomyChoiceBox().setDisable(false);
                    view.getColoringRankRadioButton().setDisable(false);
                    view.getColoringTransparencyRadioButton().setDisable(false);
                    updateSelectionInformation();
                    taxonomyFileLoaded = true;

                    //calculate colour once for tax and rank
                    taxIDRGBCode = model.createColor(model.getTaxaID());

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
                    for (ViewVertex vv : viewVertices.values()) Tooltip.install(vv, new Tooltip(createTooltip(vv)));
                    view.getCoverageGCMenu().setDisable(false);
                    view.getNodeSizeCoverageRadioButton().setDisable(false);
                    view.getColoringCoverageRadioButton().setDisable(false);
                    view.getColoringTransparencyRadioButton().setDisable(false);
                    coverageColor = model.heatmapColorsCovarge();
                    coverageReadyBool = true;
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
                    PresenterPlot presenterPlot = new PresenterPlot(model, viewplot, viewplot.getTabGcCoverage(), model.getGraph(), self);
                    PresenterPlot presenterPlotSele = new PresenterPlot(model, viewplot, viewplot.getTabSelection(), seleGraph, self);
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

        view.getTabSelection().setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {

                resetTab();
                if (view.getTabSelection().isSelected()) {
                    saveMenuSettingsMain();
                    System.out.println("Selection tab recognized");
                    model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), seleGraph, view.getOrderByContigLengthRadioButton().isSelected());
                    visualizeSelectionGraph(seleGraph, view.getInnerViewObjectsSele().getChildren(), view.getInnerViewObjectsSele());
                    view.getScrollPaneSele().setDisable(false);
                    view.makeScrollPaneZoomable(view.getScrollPaneSele());
                }
            }
        });

        view.getTabMain().setOnSelectionChanged(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (view.getTabMain().isSelected()) {
                    setMenuSettingsMain();
                }
            }
        });


        view.getColoringTaxonomyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();

                if (view.getColoringTaxonomyRadioButton().isSelected() ) {
                    taxonomyBool = true;
                    gcBool = false; coverageBool = false; rankBool = false;
                }

                view.getLegendItems().clear();

                for (MyVertex v : currentGraph.getVertices()) {
                    Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
                    if (taxIDRGBCode.keySet().contains(taxNode.getId())) {
                        String rgb = taxIDRGBCode.get(taxNode.getId());
                        String[] rgbCodes = rgb.split("t");
                         currentViewVertices.get(v.getID()).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2])));

                        //updating legend
                        LegendItem legendItem = new LegendItem(new Circle(5, Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]))), taxNode.getScientificName());
                        if (!view.getLegendItems().contains(legendItem)) {
                            view.getLegendItems().add(legendItem);
                        }
                    } else if (taxNode.getId() == -100) {
                        currentViewVertices.get(v.getID()).setColour(Color.rgb(0, 255, 0));
                        LegendItem legendItem = new LegendItem(new Circle(5,Color.rgb(0, 255, 0)), "not available");
                        if (!view.getLegendItems().contains(legendItem)){
                            view.getLegendItems().add(legendItem);
                        }
                    }
                }
                view.updateLabelCol("Taxonomy");
                view.getShowLegendMenuItem().setDisable(false);
                if (view.getShowLegendMenuItem().isSelected()) {
                    view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                    view.getLabelCol().setPrefWidth(210);
                }
            }
        });

        view.getColoringRankRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (view.getColoringRankRadioButton().isSelected() ) {
                    rankBool = true;
                    gcBool = false; coverageBool = false; taxonomyBool = false;
                }
                ObservableList<String> rankNames = FXCollections.observableArrayList();

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
                if (view.getShowLegendMenuItem().isSelected()) {
                    view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                    view.getLabelCol().setPrefWidth(210);
                }
            }
        });

        view.getColoringRankChoiceBox().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                view.getLegendItems().clear();
                String chosenRank = view.getColoringRankChoiceBox().getValue();
                contigsOrderedByChosenRank = new HashMap<>();
                for (MyVertex v : currentGraph.getVertices()) {
                    Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
                    String ancestorNameOfChosenRank = taxNode.getAncestorName(chosenRank).toString();
                    if (ancestorNameOfChosenRank.equals("-1")) ancestorNameOfChosenRank = "not available";
                    if (contigsOrderedByChosenRank.containsKey(ancestorNameOfChosenRank)) {
                        contigsOrderedByChosenRank.get(ancestorNameOfChosenRank).add(v.getID());
                    }
                    else {
                        contigsOrderedByChosenRank.put(ancestorNameOfChosenRank, new LinkedList<>());
                        contigsOrderedByChosenRank.get(ancestorNameOfChosenRank).add(v.getID());
                    }
                }
                colorIndividualRank = model.createColorRank(contigsOrderedByChosenRank.keySet());

                //reset colours to default
                for (MyVertex v : currentGraph.getVertices()) {
                    currentViewVertices.get(v.getID()).setColour(Color.rgb(67, 110, 238));
                }
                for (String group : contigsOrderedByChosenRank.keySet()) {
                    String rgbCodeTotal = colorIndividualRank.get(group);
                    String[] rgbCodes = rgbCodeTotal.split("t");
                    LegendItem legendItem = new LegendItem(new Circle(5, Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]))), group);
                    if (!view.getLegendItems().contains(legendItem)) {
                        view.getLegendItems().add(legendItem);
                    }
                    for (String contigID : contigsOrderedByChosenRank.get(group)) {
                        currentViewVertices.get(contigID).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2])));
                    }
                }
                view.updateLabelCol("Taxonomy: " + chosenRank);
                view.getShowLegendMenuItem().setDisable(false);
                if (view.getShowLegendMenuItem().isSelected()) {
                    view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                    view.getLabelCol().setPrefWidth(210);
                }
            }
        });

        view.getColoringDefaultRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                //Updates Legend on the side.
                view.getLegendItems().clear();
                view.getLegendTableView().setPrefWidth(0);
                view.getShowLegendMenuItem().setSelected(false);
                for (MyVertex v : currentGraph.getVertices()) {
                    currentViewVertices.get(v.getID()).setColour(Color.rgb(67, 110, 238));
                }
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
                determineCurrentTab();

                if (rankBool) {
                    view.getLegendItems().clear();
                    for (MyVertex v : currentGraph.getVertices()) {
                        currentViewVertices.get(v.getID()).setColour(Color.rgb(67, 110, 238));
                    }
                    for (String group : contigsOrderedByChosenRank.keySet()) {
                        String rgbCodeTotal = colorIndividualRank.get(group);
                        String[] rgbCodes = rgbCodeTotal.split("t");
                        LegendItem legendItem = new LegendItem(new Circle(5, Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue())), group);
                        if (!view.getLegendItems().contains(legendItem)) {
                            view.getLegendItems().add(legendItem);
                        }
                        for (String contigID : contigsOrderedByChosenRank.get(group)) {
                            currentViewVertices.get(contigID).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue()));
                        }
                    }
                    view.getShowLegendMenuItem().setDisable(false);
                    if (view.getShowLegendMenuItem().isSelected()) {
                        view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                        view.getLabelCol().setPrefWidth(210);
                    }
                }
                else if (taxonomyBool) {
                    view.getLegendItems().clear();
                    for (MyVertex v : currentGraph.getVertices()) {
                        Node taxNode = (Node) v.getProperty(ContigProperty.TAXONOMY);
                        if (taxIDRGBCode.keySet().contains(taxNode.getId())) {
                            String rgb = taxIDRGBCode.get(taxNode.getId());
                            String[] rgbCodes = rgb.split("t");
                            currentViewVertices.get(v.getID()).setColour(Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue()));

                            //updating legend
                            LegendItem legendItem = new LegendItem(new Circle(5, Color.rgb(Integer.parseInt(rgbCodes[0]), Integer.parseInt(rgbCodes[1]), Integer.parseInt(rgbCodes[2]), view.getColoringTransparencySlider().getValue())), taxNode.getScientificName());
                            if (!view.getLegendItems().contains(legendItem)) {
                                view.getLegendItems().add(legendItem);
                            }
                        } else if (taxNode.getId() == -100) {
                            currentViewVertices.get(v.getID()).setColour(Color.rgb(0, 255, 0, view.getColoringTransparencySlider().getValue()));
                            LegendItem legendItem = new LegendItem(new Circle(5,Color.rgb(0, 255, 0, view.getColoringTransparencySlider().getValue())), "not available");
                            if (!view.getLegendItems().contains(legendItem)){
                                view.getLegendItems().add(legendItem);
                            }
                        }
                    }
                    view.getShowLegendMenuItem().setDisable(false);
                    if (view.getShowLegendMenuItem().isSelected()) {
                        view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                        view.getLabelCol().setPrefWidth(210);
                    }
                }
                else if (coverageBool) {
                    view.getLegendItems().clear();
                    for (ViewVertex v : currentViewVertices.values()) {
                        for (Object j : coverageColor.keySet()) {
                            if (v.getID().equals(j)) {
                                if (coverageColor.get(j) < 0.5) {
                                    v.setColour(Color.hsb(120, 1 - coverageColor.get(j), 0.49 + coverageColor.get(j), view.getColoringTransparencySlider().getValue()));
//                                    v.setColour(Color.hsb);
                                } else if (coverageColor.get(j) >= 0.5) {
                                    v.setColour(Color.hsb(0, coverageColor.get(j), 1, view.getColoringTransparencySlider().getValue()));
                                }
                            }
                        }
                    }
                    createLegendCoverage(view.getColoringTransparencySlider().getValue());
                }
                else if (gcBool) {
                    view.getLegendItems().clear();
                    for (MyVertex v : currentGraph.getVertices()) {
                        for (Object i : gcContent.keySet()) {
                            if (v.getID().equals(i)) {
                                if (gcContent.get(i) < 0.5) {
                                    currentViewVertices.get(v.getID()).setColour(Color.hsb(120, 1 - gcContent.get(i), 0.49 + gcContent.get(i), view.getColoringTransparencySlider().getValue()));
                                } else if (gcContent.get(i) >= 0.5) {
                                    currentViewVertices.get(v.getID()).setColour(Color.hsb(0, gcContent.get(i), 1, view.getColoringTransparencySlider().getValue()));
                                }
                            }
                        }
                    }
                    createLegendGCcontent(view.getColoringTransparencySlider().getValue());
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
                resetSelection();
            }
        });

        view.getSearchField().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(model.getGraph() == null){
                    view.getSearchField().setText("");
                } else {
                    String input = view.getSearchField().getText();
                    for(MyVertex mv : model.getGraph().getVertices()){
                        if(input.equals(mv.getID())){
                            ViewVertex vv = viewVertices.get(mv.getID());
                            vv.setSelected();
                            updateSelectionGraph(vv);
                            break;
                        }
                        if (input.equals(model.getScientificTaxNames().get(mv.getProperty(ContigProperty.TAXONOMY)))) {
                            ViewVertex vv = viewVertices.get(mv.getID());
                            vv.setSelected();
                            updateSelectionGraph(vv);
                            break;
                        }
                    }
                }
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
                determineCurrentTab();
                view.getNodeSizeGroup().selectToggle(view.getNodeSizeDefaultRadioButton());
                if (!currentViewVertices.isEmpty()) for (ViewVertex vv : currentViewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });

        view.getNodeSizeCoverageRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) {
                    double lowestCoverage = model.getLowestCoverage();
                    double highestCoverage = model.getHighestCoverage();
                    double range = highestCoverage - lowestCoverage;
                    for (MyVertex v : model.getGraph().getVertices()) {
                        ViewVertex vv = currentViewVertices.get(v.getID());
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
                if (view.getColoringCoverageRadioButton().isSelected() ) {
                    coverageBool = true;
                    gcBool = false; taxonomyBool = false; rankBool = false;
                }
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) {
                    view.getLegendItems().clear();
                    for (ViewVertex v : currentViewVertices.values()) {
                        for (Object j : coverageColor.keySet()) {
                            if (v.getID().equals(j)) {
                                if (coverageColor.get(j) < 0.5) {
                                    v.setColour(Color.hsb(120, 1 - coverageColor.get(j), 0.49 + coverageColor.get(j)));
                                } else if (coverageColor.get(j) >= 0.5) {
                                    v.setColour(Color.hsb(0, coverageColor.get(j), 1));
                                }
                            }
                        }
                    }
                    createLegendCoverage(1.0);
                }
            }
        });

        view.getColoringGCcontentRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (view.getColoringGCcontentRadioButton().isSelected() ) {
                    gcBool = true;
                    taxonomyBool = false; coverageBool = false; rankBool = false;
                }
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) {
                    view.getLegendItems().clear();
                    for (MyVertex v : currentGraph.getVertices()) {
                        for (Object i : gcContent.keySet()) {
                            if (v.getID().equals(i)) {
                                if (gcContent.get(i) < 0.5) {
                                    currentViewVertices.get(v.getID()).setColour(Color.hsb(120, 1 - gcContent.get(i), 0.49 + gcContent.get(i)));
                                } else if (gcContent.get(i) >= 0.5) {
                                    currentViewVertices.get(v.getID()).setColour(Color.hsb(0, gcContent.get(i), 1));
                                }
                            }
                        }
                    }
                    createLegendGCcontent(1.0);
                }
            }
        });

        view.getNodeSizeContigLengthRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) {
                    double smallestContigLength = model.getSmallestContigLength();
                    double largestContigLength = model.getLargestContigLength();
                    double range = largestContigLength - smallestContigLength;
                    for (MyVertex v : currentGraph.getVertices()) {
                        ViewVertex vv = currentViewVertices.get(v.getID());
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
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) for (ViewVertex vv : currentViewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });

        view.getNodeSizeManualSlider().disableProperty().bind(view.getNodeSizeManualRadioButton().selectedProperty().not());
        view.getNodeSizeManualRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                if (!currentViewVertices.isEmpty()) for (ViewVertex vv : currentViewVertices.values()) {
                    vv.setSize(5);
                }
                view.getNodeSizeManualSlider().setValue(5);
            }
        });
        view.getNodeSizeManualSlider().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                determineCurrentTab();
                if (!currentViewVertices.isEmpty() & view.getNodeSizeScaleChoiceBox().getValue().equals("linear scale"))
                    for (ViewVertex vv : currentViewVertices.values()) {
                        vv.setSize(view.getNodeSizeManualSlider().getValue());
                    }
                else if (!currentViewVertices.isEmpty() & view.getNodeSizeScaleChoiceBox().getValue().equals("logarithmic scale"))
                    for (ViewVertex vv : currentViewVertices.values()) {
                        vv.setSize(Math.log(view.getNodeSizeManualSlider().getValue()));
                    }
            }
        });

        view.getOrderByNodeNumbersRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                determineCurrentTab();
                Task<Void> layoutApplyTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        view.getProgressIndicator().setVisible(true);
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), currentGraph, false);
                        return null;
                    }
                };
                layoutApplyTask.setOnSucceeded(e -> {
                    view.getProgressIndicator().setVisible(false);
                    for (MyVertex mv : currentGraph.getVertices()) {
                        ViewVertex vv = currentViewVertices.get(mv.getID());
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
                determineCurrentTab();
                Task<Void> layoutApplyTask = new Task<Void>() {
                    @Override
                    protected Void call() {
                        view.getProgressIndicator().setVisible(true);
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), currentGraph, true);
                        return null;
                    }
                };
                layoutApplyTask.setOnSucceeded(e -> {
                    view.getProgressIndicator().setVisible(false);
                    for (MyVertex mv : currentGraph.getVertices()) {
                        ViewVertex vv = currentViewVertices.get(mv.getID());
                        vv.animate(mv.getX(), mv.getY());
                    }
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
                    if (view.getColoringTaxonomyRadioButton().isSelected() | view.getColoringRankRadioButton().isSelected()) {
                        view.getLegendTableView().setPrefWidth(view.getLegendTableView().getMaxWidth());
                        view.getLabelCol().setPrefWidth(210);
                    }
                    else {
                        view.getLegendTableView().setPrefWidth(120);
                        view.getLabelCol().setPrefWidth(100);
                    }
                } else {
                    view.getLegendTableView().setPrefWidth(0);
                }
            }
        });

        countSelected.bind(view.getSelectedContigs().sizeProperty());
    }

    private void determineCurrentTab() {
        //toggle between selectionTab and MainTab
        if (view.getTabSelection().isSelected()) {
            currentGraph = seleGraph;
            currentViewVertices = viewVerticesSelection;
        } else {
            currentGraph = model.getGraph();
            currentViewVertices = viewVertices;
        }
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
            Tooltip.install(vv, new Tooltip(createTooltip(vv)));
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
            ViewVertex vv = new ViewVertex(v1.getID(), 5, v1.getX(), v1.getY(), viewVertices.get(v1.getID()));
            view.addVertex(vv, observableList);
            viewVerticesSelection.put(v1.getID(), vv);
            makeDraggable(vv, innerObjects);
            makeSelectable(vv);
            Tooltip.install(vv, new Tooltip(createTooltip(vv)));
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
            view.getInnerViewObjects().getChildren().remove(view.getSelectionRectangle());
            view.removeSelectionRectangle();
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
        view.initNewScrollPane();
        view.removeAllFromInfoTable();
        view.getShowLegendMenuItem().setDisable(true);
        view.getLegendTableView().setPrefWidth(0);
        view.getLegendItems().clear();
        view.getImportTaxonomyMenuItem().setDisable(true);
        view.getImportCoverageMenuItem().setDisable(true);
        view.getNodeSizeDefaultRadioButton().setSelected(true);
        view.getNodeSizeCoverageRadioButton().setDisable(true);
        view.getColoringDefaultRadioButton().setSelected(true);
        view.getColoringCoverageRadioButton().setDisable(true);
        view.getColoringTaxonomyRadioButton().setDisable(true);
        view.getColoringRankRadioButton().setDisable(true);
        view.getOrderByNodeNumbersRadioButton().setSelected(true);
    }

    private void resetTab(){
        view.getInnerViewObjectsSele().getChildren().clear();
    }

    private void resetSelection (){
        for (ViewVertex vv : viewVertices.values()) {
            if (vv.isSelected()) {
                vv.setSelected();
            }
        }
        seleGraph = new UndirectedSparseGraph<>();
        viewVerticesSelection = new HashMap<>();
        updateSelectionInformation();
        view.setSelectionTextField(countSelected.intValue());
        resetTab();
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
            view.setSelectionTextField(countSelected.intValue());
        });
    }

    private void updateSelectionGraph(ViewVertex viewVertex){
        List<MyVertex> removeList = new ArrayList<>();
        for(MyVertex v : model.getGraph().getVertices()) {
            if (v.getID().equals(viewVertex.getID())) {
                if (!seleGraph.containsVertex(v)) {
                    System.out.println("addded test: " + viewVertex.getID());
                    seleGraph.addVertex(v);
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
        view.setSelectionTextField(countSelected.intValue());
    }



    private void changeLayoutParameters(Button button) {
        Task<Void> layoutApplyTask = new Task<Void>() {
            @Override
            protected Void call() {
                view.getProgressIndicator().setVisible(true);
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
            view.getProgressIndicator().setVisible(false);
        });
        Thread layoutApplyThread = new Thread(layoutApplyTask);
        layoutApplyThread.setDaemon(true);
        layoutApplyThread.start();
    }

    public void applyDragSelectRectangleFunctionality(){
        view.getScrollPane().addEventFilter(MouseEvent.MOUSE_PRESSED,new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                resetSelection();
                view.initSelectionRectangle(0,0);
                view.getSelectionRectangle().setStroke(Color.BLACK);
                view.getSelectionRectangle().setTranslateX(event.getX()/ view.getScaleProperty());
                view.getSelectionRectangle().setTranslateY(event.getY()/ view.getScaleProperty());
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
                    view.removeSelectionRectangle();
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
                        model.applyLayout(new Dimension(MAX_WINDOW_DIMENSION.width, MAX_WINDOW_DIMENSION.height), model.getGraph(), view.getOrderByContigLengthRadioButton().isSelected());
                        return null;
                    }
                };
                parseGraphTask.setOnSucceeded(e -> {
                    visualizeGraph(model.getGraph(), view.getInnerViewObjects().getChildren(), view.getInnerViewObjects());
                    view.getScrollPane().setDisable(false);
                    view.makeScrollPaneZoomable(view.getScrollPane());
                    view.getProgressIndicator().setVisible(false);
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

    public double calcCovFromPercentage(double percentRelativeCoverage) {
        return model.getLowestCoverage() + (percentRelativeCoverage * (model.getHighestCoverage() - model.getLowestCoverage()));
    }

    public void createLegendGCcontent(Double transparencyValue) {
        view.getShowLegendMenuItem().setDisable(false);
        view.updateLabelCol("GC content");
        LegendItem legendItem0 = new LegendItem(new Circle(5,Color.hsb(120, 1, 0.49 + 0, transparencyValue)), "0.00");
        LegendItem legendItem25 = new LegendItem(new Circle(5,Color.hsb(120, 1 - 0.25, 0.49 + 0.25, transparencyValue)), "0.25");
        LegendItem legendItem40 = new LegendItem(new Circle(5,Color.hsb(120, 1 - 0.4, 0.49 + 0.4, transparencyValue)), "0.40");
        LegendItem legendItem50 = new LegendItem(new Circle(5,Color.hsb(0, 0.5, 1, transparencyValue)), "0.50");
        LegendItem legendItem60 = new LegendItem(new Circle(5,Color.hsb(0, 0.6, 1, transparencyValue)), "0.60");
        LegendItem legendItem75 = new LegendItem(new Circle(5,Color.hsb(0, 0.75, 1, transparencyValue)), "0.75");
        LegendItem legendItem100 = new LegendItem(new Circle(5,Color.hsb(0, 1, 1, transparencyValue)), "1.00");
        addToLegendAndAdjust(legendItem0, legendItem25, legendItem40, legendItem50, legendItem60, legendItem75, legendItem100);
    }

    public void createLegendCoverage(Double transparencyValue){
        view.getShowLegendMenuItem().setDisable(false);
        view.updateLabelCol("Coverage");
        LegendItem legendItem0 = new LegendItem(new Circle(5, Color.hsb(120, 1, 0.49 + 0, transparencyValue)), Integer.toString((int) model.getLowestCoverage()) + " (minimum)");
        LegendItem legendItem25 = new LegendItem(new Circle(5, Color.hsb(120, 1 - 0.25, 0.49 + 0.25, transparencyValue)), Integer.toString((int) calcCovFromPercentage(0.25)));
        LegendItem legendItem50 = new LegendItem(new Circle(5, Color.hsb(0, 0.5, 1, transparencyValue)), Integer.toString((int) calcCovFromPercentage(0.5)));
        LegendItem legendItem75 = new LegendItem(new Circle(5, Color.hsb(0, 0.75, 1, transparencyValue)), Integer.toString((int) calcCovFromPercentage(0.75)));
        LegendItem legendItem100 = new LegendItem(new Circle(5, Color.hsb(0, 1, 1, transparencyValue)), Integer.toString((int) model.getHighestCoverage()) + " (maximum)");
        addToLegendAndAdjust(legendItem0,legendItem25,legendItem50,legendItem75,legendItem100);
    }

    private void addToLegendAndAdjust(LegendItem... legendItems) {
        for(LegendItem li : legendItems) {
            view.getLegendItems().add(li);
        }
        view.getShowLegendMenuItem().setDisable(false);
        if (view.getShowLegendMenuItem().isSelected()) {
            view.getLegendTableView().setPrefWidth(120);
            view.getLabelCol().setPrefWidth(100);
        }
    }

    private void saveMenuSettingsMain(){
        menuSettingsMain.put("ColorGroup", view.getColoringGroup().getSelectedToggle());
        menuSettingsMain.put("OrderGroup", view.getOrderGroup().getSelectedToggle());
        menuSettingsMain.put("NodeSizeGroup", view.getNodeSizeGroup().getSelectedToggle());


        ObservableList<LegendItem> legendItems= FXCollections.observableArrayList();
        for (LegendItem legendItem: view.getLegendItems()){
            legendItems.add(new LegendItem(new Circle(legendItem.getCircle().getRadius(),legendItem.getCircle().getFill()), legendItem.getLabel()));
        }
        menuSettingsMain.put("LegendItems",legendItems);

        menuSettingsMain.put("RankChoiceBox", view.getColoringRankChoiceBox().getValue());
        menuSettingsMain.put("TransparencySlider", view.getColoringTransparencySlider().getValue());

        menuSettingsMain.put("NodeSizeScaleChoiceBox", view.getNodeSizeScaleChoiceBox().getValue());
        menuSettingsMain.put("NodeSizeSlider", view.getNodeSizeManualSlider().getValue());

        //having trouble with repulsion and attraciton spinner..
        //menuSettingsMain.put("RepulsionMultiplier", view.getLayoutRepulsionMultiplierSpinner().getValueFactory());
        //menuSettingsMain.put("AttractionMultiplier", view.getLayoutAttractionMultiplierSpinnerValue());


    }

    private void setMenuSettingsMain() {
        view.getColoringGroup().selectToggle((Toggle) menuSettingsMain.get("ColorGroup"));
        view.getOrderGroup().selectToggle((Toggle) menuSettingsMain.get("OrderGroup"));
        view.getNodeSizeGroup().selectToggle((Toggle) menuSettingsMain.get("NodeSizeGroup"));

        view.getLegendItems().clear();
        view.getLegendItems().addAll((ObservableList<LegendItem>) menuSettingsMain.get("LegendItems"));

        view.getColoringRankChoiceBox().setValue((String) menuSettingsMain.get("RankChoiceBox"));
        view.getColoringTransparencySlider().setValue((double) menuSettingsMain.get("TransparencySlider"));

        view.getNodeSizeScaleChoiceBox().setValue((String) menuSettingsMain.get("NodeSizeScaleChoiceBox"));
        view.getNodeSizeManualSlider().setValue((double) menuSettingsMain.get("NodeSizeSlider"));

        //having trouble with repulsion/attraction spinner
        //view.getLayoutRepulsionMultiplierSpinner().setValueFactory((SpinnerValueFactory)menuSettingsMain.get("RepulsionMultiplier"));
        //view.getLayoutAttractionMultiplierSpinner().setValueFactory((SpinnerValueFactory)menuSettingsMain.get("AttractionMultiplier"));
    }
    public Map<Integer, String> getTaxIDRGBCode() {
        return taxIDRGBCode;
    }

    public Map<Object, Double> getGCContent () {
        return gcContent;
    }

    public Map<Object, Double> getCoverageColor () {
        return coverageColor;
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getSelectionGraph () {
        return seleGraph;
    }

    public Boolean getTaxonomyFileLoaded () {
        return taxonomyFileLoaded;
    }

    public Boolean getGcContentReady () {
        return gcContentReady;
    }

    public Boolean getCoverageReadyBool () {
        return coverageReadyBool;
    }


    public String createTooltip(ViewVertex viewVertex) {
        DecimalFormat df = new DecimalFormat("####0.000");
        for (MyVertex v : model.getGraph().getVertices()) {
            if(v.getID().equals(viewVertex.getID())) {
                String length = "\nLength: " + (int)Math.round((double)v.getProperty(ContigProperty.LENGTH)) + " bp";
                String GC = "\nGC-content: " + df.format((double)v.getProperty(ContigProperty.GC));
                String taxName = "";
                if (v.getProperty(ContigProperty.TAXONOMY) instanceof Node) {
                    taxName = "\n" + ((Node) v.getProperty(ContigProperty.TAXONOMY)).getScientificName();
                }
                String coverage = "";
                if (v.getProperty(ContigProperty.COVERAGE) != "no entry") {
                    coverage = "\nCoverage: " + df.format((double)v.getProperty(ContigProperty.COVERAGE));
                }
                return viewVertex.getID() + length + GC + coverage + taxName;
            }
        }
        return "";
    }
}

