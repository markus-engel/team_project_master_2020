package model;

import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.GraphParser;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;

import java.io.IOException;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

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
    // should we change the TaxIdParser? Because now its working through its main and there's no method to call

    //TaxonomyTree currentTaxTree = new TaxonomyTree();
    //currentTaxTree.getRank(9); // as method call example

}
