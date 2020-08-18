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
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();
    private double lowestCoverage, highestCoverage;
    private double smallestContigLength, largestContigLength;
    private double lowestGCContent, highestGCContent;
    TreeSet<Integer> taxa = new TreeSet<>();
    TreeSet<String> ranks = new TreeSet<>();
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
                graphProperty.setValue(graphProperty.get());
            }
        });
        repulsionMultiplier = 0.1;
        attractionMultiplier = 1.0;
    }


    //TODO: the name here is confusing. parsing the graph is only one of it, the rest is layout! (Caner) @Julia: export for loop in different method (Jonas)

    // create needed objects of the IO classes to use them in presenter
    public void parseGFA(String path) throws IOException {
        this.graphProperty = new SimpleObjectProperty<>(GraphParser.readFile(path));
        calculateContigLengthRange();
        calculateGCContentRange();
    }

    private void calculateContigLengthRange() {
        double smallestContigLength = Double.MAX_VALUE;
        double largestContigLength = Double.MIN_VALUE;
        for (MyVertex v : graphProperty.get().getVertices()) {
            double contigLength = (double) v.getProperty(ContigProperty.LENGTH);
            if (contigLength < smallestContigLength)
                smallestContigLength = contigLength;
            else if (contigLength > largestContigLength)
                largestContigLength = contigLength;
        }
        setContigLengthRange(smallestContigLength, largestContigLength);
    }

    private void calculateGCContentRange () {
        double lowestGCContent = Double.MAX_VALUE;
        double highestGCContent = Double.MIN_VALUE;
        for (MyVertex v : graphProperty.get().getVertices()) {
            double gcContent = (double) v.getProperty(ContigProperty.GC);
            if (gcContent < lowestGCContent) {
                lowestGCContent = gcContent;
            }
            else if (gcContent > highestGCContent) {
                highestGCContent = gcContent;
            }
        }
        setGCContentRange(lowestGCContent, highestGCContent);
    }

    private SortedSet<Set<MyVertex>> clusterVertices(UndirectedSparseGraph<MyVertex, MyEdge> graph, boolean byContigLength) {
        // Store connected components in a Set of Set of Vertices using the JUNG lib algorithm
        WeakComponentClusterer<MyVertex, MyEdge> weakComponentClusterer = new WeakComponentClusterer<>();
        Set<Set<MyVertex>> cluster = weakComponentClusterer.apply(graph);
        // Each Vertex knows in which connected component it is
        for (Set<MyVertex> set : cluster) {
            for (MyVertex mv : set) {
                mv.setConnectedComponent(set);
            }
        }
        // The comparatorByNodeNumber sorts the Set of Sets based on their size. In the SortedSet the sets with biggest size appear first
        Comparator<Set<MyVertex>> comparatorByNodeNumber = (o1, o2) -> {
            if (o1.size() > o2.size()) {
                return -1;
            } else {
                return 1;
            }
        };
        // The comparatorByContigLength sorts the Set of Sets based on the sum of the contig lengths
        Comparator<Set<MyVertex>> comparatorByContigLength = (o1, o2) -> {
            double contigLengthSum1 = 0;
            double contigLengthSum2 = 0;
            for (MyVertex v : o1) contigLengthSum1 += (double) v.getProperty(ContigProperty.LENGTH);
            for (MyVertex v : o2) contigLengthSum2 += (double) v.getProperty(ContigProperty.LENGTH);
            return Double.compare(contigLengthSum2, contigLengthSum1);
        };
        try {
            if (!byContigLength) {
                SortedSet<Set<MyVertex>> sortedSet = new TreeSet<>(comparatorByNodeNumber);
                sortedSet.addAll(cluster);
                return sortedSet;
            } else {
                SortedSet<Set<MyVertex>> sortedSet = new TreeSet<>(comparatorByContigLength);
                sortedSet.addAll(cluster);
                return sortedSet;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void applyLayout(Dimension dimension, UndirectedSparseGraph<MyVertex, MyEdge> graph, boolean byContigLength) {
        SortedSet<Set<MyVertex>> sortedSet = clusterVertices(graph, byContigLength);
        double shiftX = 0.0;
        double shiftY = 0.0;
        double maxY = 0.0;
        boolean firstLonelyVertices;
        int firstLonelyVerticesShiftY = 0;
        int ratio = graph.getVertexCount() - getLonelyVertexCount(graph);
        int spaceInbetween = 15;
        // Apply the layout onto every set of vertices and update coordinates.
        for (Set<MyVertex> set : sortedSet) {
            firstLonelyVertices = true;
            if(set.size() > 1){
                UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = createAuxiliarGraph(set, graph);
                // Calculate layout dimension for each set based on the set size
                int dimensionX = (int) ((double)dimension.width*((double)set.size()/(double) ratio));
                int dimensionY = (int) ((double)dimension.height*((double)set.size()/(double)ratio));
                firstLonelyVerticesShiftY = dimensionY;
                Dimension setDimension = new Dimension(dimensionX,dimensionY);
                if(dimensionY > maxY) {
                    maxY = (double) dimensionY + spaceInbetween;
                }
                if(dimensionX + shiftX > dimension.width){
                    shiftX = 0.0;
                    shiftY += maxY;
                    maxY = 0.0;
                    applyLayoutAndShiftCoords(auxGraph,setDimension,shiftX,shiftY);
                } else {
                    applyLayoutAndShiftCoords(auxGraph,setDimension,shiftX,shiftY);
                    shiftX += dimensionX + spaceInbetween;
                }
            } else {
                for(MyVertex lonelyVertex : set) {
                    lonelyVertex.setX(shiftX);
                    lonelyVertex.setY(shiftY);
                    shiftX += 10;
                    if (shiftX > dimension.width && firstLonelyVertices) {
                        shiftX = 0.0;
                        shiftY += firstLonelyVerticesShiftY + spaceInbetween;
                        firstLonelyVertices = false;
                    } else if (shiftX > dimension.width) {
                        shiftX = 0.0;
                        shiftY += spaceInbetween;
                    }
                }
            }
        }
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graphProperty.get();
    }

    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph) {this.graphProperty.set(graph);}

    public void parseTaxId(String path) throws IOException {
        new TaxIdParser(graphProperty.get(), path, taxonomyTree, taxa, ranks);
    }

    public int getTaxaCount() {
        return taxa.size();
    }

    public TreeSet<Integer> getTaxaID() {
        return taxa;
    }

    public TreeSet<String> getRanks() {
        return ranks;
    }

    public void setAttractionMultiplier(double attractionMultiplier) {
        this.attractionMultiplier = attractionMultiplier;
    }

    public double getAttractionMultiplier() {
        return attractionMultiplier;
    }

    public void setRepulsionMultiplier(double repulsionMultiplier) {
        this.repulsionMultiplier = repulsionMultiplier;
    }

    public double getRepulsionMultiplier() {
        return repulsionMultiplier;
    }

    public void setCoverageRange(double lowerLimit, double upperLimit) {
        lowestCoverage = lowerLimit;
        highestCoverage = upperLimit;
    }

    public void setContigLengthRange(double lowerLimit, double upperLimit) {
        smallestContigLength = lowerLimit;
        largestContigLength = upperLimit;
    }

    public double getLowestCoverage() {
        return lowestCoverage;
    }

    public double getHighestCoverage() {
        return highestCoverage;
    }

    public double getSmallestContigLength() {
        return smallestContigLength;
    }

    public double getLargestContigLength() {
        return largestContigLength;
    }

    public void setGCContentRange (double lowerLimit, double upperLimit) {
        lowestGCContent = lowerLimit;
        highestGCContent = upperLimit;
    }

    public double getLowestGCContent(){
        return lowestGCContent;
    }

    public double getHighestGCContent () {
        return highestGCContent;
    }

    public void parseCoverage(String path) throws IOException {
        new CoverageParser(graphProperty.get(), path);
        double lowestCoverage = Double.MAX_VALUE;
        double highestCoverage = Double.MIN_VALUE;
        for (MyVertex v : graphProperty.get().getVertices()) {
            double coverage = (double) v.getProperty(ContigProperty.COVERAGE);
            if (coverage < lowestCoverage)
                lowestCoverage = coverage;
            else if (coverage > highestCoverage)
                highestCoverage = coverage;
        }
        setCoverageRange(lowestCoverage, highestCoverage);
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

    private UndirectedSparseGraph<MyVertex,MyEdge> createAuxiliarGraph(Set<MyVertex> vertexSet, UndirectedSparseGraph<MyVertex, MyEdge> graph){
        UndirectedSparseGraph<MyVertex,MyEdge> auxGraph = new UndirectedSparseGraph<>();
        for(MyVertex v: vertexSet){
            v.setConnectedComponent(vertexSet);
            auxGraph.addVertex(v);
            for(MyEdge edge : graph.getInEdges(v)){
                auxGraph.addEdge(edge, edge.getVertices());
            }
        }
        return auxGraph;
    }

    private int getLonelyVertexCount(UndirectedSparseGraph<MyVertex, MyEdge> graph){
        int count = 0;
        for (MyVertex v : graph.getVertices()){
            if (graph.getInEdges(v).isEmpty() || (graph.getNeighbors(v).contains(v) && graph.getNeighbors(v).size() == 1)){
                count++;
            }
        }
        return count;
    }

    public Map<Integer, String> createColor(TreeSet<Integer> taxaID) {
        int[] rgbNumbers;
        double alpha = 1;
        Map<Integer, String> taxIDRGBCode = new HashMap<>();
//        https://www.farb-tabelle.de/en/table-of-color.htm#white
        for (Object i : taxaID) {
            String rgbCodeTaxa = "";
            rgbNumbers = randomNumberColoring();
            rgbCodeTaxa = rgbNumbers[0] + "t" + rgbNumbers[1] + "t" + rgbNumbers[2] + "t" + alpha;
            taxIDRGBCode.put((int) i, rgbCodeTaxa);
        }
        return taxIDRGBCode;
    }

    public Map<String, String> createColorRank(Set<String> rankMembers) {
        int[] rgbNumbersRank;
        Map<String, String> rankRGBCode = new HashMap<>();

        for (String i : rankMembers) {
            String rgbCodeTaxa;
            rgbNumbersRank = randomNumberColoring();
            rgbCodeTaxa = rgbNumbersRank[0] + "t" + rgbNumbersRank[1] + "t" + rgbNumbersRank[2];
            rankRGBCode.put(i, rgbCodeTaxa);
        }
        return rankRGBCode;
    }

    public int[] randomNumberColoring () {
        int lowestValue = 30;
        int highestValue = 230;
        int[] rgbNumbers = new int[3];
        for (int i = 1; i <= rgbNumbers.length; i ++) {
            rgbNumbers[i - 1] = (int) (lowestValue + Math.random() * ((highestValue - lowestValue) + 1));
        }
        return rgbNumbers;
    }

    public Map<Object, Double> heatmapColorsCovarge () {
        Map<Object, Double> coverage = new HashMap<>();
        double lowestCoverage = getLowestCoverage();
        double highestCoverage = getHighestCoverage();
        double range = highestCoverage - lowestCoverage;
        for (MyVertex v : getGraph().getVertices()) {
            double relativeCoverage = ((double) v.getProperty(ContigProperty.COVERAGE) - lowestCoverage) / range;
            coverage.put(v.getID(), relativeCoverage);
        }
        return coverage;
    }

    public Map<Object, Double> heatmapColorsGCContent () {
        Map<Object, Double> gcContent = new HashMap<>();
        double lowestGCContentent = getLowestGCContent();
        double highestGCContent = getHighestGCContent();
        double rangeGCContent = highestGCContent - lowestGCContentent;
        for (MyVertex v : getGraph().getVertices()) {
//            double relativeGCContent = ((double) v.getProperty(ContigProperty.GC) - lowestGCContentent) / rangeGCContent;
            gcContent.put(v.getID(), (Double) v.getProperty(ContigProperty.GC));
        }
        return gcContent;
    }
}
