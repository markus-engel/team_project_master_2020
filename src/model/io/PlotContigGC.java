// Java FX Scatter Chart Application by Anna
package model.io;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.io.IOException;

public class PlotContigGC {

    public PlotContigGC(UndirectedSparseGraph<MyVertex, MyEdge> graph, Stage stage) throws IOException {
        plotContigGC(graph);
    }

    static public ScatterChart plotContigGC(UndirectedSparseGraph<MyVertex, MyEdge> graph) {
        double coverage = 0;
        double gc = 0;

        NumberAxis yaxis = new NumberAxis(0.0, 2000.0, 200);
        NumberAxis xaxis = new NumberAxis(0.0, 1.0, 0.2);
        yaxis.setLabel("Coverage");
        xaxis.setLabel("GC content");

        ScatterChart sChart = new ScatterChart(xaxis, yaxis);
        sChart.setTitle("Coverage-GC content plot");

        XYChart.Series series = new XYChart.Series();

        for (MyVertex v : graph.getVertices()) {
            coverage = (double) v.getProperty(ContigProperty.GC);
            gc = (double) v.getProperty(ContigProperty.GC);
            series.getData().add(new XYChart.Data(coverage, gc));
        }

        sChart.getData().add(series);
        /*Group root = new Group();
        root.getChildren().add(sChart);
        Scene scenePLot = new Scene(root,500,800);
        stage.setScene(scenePLot);
        stage.show();*/
        return sChart;
    }

}