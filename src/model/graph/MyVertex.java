package model.graph;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/*
Class defining the vertices used in UndirectedSparseGraph.
Constructed by Antonia.
 */

public class MyVertex {

    private String ID;                                              //ID of the vertex
    private String sequence;                                        //sequence of the vertex
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;          //graph of the Vertex

    public MyVertex(String ID, String sequence){
        this.ID = ID;
        this.sequence = sequence;
    }

    public MyVertex(String ID){
        this.ID = ID;
    }

    public String getSequence(){
        return this.sequence;
    }

    public String getID(){ return this.ID;}

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph(){return this.graph;}

    public void setID(String ID){ this.ID=ID; }

    public void setSequence(String sequence){this.sequence=sequence;}

    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph){this.graph=graph; }

    @Override
    public String toString() { return this.ID; }
}
