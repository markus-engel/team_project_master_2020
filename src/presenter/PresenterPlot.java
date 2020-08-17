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
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import view.ViewPlot;
import view.ViewVertex;

import java.io.IOException;

public class PresenterPlot {

    Model model;
    ViewPlot viewPlot;

    public PresenterPlot(Model model, ViewPlot viewPlot, Tab tab,UndirectedSparseGraph<MyVertex,MyEdge> graph) throws IOException {
        this.model = model;
        this.viewPlot = viewPlot;
        plotCoverageGC(2.0, tab, graph);
        plotContigLengthDistribution();
        setUpBinding();
    }

    private void setUpBinding() {

        viewPlot.getNodeSizeManualSliderPlot().disableProperty().bind(viewPlot.getNodeSizeManualRadioButtonPlot().selectedProperty().not());

        viewPlot.getNodeSizeManualSliderPlot().setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    plotCoverageGC(Math.log(viewPlot.getNodeSizeManualSliderPlot().getValue()), viewPlot.getTabGcCoverage(), model.getGraph());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        viewPlot.getNodeSizeDefaultRadioButtonPlot().setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    plotCoverageGC(2.0, viewPlot.getTabGcCoverage(), model.getGraph());
                    viewPlot.getNodeSizeManualSliderPlot().setValue(5.0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Method to plot Coverage and GC-content of the contigs in the graph
    public void plotCoverageGC(double circleSize, Tab tab, UndirectedSparseGraph<MyVertex,MyEdge> graph) throws IOException {
        double coverage;
        double gc;
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
            series.getData().add(new XYChart.Data<>(gc, coverage, v.getID()));
        }

        sChart.getData().add(series);
        viewPlot.setGcPlot(sChart, tab);

        for (XYChart.Series<Number, Number> s : sChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip((String)d.getExtraValue()+":\n"+ String.format("%.4g%n",d.getXValue())
                        + " GC content, " + String.format("%.4g%n",d.getYValue()) +" coverage"));
                d.getNode().setScaleY(circleSize);
                d.getNode().setScaleX(circleSize);
                d.getNode().setStyle("-fx-background-color: #860061, orange;");
            }
        }
    }

    public void plotContigLengthDistribution(){
        NumberAxis yaxis = new NumberAxis(0.0, 5000, 200);
        CategoryAxis xaxis = new CategoryAxis();
        yaxis.setLabel("Number of Contigs");
        xaxis.setLabel("Length in bp");

        BarChart<String, Number> bc = new BarChart<String, Number>(xaxis, yaxis);

        viewPlot.setCDPlot(bc, viewPlot.getTabContigLengthDistribution());
    }
}
