package model;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.scene.chart.ScatterChart;
import javafx.stage.Stage;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.*;

import java.awt.*;
import java.io.IOException;

public class Model {

    private TaxonomyTree currentTaxTree;
    private FRLayout<MyVertex, MyEdge> layout;
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
    //private SimpleObjectProperty graph1 = new SimpleObjectProperty(graph);
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();


    public Model() throws IOException {
        // Instantiation of the currentTaxTree in a task to show the responsive GUI already while parsing the tree
        Task<Void> taskTaxonomyTree = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                currentTaxTree = new TaxonomyTree();
                System.out.println("Taxonomy tree is prepared");
                return null;
            }
        };

        Thread treeBackgroundTask = new Thread(taskTaxonomyTree);
        treeBackgroundTask.setDaemon(true);
        treeBackgroundTask.start();

        Task<Void> loadGraph = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                graph = new UndirectedSparseGraph<MyVertex, MyEdge>();
                System.out.println("Graph prepared");
                return null;
            }
        };
        Thread loadGraphTask = new Thread(loadGraph);
        loadGraphTask.setDaemon(true);
        loadGraphTask.start();

        // either new method listener or:
        // InvalidationListener listener = null;
        graphProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                graphProperty.setValue(graph);
            }
        });
    }

    // create needed objects of the IO classes to use them in presenter
    public void parseGraph(String path) throws IOException {
        this.graph = GraphParser.readFile(path);
        initializeLayout(graph, new Dimension(900, 550));
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graph;
    }

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graph, path, currentTaxTree);
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graph, path);
    }

    public Layout<MyVertex, MyEdge> getLayout() {
        return this.layout;
    }

    private void initializeLayout(UndirectedSparseGraph<MyVertex, MyEdge> graph, Dimension dimension) {
        this.layout = new FRLayout<>(graph);
        this.layout.initialize();
        this.layout.setSize(dimension);

        VisRunner relaxer = new VisRunner((IterativeContext) layout);
        relaxer.prerelax();
        relaxer.relax();
        relaxer.run();
    }
}
