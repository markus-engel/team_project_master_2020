package model;

import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.util.VisRunner;
import edu.uci.ics.jung.algorithms.util.IterativeContext;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Model {

    private TaxonomyTree currentTaxTree;
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
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
    public void parseGraph(String path, Dimension dimension) throws IOException {
        this.graph = GraphParser.readFile(path);
        // Store connected components in a Set of Set of Vertices using the JUNG lib algorithm
        WeakComponentClusterer<MyVertex,MyEdge> weakComponentClusterer = new WeakComponentClusterer<>();
        Set<Set<MyVertex>> cluster = weakComponentClusterer.apply(graph);
        // The Comparator sorts the Set of Sets based on their size. In the SortedSet the sets with biggest size appear first
        Comparator<Set<MyVertex>> comparator = new Comparator<Set<MyVertex>>() {
            @Override
            public int compare(Set<MyVertex> o1, Set<MyVertex> o2) {
                if(o1.size() > o2.size()) {
                    return -1;
                }
                else { return 1;}
            }
        };
        SortedSet<Set<MyVertex>> sortedSet = new TreeSet<>(comparator);
        sortedSet.addAll(cluster);
        double shiftX = 0.0;
        double shiftY = 0.0;
        // Apply the layout onto every set of vertices and update coordinates.
        for(Set<MyVertex> set : sortedSet){
            if(set.size() > 1){
                UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = createAuxilliarGraph(set);
                // Calculate layout dimension for each set based on the set size
                int dimensionX = (int) ((double)dimension.width*((double)set.size()/(double)graph.getVertexCount()));
                int dimensionY = (int) ((double)dimension.height*((double)set.size()/(double)graph.getVertexCount()));
                Dimension setDimension = new Dimension(dimensionX,dimensionY);
                applyLayout(auxGraph,setDimension,shiftX,shiftY);
                shiftY += dimensionY + 10;
            } else {
                set.iterator().next().setX(shiftX);
                set.iterator().next().setY(shiftY);
                shiftY += 10;
            }
        }
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graph;
    }

    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph){ this.graph = graph;}

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graph, path, currentTaxTree);
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graph, path);
    }

    private UndirectedSparseGraph<MyVertex,MyEdge> createAuxilliarGraph(Set<MyVertex> vertices){
        UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = new UndirectedSparseGraph<>();
        for(MyVertex v: vertices){
            auxGraph.addVertex(v);
            for(MyEdge edge : this.graph.getInEdges(v)){
                auxGraph.addEdge(edge, edge.getVertices());
            }
        }
        return auxGraph;
    }

    private void applyLayout(UndirectedSparseGraph<MyVertex, MyEdge> graph, Dimension dimension, double shiftX, double shiftY) {
        FRLayout<MyVertex,MyEdge> layout = new FRLayout<>(graph);
        layout.initialize();
        layout.setSize(dimension);

        VisRunner relaxer = new VisRunner((IterativeContext) layout);
        relaxer.prerelax();
        relaxer.relax();
        relaxer.run();
        // Apply layout onto auxGraph
        for(MyVertex v : graph.getVertices()){
            v.setX(layout.getX(v) + shiftX);
            v.setY(layout.getY(v) + shiftY);
        }
    }
}
