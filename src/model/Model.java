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

    public HashMap<Integer, String> createColor(Integer taxaCount, TreeSet<Integer> taxaID) {
//        int r = 5, g = 5, b = 5, rgbBorderHigh = 255;
        int[] rgbNumbers;
        double alpha = 1;
        HashMap<Integer, String> taxIDRGBCode = new HashMap<>();

//        https://www.farb-tabelle.de/en/table-of-color.htm#white


//        int temp1 = ((taxaCount + 9) / 10) * 10;
//        int iterationStepsPerColor = temp1 / 3;
//        int stepSizePerColor = rgbBorderHigh / (iterationStepsPerColor + 1);

        for (Object i : taxaID) {
            String rgbCodeTaxa = "";
            rgbNumbers = randomNumberColoring();
            rgbCodeTaxa = rgbNumbers[0] + "t" + rgbNumbers[1] + "t" + rgbNumbers[2] + "t" + alpha;
//            if (counter <= iterationStepsPerColor) {
////                r += stepSizePerColor;
////                alpha -= stepSizePerColor/100.0;
//                rgbNumbers = randomNumberColoring();
//                rgbCodeTaxa = rgbNumbers[1] + "t" + rgbNumbers[1] + "t" + rgbNumbers[1] + "t" + alpha;
//            } else if (counter > iterationStepsPerColor && counter <= iterationStepsPerColor * 2) {
////                g += stepSizePerColor;
////                alpha -= stepSizePerColor/100.0;
////                r = 125;
//                rgbCodeTaxa = r + "t" + g + "t" + b + "t" + alpha;
//            } else {
////                b += stepSizePerColor;
////                alpha -= stepSizePerColor/100.0;
//                rgbCodeTaxa = r + "t" + g + "t" + b + "t" + alpha;
////                randomTest();
//            }
            taxIDRGBCode.put((int) i, rgbCodeTaxa);
        }
        return taxIDRGBCode;
    }

    public HashMap<String, String> createColorRank(TreeSet<String> ranks) {
        int[] rgbNumbersRank;
        HashMap<String, String> rankIDRGBCode = new HashMap<>();

        for (Object i : ranks) {
            String rgbCodeTaxa;
            rgbNumbersRank = randomNumberColoring();
            rgbCodeTaxa = rgbNumbersRank[0] + "t" + rgbNumbersRank[1] + "t" + rgbNumbersRank[2];
            rankIDRGBCode.put((String) i, rgbCodeTaxa);
        }
        return rankIDRGBCode;
    }

    public int[] randomNumberColoring () {
        int[] rgbNumbers = new int[3];
        for (int i = 1; i <= rgbNumbers.length; i ++) {
            rgbNumbers[i - 1] = (int) (Math.random() * ((255 - 0) + 1)) + 0;
        }
        return rgbNumbers;
    }
}
