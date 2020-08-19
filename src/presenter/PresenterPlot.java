package presenter;

// Presenter for plot.fxml
// by Julia

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.Node;
import view.LegendItem;
import view.ViewPlot;
import view.ViewVertex;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class PresenterPlot {

    Model model;
    ViewPlot viewPlot;
    Map<Integer, String> taxIDRGBCode;
    Presenter presenter;
    private String coloringMethode = "default";

    public PresenterPlot(Model model, ViewPlot viewPlot, Tab tab,UndirectedSparseGraph<MyVertex,MyEdge> graph, Presenter presenter) throws IOException {
        this.model = model;
        this.viewPlot = viewPlot;
        this.presenter = presenter;
        plotCoverageGC(2.0, tab, graph, coloringMethode);
        plotContigLengthDistribution(graph);
        setUpBinding();
    }

    private void setUpBinding() {

        if (presenter.getGcContentReady()) {
            viewPlot.getColoringGCcontentRadioButton().setDisable(false);
        }
        if (presenter.getTaxonomyFileLoaded()) {
            viewPlot.getColoringTaxonomyRadioButton().setDisable(false);
        }
        if (presenter.getCoverageReadyBool()) {
            viewPlot.getColoringCoverageRadioButton().setDisable(false);
        }


        viewPlot.getNodeSizeManualSliderPlot().disableProperty().bind(viewPlot.getNodeSizeManualRadioButtonPlot().selectedProperty().not());

        viewPlot.getNodeSizeManualSliderPlot().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(Math.log(viewPlot.getNodeSizeManualSliderPlot().getValue()), viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getNodeSizeDefaultRadioButtonPlot().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getColoringTaxonomyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getColoringDefaultRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getColoringGCcontentRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getColoringCoverageRadioButton().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                coloringMethode = coloringDecision();
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        if (presenter.getSelectionGraph().getVertexCount() != 0) {

            viewPlot.getNodeSizeManualSliderPlot().disableProperty().bind(viewPlot.getNodeSizeManualRadioButtonPlot().selectedProperty().not());

            viewPlot.getNodeSizeManualSliderPlot().setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(Math.log(viewPlot.getNodeSizeManualSliderPlot().getValue()), viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(Math.log(viewPlot.getNodeSizeManualSliderPlot().getValue()), viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewPlot.getNodeSizeDefaultRadioButtonPlot().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(2.0, viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                        viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewPlot.getColoringTaxonomyRadioButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(2.0, viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                        viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewPlot.getColoringDefaultRadioButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(2.0, viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                        viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewPlot.getColoringGCcontentRadioButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(2.0, viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                        viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            viewPlot.getColoringCoverageRadioButton().setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    coloringMethode = coloringDecision();
                    try {
                        plotCoverageGC(2.0, viewPlot.getTabSelection(), presenter.getSelectionGraph(), coloringMethode);
                        plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph(), coloringMethode);
                        viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }

    // Method to plot Coverage and GC-content of the contigs in the graph
    public void plotCoverageGC(double circleSize, Tab tab, UndirectedSparseGraph<MyVertex,MyEdge> graph, String coloringMethode) throws IOException {
        double coverage;
        double gc;
        int taxID;
        int maxCoverage = 0;

        for (MyVertex v : graph.getVertices()) {
            coverage = (double) v.getProperty(ContigProperty.COVERAGE);
            if (coverage > maxCoverage) {
                maxCoverage = 100*(Math.round((int)coverage/100));
            }
        }

        NumberAxis yaxis = new NumberAxis(0.0, (int)maxCoverage+100, 200);
        NumberAxis xaxis = new NumberAxis(0.0, 1.0, 0.2);
        yaxis.setLabel("Coverage");
        xaxis.setLabel("GC content");

        ScatterChart<Number, Number> sChart = new ScatterChart<>(xaxis, yaxis);
        sChart.setTitle("Coverage - GC content plot");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();

        for (MyVertex v : graph.getVertices()) {
            coverage = (double) v.getProperty(ContigProperty.COVERAGE);
            gc = (double) v.getProperty(ContigProperty.GC);
            taxID = ((Node) v.getProperty(ContigProperty.TAXONOMY)).getId();
            String id = v.getID() + ">" + taxID;
            series.getData().add(new XYChart.Data<>(gc, coverage, id));
        }

        sChart.getData().add(series);
        viewPlot.setGcPlot(sChart, tab);

        sChart.setLegendVisible(false);

        if (coloringMethode.equals("default")) {
            for (XYChart.Series<Number, Number> s : sChart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+"\n"
                            + "x: " + String.format("%.3g%n",d.getXValue())
                            + "y: " + Math.round((Double) d.getYValue())));
                    d.getNode().setScaleY(circleSize);
                    d.getNode().setScaleX(circleSize);
                    String colorCode = "rgb(" + 67 + "," + 110 + "," + 238 + ");";
                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                }
            }
        }
        else if (coloringMethode.equals("tax")) {
            Map<Integer, String> taxIDRGBCode = presenter.getTaxIDRGBCode();
            for (XYChart.Series<Number, Number> s : sChart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+"\n"
                            + "x: " + String.format("%.3g%n",d.getXValue())
                            + "y: " + Math.round((Double) d.getYValue())));
                    d.getNode().setScaleY(circleSize);
                    d.getNode().setScaleX(circleSize);
                    String taxIDActualString = ((String) d.getExtraValue()).split(">")[1];

                    if (!taxIDActualString.equals("-100")) {
                        String rgb = taxIDRGBCode.get(Integer.parseInt(taxIDActualString));
                        String[] rgbCodes = rgb.split("t");
                        String colorCode = "rgb(" + rgbCodes[0] + "," + rgbCodes[1] + "," + rgbCodes[2] + ");";
                        d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                    }
                    else {
                        System.out.println(taxIDActualString);
                        String colorCode = "rgb(" + 0 + "," + 255 + "," + 0 + ");";
                        d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                    }
                }
            }
        }
        else if (coloringMethode.equals("gc")) {
            Map<Object, Double> gcContent = presenter.getGCContent();
            for (XYChart.Series<Number, Number> s : sChart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+"\n"
                            + "x: " + String.format("%.3g%n",d.getXValue())
                            + "y: " + Math.round((Double) d.getYValue())));
                    d.getNode().setScaleY(circleSize);
                    d.getNode().setScaleX(circleSize);
                    String vertexIDActualString = ((String) d.getExtraValue()).split(">")[0];

                    if (!vertexIDActualString.isEmpty()) {
                        for (Object i : gcContent.keySet()) {
                            if ((vertexIDActualString).equals(i)) {
                                if (gcContent.get(i) < 0.5) {
                                    String colorCode = "hsb(" + 120 + "," + ((1 - gcContent.get(i)) * 100) + "%," + ((0.49 + gcContent.get(i)) * 100) + "%);";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                                else if (gcContent.get(i) >= 0.5) {
                                    String colorCode = "hsb(" + 0 + "," + (gcContent.get(i) * 100) + "%," + (1 * 100) + "%);";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (coloringMethode.equals("cov")) {
            Map<Object, Double> coverageColor = presenter.getCoverageColor();

            for (XYChart.Series<Number, Number> s : sChart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+"\n"
                            + "x: " + String.format("%.3g%n",d.getXValue())
                            + "y: " + Math.round((Double) d.getYValue())));
                    d.getNode().setScaleY(circleSize);
                    d.getNode().setScaleX(circleSize);
                    String vertexIDActualString = ((String) d.getExtraValue()).split(">")[0];

                    if (!vertexIDActualString.isEmpty()) {
                        for (Object i : coverageColor.keySet()) {
                            if ((vertexIDActualString).equals(i)) {
                                if (coverageColor.get(i) < 0.5) {
                                    String colorCode = "hsb(" + 120 + "," + ((1 - coverageColor.get(i)) * 100) + "%," + ((0.49 + coverageColor.get(i)) * 100) + "%);";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                                else if (coverageColor.get(i) >= 0.5) {
                                    String colorCode = "hsb(" + 0 + "," + (coverageColor.get(i) * 100) + "%," + (1 * 100) + "%);";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public int getMaxLengthPlot(UndirectedSparseGraph<MyVertex,MyEdge> graph){
        int maxLength = 0;
        for(MyVertex v : graph.getVertices()){
            int curLength = (int)(double)v.getProperty(ContigProperty.LENGTH);
            if(curLength>maxLength){
                maxLength=curLength;
            }
        }
        return maxLength;
    }

    public void plotContigLengthDistribution(UndirectedSparseGraph<MyVertex,MyEdge> graph){
        int maxLength = getMaxLengthPlot(graph);
        int maxCounter = 0;

        int maxLegend = maxLength;
        System.out.println(maxLength);
        NumberAxis yaxis = new NumberAxis();
        CategoryAxis xaxis = new CategoryAxis();
        yaxis.setLabel("Number of Contigs");
        xaxis.setLabel("Percentage of Maximun Contig length: " + maxLegend + "bp");

        double iq0 = (double)maxLength * 0.1;
        int iq0Counter = 0;
        double iq1 = (double)maxLength * 0.2;
        int iq1Counter = 0;
        double iq2 = (double)maxLength * 0.3;
        int iq2Counter = 0;
        double iq3 = (double)maxLength * 0.4;
        int iq3Counter = 0;
        double iq4 = (double)maxLength * 0.5;
        int iq4Counter = 0;
        double iq5 = (double)maxLength * 0.6;
        int iq5Counter = 0;
        double iq6 = (double)maxLength * 0.7;
        int iq6Counter = 0;
        double iq7 = (double)maxLength * 0.8;
        int iq7Counter = 0;
        double iq8 = (double)maxLength * 0.9;
        int iq8Counter = 0;
        double iq9 = (double)maxLength * 1.0;
        int iq9Counter = 0;

        for(MyVertex v : graph.getVertices()){
            int curLength = (int)(double)v.getProperty(ContigProperty.LENGTH);
            if(curLength<iq0){
                iq0Counter +=1;
            }
            if(curLength<iq1 && curLength>iq0){
                iq1Counter +=1;
            }
            if(curLength<iq2 && curLength>iq1){
                iq2Counter +=1;
            }
            if(curLength<iq3 && curLength>iq2){
                iq3Counter +=1;
            }
            if(curLength<iq4 && curLength>iq3){
                iq4Counter +=1;
            }
            if(curLength<iq5 && curLength>iq4){
                iq5Counter +=1;
            }
            if(curLength<iq6 && curLength>iq5){
                iq6Counter +=1;
            }
            if(curLength<iq7 && curLength>iq6){
                iq7Counter +=1;
            }
            if(curLength<iq8 && curLength>iq0){
                iq8Counter +=1;
            }
            if(curLength<iq9 && curLength>iq8){
                iq9Counter +=1;
            }
        }

        if(maxCounter<iq0Counter){maxCounter=iq0Counter;}
        if(maxCounter<iq1Counter){maxCounter=iq1Counter;}
        if(maxCounter<iq2Counter){maxCounter=iq2Counter;}
        if(maxCounter<iq3Counter){maxCounter=iq3Counter;}
        if(maxCounter<iq4Counter){maxCounter=iq4Counter;}
        if(maxCounter<iq5Counter){maxCounter=iq5Counter;}
        if(maxCounter<iq6Counter){maxCounter=iq6Counter;}
        if(maxCounter<iq7Counter){maxCounter=iq7Counter;}
        if(maxCounter<iq8Counter){maxCounter=iq8Counter;}
        if(maxCounter<iq9Counter){maxCounter=iq9Counter;}

        BarChart<String, Number> bc = new BarChart<String, Number>(xaxis, yaxis);
        bc.setTitle("Distribution of Contig Lengths");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<String, Number>("10%", iq0Counter));
        series.getData().add(new XYChart.Data<String, Number>("20%", iq1Counter));
        series.getData().add(new XYChart.Data<String, Number>("30%", iq2Counter));
        series.getData().add(new XYChart.Data<String, Number>("40%", iq3Counter));
        series.getData().add(new XYChart.Data<String, Number>("50%", iq4Counter));
        series.getData().add(new XYChart.Data<String, Number>("60%", iq5Counter));
        series.getData().add(new XYChart.Data<String, Number>("70%", iq6Counter));
        series.getData().add(new XYChart.Data<String, Number>("80%", iq7Counter));
        series.getData().add(new XYChart.Data<String, Number>("90%", iq8Counter));
        series.getData().add(new XYChart.Data<String, Number>("100%", iq9Counter));

        bc.getData().add(series);

        viewPlot.setCDPlot(bc, viewPlot.getTabContigLengthDistribution());
    }

    public String coloringDecision () {
        if (viewPlot.getColoringTaxonomyRadioButton().isSelected()) {
            coloringMethode = "tax";
        }
        else if (viewPlot.getColoringDefaultRadioButton().isSelected()) {
            coloringMethode = "default";
        }
        else if (viewPlot.getColoringGCcontentRadioButton().isSelected()) {
            coloringMethode = "gc";
        }
        else if (viewPlot.getColoringCoverageRadioButton().isSelected()) {
            coloringMethode = "cov";
        }

//        System.out.println(coloringMethode);

        return coloringMethode;
    }
}
