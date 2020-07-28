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

    private TaxonomyTree taxonomyTree;
    private UndirectedSparseGraph<MyVertex, MyEdge> graph; //TODO: do we need this? it's already in graphProperty (Caner)
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();
    TreeSet<Integer> taxa = new TreeSet(); //TODO: define the type <T> (Caner)
    private double repulsionMultiplier;
    private double attractionMultiplier;

    public Model() {
        // Instantiation of the currentTaxTree in a task to show the responsive GUI already while parsing the tree
        Task<Void> taskTaxonomyTree = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                taxonomyTree = new TaxonomyTree();
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
        repulsionMultiplier = 0.1;
        attractionMultiplier = 1.0;
    }


    //TODO: the name here is confusing. parsing the graph is only one of it, the rest is layout! (Caner): Hope thats better (Jonas)

    // create needed objects of the IO classes to use them in presenter
    public void parseGraph(String path) throws IOException {
        this.graph = GraphParser.readFile(path);
    }

    private SortedSet<Set<MyVertex>> clusterVertices(){
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
        return sortedSet;
    }

    public void applyLayout(Dimension dimension, UndirectedSparseGraph<MyVertex, MyEdge> currentGraph){
        SortedSet<Set<MyVertex>> sortedSet = clusterVertices();
        double shiftX = 0.0;
        double shiftY = 0.0;
        double maxY = 0.0;
        boolean firstLonelyVertices;
        int firstLonelyVerticesShiftY = 0;
        int ratio = currentGraph.getVertexCount() - getLonelyVertexCount();
        // Apply the layout onto every set of vertices and update coordinates.
        for(Set<MyVertex> set : sortedSet){
            firstLonelyVertices = true;
            if(set.size() > 1){
                UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = createAuxilliarGraph(set);
                // Calculate layout dimension for each set based on the set size
                int dimensionX = (int) ((double)dimension.width*((double)set.size()/(double) ratio));
                int dimensionY = (int) ((double)dimension.height*((double)set.size()/(double)ratio));
                firstLonelyVerticesShiftY = dimensionY;
                Dimension setDimension = new Dimension(dimensionX,dimensionY);
                if(dimensionY > maxY) {
                    maxY = (double) dimensionY + 15;
                }
                if(dimensionX + shiftX > dimension.width){
                    shiftX = 0.0;
                    shiftY += maxY;
                    maxY = 0.0;
                    applyLayoutAndShiftCoords(auxGraph,setDimension,shiftX,shiftY);
                } else {
                    applyLayoutAndShiftCoords(auxGraph,setDimension,shiftX,shiftY);
                    shiftX += dimensionX + 15;
                }
            } else {
                set.iterator().next().setX(shiftX);
                set.iterator().next().setY(shiftY);
                shiftX += 10;
                if(shiftX > dimension.width && firstLonelyVertices){
                    shiftX = 0.0;
                    shiftY += firstLonelyVerticesShiftY + 15;
                    firstLonelyVertices = false;
                }
                else if(shiftX > dimension.width){
                    shiftX = 0.0;
                    shiftY += 15;
                }
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
        new TaxIdParser(graph, path, taxonomyTree, taxa);
    }

    public int getTaxaCount() {
        return taxa.size();
    }

    public TreeSet getTaxaID() {
        return taxa;
    }

    public void setAttractionMultiplier(double attractionMultiplier) { this.attractionMultiplier = attractionMultiplier;}

    public double getAttractionMultiplier() {return attractionMultiplier;}

    public void setRepulsionMultiplier(double repulsionMultiplier) { this.repulsionMultiplier = repulsionMultiplier;}

    public double getRepulsionMultiplier() {return repulsionMultiplier;}

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

    public void applyLayoutAndShiftCoords(UndirectedSparseGraph<MyVertex, MyEdge> graph, Dimension dimension, double shiftX, double shiftY) {
        FRLayout<MyVertex, MyEdge> layout = new FRLayout<>(graph);
        layout.setRepulsionMultiplier(repulsionMultiplier);
        layout.setAttractionMultiplier(attractionMultiplier);
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

    private UndirectedSparseGraph<MyVertex,MyEdge> createAuxilliarGraph(Set<MyVertex> vertexSet){
        UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = new UndirectedSparseGraph<>();
        for(MyVertex v: vertexSet){
            v.setConnectedComponent(vertexSet);
            auxGraph.addVertex(v);
            for(MyEdge edge : this.graph.getInEdges(v)){
                auxGraph.addEdge(edge, edge.getVertices());
            }
        }
        return auxGraph;
    }

    private int getLonelyVertexCount(){
        int count = 0;
        for (MyVertex v : graph.getVertices()){
            if (graph.getInEdges(v).isEmpty() || (graph.getNeighbors(v).contains(v) && graph.getNeighbors(v).size() == 1)){
                count++;
            }
        }
        return count;
    }

    public HashMap<Integer, String> createColor(Integer taxaCount, TreeSet taxaID) {
        int r = 5, g = 5, b = 5, rgbBorderHigh = 255, counter = 0;
        double alpha = 1;
        HashMap<Integer, String> taxIDRGBCode = new HashMap<>();

//        https://www.farb-tabelle.de/en/table-of-color.htm#white
        int temp1 = ((taxaCount + 9) / 10) * 10;
        int iterationStepsPerColor = temp1 / 3;
        int stepSizePerColor = rgbBorderHigh / (iterationStepsPerColor + 1);

        for (Object i : taxaID) {
            counter += 1;
            String rgbCodeTaxa = "";
            if (counter <= iterationStepsPerColor) {
                r += stepSizePerColor;
                alpha -= stepSizePerColor/100;
                rgbCodeTaxa = r + "t" + g + "t" + b + "t" + alpha;
            } else if (counter > iterationStepsPerColor && counter <= iterationStepsPerColor * 2) {
                g += stepSizePerColor;
                alpha -= stepSizePerColor/100;
//                r = 125;
                rgbCodeTaxa = r + "t" + g + "t" + b + "t" + alpha;
            } else {
                b += stepSizePerColor;
                alpha -= stepSizePerColor/100;
                rgbCodeTaxa = r + "t" + g + "t" + b + "t" + alpha;
            }
            taxIDRGBCode.put((int) i, rgbCodeTaxa);
            rgbCodeTaxa = "";
        }
        return taxIDRGBCode;
    }
}
