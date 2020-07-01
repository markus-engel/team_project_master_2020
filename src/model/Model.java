package model;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.CoverageParser;
import model.io.GraphParser;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;

import java.io.IOException;

public class Model {

    TaxonomyTree currentTaxTree;

    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
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
        graph = new UndirectedSparseGraph<MyVertex, MyEdge>();

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
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graph;
    }

    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph) {
        this.graph = graph;
    }

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graph, path, currentTaxTree);
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graph, path);
    }
}
