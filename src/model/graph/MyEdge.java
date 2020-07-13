package model.graph;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.Collection;
import java.util.Set;

/*
Class defining the edges used in UndirectedSparseGraph.
Constructed by Antonia.
 */


public class MyEdge {
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;//graph the edge is in
    private Pair<MyVertex> vertices;

    public MyEdge(UndirectedSparseGraph<MyVertex, MyEdge> graph, Pair<MyVertex> vertices){
        this.vertices=vertices;
        this.graph=graph;
    }

    public Pair<MyVertex> getVertices(){
        return this.vertices;
    }

    public MyVertex getFirst(){
        return this.vertices.getFirst();
    }

    public MyVertex getSecond(){
        return this.vertices.getSecond();
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph(){ return this.graph;}
    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph) { this.graph = graph; }

}
