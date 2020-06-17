package model;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.concurrent.Task;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.GraphParser;
import model.io.TaxonomyTree;

import java.io.IOException;

public class Model {

    private UndirectedSparseGraph<MyVertex, MyEdge> graph;
    TaxonomyTree currentTaxTree;

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

        graph = new UndirectedSparseGraph<MyVertex, MyEdge>();
    }

    // create needed objects of the IO classes to use them in presenter
    public UndirectedSparseGraph<MyVertex,MyEdge> parseGraph (String path) throws IOException {
        GraphParser gp = new GraphParser();
        return gp.readFile(path);
    }

    public UndirectedSparseGraph<MyVertex,MyEdge> getGraph(){ return graph;}

    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph) { this.graph = graph;}

    //TaxIdParser currentTaxIdParser = new TaxIdParser();
    //currentTaxTree.getRank(9); // as method call example
}
