package presenter;


// Presenter for Selection.fxml by Anna

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.concurrent.Task;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import view.ViewPlot;
import view.ViewSelection;
import view.ViewVertex;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class PresenterSelection {
    Model model;
    ViewSelection viewSelection;
    HashMap<String, ViewVertex> viewVerticesSele = new HashMap<>();  //Hashmap of view vertex objects


    public PresenterSelection(Model model, ViewSelection viewSelection) throws IOException {
        this.model = model;
        this.viewSelection = viewSelection;
        showSelectionGraph();
        setUpBinding();
    }

    private void setUpBinding() {


    }

    // show Selection Graph
    public void showSelectionGraph() throws IOException {

        //UndirectedSparseGraph<MyVertex, MyEdge> graph = model.getGraphSele();
    }
}
