package model;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
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
import java.util.ArrayList;

public class Model {

    private TaxonomyTree currentTaxTree;
    private FRLayout<MyVertex, MyEdge> layout;
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
    //private SimpleObjectProperty graph1 = new SimpleObjectProperty(graph);
    private ObjectProperty<UndirectedSparseGraph<MyVertex, MyEdge>> graphProperty = new SimpleObjectProperty<>();
    private UndirectedSparseGraph<MyVertex, MyEdge> lonelyGraph;
    private StaticLayout<MyVertex,MyEdge> lonelyLayout;



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
        this.lonelyGraph = new UndirectedSparseGraph<>();

        groupGraph(graph);
        initializeLayout(graph, dimension);
        System.out.println("Layout initialized");
        initializeGrid(lonelyGraph, dimension.width, dimension.height);
        centering(dimension);


    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return graph;
    }

    public StaticLayout<MyVertex, MyEdge> getLonelyLayout() {
        return lonelyLayout;
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getLonelyGraph() {
        return lonelyGraph;
    }

    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph){ this.graph = graph;}

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

    private void initializeGrid(UndirectedSparseGraph<MyVertex, MyEdge> graph,  int maxX, int startY){
        this.lonelyLayout = new StaticLayout(graph);
        this.lonelyLayout.initialize();
        int initial=20;
        //MyVertex vertex = graph.getVertices().iterator().next();
        int countX=0+initial;
        int countY=startY-1;
        for (MyVertex vertex: this.lonelyGraph.getVertices()){
            if (countX%maxX ==0+initial){
                countY+=20;
            }

            //lonelyLayout.setLocation(vertex, 600, 600);
            lonelyLayout.setLocation(vertex, countX%(maxX-initial)+initial, countY);

            countX+=20;
        }

    }

    private void groupGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph){
         //need to create new collection to iterate over to remove vertices
        Iterable<MyVertex> vertices = new ArrayList<MyVertex>(graph.getVertices());
        for (MyVertex v:vertices){
            if (graph.getPredecessors(v).size()==0){
                this.lonelyGraph.addVertex(v);
                this.graph.removeVertex(v);
            }
        }
    }

    private void centering(Dimension dimension) {

        for (MyVertex v: graph.getVertices()){
            layout.setLocation(v, layout.apply(v).getX()+dimension.width/2, layout.apply(v).getY()+dimension.height/2 );
        }
        for (MyVertex v: lonelyGraph.getVertices()){
            lonelyLayout.setLocation(v, lonelyLayout.apply(v).getX()+dimension.width/2, lonelyLayout.apply(v).getY()+dimension.height/2 );
        }

    }
}
