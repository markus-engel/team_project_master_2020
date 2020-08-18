package presenter;

// Presenter for plot.fxml
// by Julia

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.*;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.Node;
import view.ViewPlot;
import view.ViewVertex;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

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
            String id = v.getID() + "-" + taxID;
            series.getData().add(new XYChart.Data<>(gc, coverage, id));
        }

        sChart.getData().add(series);
        viewPlot.setGcPlot(sChart, tab);

        if (coloringMethode.equals("default")) {
            for (XYChart.Series<Number, Number> s : sChart.getData()) {
                for (XYChart.Data<Number, Number> d : s.getData()) {
                    Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+"\n"
                            + "x: " + String.format("%.3g%n",d.getXValue())
                            + "y: " + Math.round((Double) d.getYValue())));
                    d.getNode().setScaleY(circleSize);
                    d.getNode().setScaleX(circleSize);
                    d.getNode().setStyle("-fx-background-color: #860061, orange;");
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
                    String taxIDActualString = ((String) d.getExtraValue()).split("-")[1];

                    if (!taxIDActualString.isEmpty()) {
                        String rgb = taxIDRGBCode.get(Integer.parseInt(taxIDActualString));
                        String[] rgbCodes = rgb.split("t");
                        String colorCode = "rgb(" + rgbCodes[0] + "," + rgbCodes[1] + "," + rgbCodes[2] + ");";
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
                    String vertexIDActualString = ((String) d.getExtraValue()).split("-")[0];

                    if (!vertexIDActualString.isEmpty()) {
                        for (Object i : gcContent.keySet()) {
                            if ((vertexIDActualString).equals(i)) {
                                if (gcContent.get(i) < 0.5) {
                                    Double C = gcContent.get(i) * (1 - gcContent.get(i));
                                    Double m = gcContent.get(i) - C;
                                    Double X = C * (1 - (Math.abs((120 / 60) % 2 - 1)));
                                    String colorCode = "rgb(" + ((X + m) * 255) + "," + ((C + m) * 255) + "," + ((0 + m) * 255) + ");";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                                else if (gcContent.get(i) >= 0.5) {
                                    Double C = (0.49 + gcContent.get(i)) * gcContent.get(i);
                                    Double m = (0.49 + gcContent.get(i)) - C;
                                    Double X = C * (1 - (Math.abs((120 / 60) % 2 - 1)));
                                    String colorCode = "rgb(" + ((C + m) * 255) + "," + ((X + m) * 255) + "," + ((0 + m) * 255) + ");";
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
                    String vertexIDActualString = ((String) d.getExtraValue()).split("-")[0];

                    if (!vertexIDActualString.isEmpty()) {
                        for (Object i : coverageColor.keySet()) {
                            if ((vertexIDActualString).equals(i)) {
                                if (coverageColor.get(i) < 0.5) {
                                    Double C = coverageColor.get(i) * (1 - coverageColor.get(i));
                                    Double m = coverageColor.get(i) - C;
                                    Double X = C * (1 - (Math.abs((120 / 60) % 2 - 1)));
                                    String colorCode = "rgb(" + ((X + m) * 255) + "," + ((C + m) * 255) + "," + ((0 + m) * 255) + ");";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                                else if (coverageColor.get(i) >= 0.5) {
                                    Double C = (0.49 + coverageColor.get(i)) * coverageColor.get(i);
                                    Double m = (0.49 + coverageColor.get(i)) - C;
                                    Double X = C * (1 - (Math.abs((120 / 60) % 2 - 1)));
                                    String colorCode = "rgb(" + ((C + m) * 255) + "," + ((X + m) * 255) + "," + ((0 + m) * 255) + ");";
                                    d.getNode().setStyle("-fx-background-color: #860061, " + colorCode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void plotContigLengthDistribution(UndirectedSparseGraph<MyVertex,MyEdge> graph){
        TreeMap<Integer, Integer> cls = new TreeMap<Integer, Integer>();
        for(MyVertex v : graph.getVertices()){
            int curLength = (int)(double)v.getProperty(ContigProperty.LENGTH);
            if(cls.containsKey(curLength)){
                cls.put(curLength, cls.get(curLength + 1));
            }
            else{
                cls.put(curLength, 1);
            }
        }

        NumberAxis yaxis = new NumberAxis(0.0, 5000, 200);
        CategoryAxis xaxis = new CategoryAxis();
        yaxis.setLabel("Number of Contigs");
        xaxis.setLabel("Length in bp");


        BarChart<String, Number> bc = new BarChart<String, Number>(xaxis, yaxis);
        bc.setTitle("Distribution of Contig Lengths");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for(Map.Entry<Integer,Integer> entry : cls.entrySet()){
            series.getData().add(new XYChart.Data<String, Number>(String.valueOf(entry.getKey()), entry.getValue()));
//            System.out.printf(String.valueOf(entry.getKey()) + "  " + entry.getValue() + " ");
        }

        //bc.getData().add(series);

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

        System.out.println(coloringMethode);

        return coloringMethode;
    }
}
