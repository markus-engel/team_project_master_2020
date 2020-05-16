import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class myEdge {
    //private Vertex source;              //source of the edge
    // private Vertex destination;         //destination of the edge
    private UndirectedSparseGraph<myVertex, myEdge> graph;

    public myEdge(UndirectedSparseGraph<myVertex, myEdge> graph){
        this.graph=graph;
        // this.source = source;
        // this.destination = destination;
    }

   // public Vertex getSource(){ return this.source; }

   // public Vertex getDestination(){ return this.destination; }

    public UndirectedSparseGraph<myVertex, myEdge> getGraph(){ return this.graph;}

    //public void setSource(Vertex source){this.source=source;}

    //public void setDestination(Vertex destination){this.destination=destination;}

    public void setGraph(UndirectedSparseGraph<myVertex, myEdge> graph) { this.graph = graph; }

}
