import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class myVertex {

    private String ID;                          //ID of the vertex
    private String sequence;                    //sequence of the vertex
    private UndirectedSparseGraph<myVertex, myEdge> graph;        //graph of the Vertex

    public myVertex(String ID, String sequence){
        this.ID = ID;
        this.sequence = sequence;
    }

    public String getSequence(){
        return this.sequence;
    }

    public String getID(){ return this.ID;}

    public UndirectedSparseGraph<myVertex, myEdge> getGraph(){return this.graph;}

    public void setID(String ID){ this.ID=ID; }

    public void setSequence(String sequence){this.sequence=sequence;}

    public void setGraph(UndirectedSparseGraph<myVertex, myEdge> graph){this.graph=graph; }

    @Override
    public String toString() { return this.ID; }
}
