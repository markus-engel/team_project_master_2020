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
import view.ViewVertex;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Model {

    private TaxonomyTree currentTaxTree; //TODO: what's not current? :) (Caner)
    private UndirectedSparseGraph<MyVertex, MyEdge> graph; //TODO: do we need this? it's already in graphProperty (Caner)
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();
    TreeSet<Integer> taxons = new TreeSet(); //TODO: define the type <T> (Caner)

    public Model() throws IOException { //TODO: why throw? (Caner)
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

        //TODO: explain why do we need this or delete (Caner)

        // either new method listener or:
        // InvalidationListener listener = null;
        graphProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                graphProperty.setValue(graph);
            }
        });
    }


    //TODO: the name here is confusing. parsing the graph is only one of it, the rest is layout! (Caner)

    // create needed objects of the IO classes to use them in presenter
    public void parseGraph(String path, Dimension dimension) throws IOException {
        this.graph = GraphParser.readFile(path);

        // Store connected components in a Set of Set of Vertices using the JUNG lib algorithm
        WeakComponentClusterer<MyVertex, MyEdge> weakComponentClusterer = new WeakComponentClusterer<>();
        Set<Set<MyVertex>> cluster = weakComponentClusterer.apply(graph);
        // The Comparator sorts the Set of Sets based on their size. In the SortedSet the sets with biggest size appear first

        //TODO: too old-school, but okay :) (Caner)
        Comparator<Set<MyVertex>> comparator = new Comparator<Set<MyVertex>>() {
            @Override
            public int compare(Set<MyVertex> o1, Set<MyVertex> o2) {
                if (o1.size() > o2.size()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        };
        SortedSet<Set<MyVertex>> sortedSet = new TreeSet<>(comparator);
        sortedSet.addAll(cluster);
        double shiftX = 0.0;
        double shiftY = 0.0;
        // Apply the layout onto every set of vertices and update coordinates.
        for (Set<MyVertex> set : sortedSet) {
            if (set.size() > 1) {
                UndirectedSparseGraph<MyVertex, MyEdge> auxGraph = createAuxiliaryGraph(set);
                // Calculate layout dimension for each set based on the set size
                int dimensionX = (int) ((double) dimension.width * ((double) set.size() / (double) graph.getVertexCount()));
                int dimensionY = (int) ((double) dimension.height * ((double) set.size() / (double) graph.getVertexCount()));
                Dimension setDimension = new Dimension(dimensionX, dimensionY);
                applyLayout(auxGraph, setDimension, shiftX, shiftY);
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

    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph) {
        this.graph = graph;
    }

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graph, path, currentTaxTree, taxons);
    }

    public int getTaxaCount() {
        return taxons.size();
    }

    public TreeSet getTaxaID() {
        return taxons;
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graph, path);
    }

    private UndirectedSparseGraph<MyVertex, MyEdge> createAuxiliaryGraph(Set<MyVertex> vertices) {
        UndirectedSparseGraph<MyVertex, MyEdge> auxGraph = new UndirectedSparseGraph<>();
        for (MyVertex v : vertices) {
            auxGraph.addVertex(v);
            for (MyEdge edge : this.graph.getInEdges(v)) {
                auxGraph.addEdge(edge, edge.getVertices());
            }
        }
        return auxGraph;
    }

    public void applyLayout(UndirectedSparseGraph<MyVertex, MyEdge> graph, Dimension dimension, double shiftX, double shiftY) {
        FRLayout<MyVertex, MyEdge> layout = new FRLayout<>(graph);
        layout.setRepulsionMultiplier(0.1);
        layout.initialize();
        layout.setSize(dimension);

        VisRunner relaxer = new VisRunner((IterativeContext) layout);
        relaxer.prerelax();
        relaxer.relax();
        relaxer.run();
        // Apply layout onto auxGraph
        for (MyVertex v : graph.getVertices()) {
            v.setX(layout.getX(v) + shiftX);
            v.setY(layout.getY(v) + shiftY);
        }
    }

    public HashMap<Integer, String> createColor(Integer taxaCount, TreeSet taxaID) {
        int r = 3, g = 3, b = 3, alpha = 1, rgbBorderHigh = 255, counter = 0;
        HashMap<Integer, String> taxIDRGBCode = new HashMap<>();

        int temp1 = ((taxaCount + 9) / 10) * 10;
        int iterationStepsPerColor = temp1 / 3;
        int stepSizePerColor = rgbBorderHigh / (iterationStepsPerColor + 1);

        for (Object i : taxaID) {
            counter += 1;
            String rgbCodeTaxa = "";
            if (counter <= iterationStepsPerColor) {
                r += stepSizePerColor;
                rgbCodeTaxa = r + "t" + g + "t" + b;
            } else if (counter > iterationStepsPerColor && counter <= iterationStepsPerColor * 2) {
                g += stepSizePerColor;
                rgbCodeTaxa = r + "t" + g + "t" + b;
            } else {
                b += stepSizePerColor;
                rgbCodeTaxa = r + "t" + g + "t" + b;
            }
            taxIDRGBCode.put((int) i, rgbCodeTaxa);
            rgbCodeTaxa = "";
        }
        return taxIDRGBCode;
    }
}
