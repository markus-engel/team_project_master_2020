package model;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.CoverageParser;
import model.io.GraphParser;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;

import java.awt.*;
import java.io.IOException;

public class Model {

    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
    private TaxonomyTree currentTaxTree;
    private FRLayout<MyVertex, MyEdge> layout;

    //private SimpleObjectProperty graph1 = new SimpleObjectProperty(graph);
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();



    public Model() throws IOException {
        // Instantiation of the currentTaxTree in a task to show the responsive GUI already while parsing the tree
        Task<Void> taskTaxonomyTree = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                currentTaxTree = new TaxonomyTree();
                System.out.println("Taxonomy tree is loaded");
                return null;
            }
        };

        new Thread(taskTaxonomyTree).start();

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
        GraphParser gp = new GraphParser();
        this.graph = gp.readFile(path);
        initializeLayout(graph);
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graph;
    }

    //TaxIdParser currentTaxIdParser = new TaxIdParser();
    //currentTaxTree.getRank(9); // as method call example


    private void initializeLayout(UndirectedSparseGraph<MyVertex, MyEdge> graph){
        this.layout = new FRLayout<>(graph);
        this.layout.initialize();
        this.layout.setSize(new Dimension(500, 300));

        for (int i=0; i<50;i++){
            this.layout.step();
        }
    }

    public Layout<MyVertex, MyEdge> getLayout() {
        return this.layout;
    }

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graph, path, currentTaxTree);
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graph, path);
    }
}
