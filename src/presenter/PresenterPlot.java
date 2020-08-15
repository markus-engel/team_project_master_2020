package presenter;

// Presenter for plot.fxml
// by Julia

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tooltip;
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

    public PresenterPlot(Model model, ViewPlot viewPlot) throws IOException {
        this.model = model;
        this.viewPlot = viewPlot;
        plotCoverageGC();
        setUpBinding();
    }

    private void setUpBinding() {

    }

    // Method to plot Coverage and GC-content of the contigs in the graph
    public void plotCoverageGC() throws IOException {
        double coverage;
        double gc;
        UndirectedSparseGraph<MyVertex, MyEdge> graph = model.getGraph();

        NumberAxis yaxis = new NumberAxis(0.0, 2200.0, 200);
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
        viewPlot.setGcPlot(sChart);

        for (XYChart.Series<Number, Number> s : sChart.getData()) {
            for (XYChart.Data<Number, Number> d : s.getData()) {
                Tooltip.install(d.getNode(), new Tooltip((String) d.getExtraValue()));
            }
        }
    }

    // TODO: Colours, Tooltips, selection options

}
