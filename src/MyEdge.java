import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/*
Class defining the edges used in UndirectedSparseGraph.
Constructed by Antonia.
 */

public class MyEdge {
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;      //graph the edge is in

    public MyEdge(UndirectedSparseGraph<MyVertex, MyEdge> graph){
        this.graph=graph;
    }


    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph(){ return this.graph;}
    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph) { this.graph = graph; }

}
