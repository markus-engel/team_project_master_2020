import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class myEdge {
    private UndirectedSparseGraph<myVertex, myEdge> graph;

    public myEdge(UndirectedSparseGraph<myVertex, myEdge> graph){
        this.graph=graph;
    }

    public UndirectedSparseGraph<myVertex, myEdge> getGraph(){ return this.graph;}

    public void setGraph(UndirectedSparseGraph<myVertex, myEdge> graph) { this.graph = graph; }

}
