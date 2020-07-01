package model;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.concurrent.Task;
import javafx.geometry.Point2D;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.GraphParser;
import model.io.TaxonomyTree;

import java.awt.*;
import java.io.IOException;

public class Model {

    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
    private TaxonomyTree currentTaxTree;
    private FRLayout<MyVertex, MyEdge> layout;


    public Model() throws IOException {

        // Instantiation of the currentTaxTree in a Task to show the responsive GUI already while parsing the tree
        Task<Void> taskTaxonomyTree = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                currentTaxTree = new TaxonomyTree();
                System.out.println("Taxonomy tree is loaded");
                return null;
            }
        };
        new Thread(taskTaxonomyTree).start();


    }

    // create needed objects of the IO classes to use them in presenter
    public void parseGraph (String path) throws IOException {
        this.graph = GraphParser.readFile(path);
        initializeLayout(graph);
    }

    public UndirectedSparseGraph<MyVertex,MyEdge> getGraph(){ return graph;}


    //WAS MEINT setgraph? Für wen? PREsenter? neuen graphen einlesen? weil dann reicht ja PARSEGRAPH oder??
    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph) {

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

    public Layout<MyVertex, MyEdge> getLayout(){
        return this.layout;
    }
}
