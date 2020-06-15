package model;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.GraphParser;
import java.io.IOException;

public class Model {

    private UndirectedSparseGraph<MyVertex, MyEdge> graph;

    public Model() throws IOException {
        graph = new UndirectedSparseGraph<MyVertex,MyEdge>();
    }

    // create needed objects of the IO classes to use them in presenter
    public UndirectedSparseGraph<MyVertex,MyEdge> parseGraph (String path) throws IOException {
        GraphParser gp = new GraphParser();
        return gp.readFile(path);
    }

    public UndirectedSparseGraph<MyVertex,MyEdge> getGraph(){ return graph;}

    public void setGraph(UndirectedSparseGraph<MyVertex,MyEdge> graph) { this.graph = graph;}

    //TaxIdParser currentTaxIdParser = new TaxIdParser();
    //TaxonomyTree currentTaxTree = new TaxonomyTree();
    //currentTaxTree.getRank(9); // as method call example
}
