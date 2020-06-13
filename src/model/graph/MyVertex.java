package model.graph;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/*
Class defining the vertices used in UndirectedSparseGraph.
Constructed by Antonia.
Modified by Anna: Object properties with getters & setters
 */

public class MyVertex {

    StringProperty IDprop = new SimpleStringProperty(this, "ID", "");
    StringProperty sequenceprop = new SimpleStringProperty(this, "Sequence", "");
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;          //graph of the Vertex

    public MyVertex(StringProperty ID, StringProperty sequence){
        this.IDprop = ID;
        this.sequenceprop = sequence;
    }

    public MyVertex(StringProperty ID){
        this.IDprop = ID;
    }

    // getter & setter
    // first getter returns StringProperty object itself
    public String getIDprop() {
        return IDprop.get();
    }

    // second getter returns ID value of the StringProperty
    public StringProperty getIDpropProperty() {
        return IDprop;
    }

    public void setIDprop(String IDprop) {
        this.IDprop.set(IDprop);
    }

    // first getter returns StringProperty object itself
    public String getSequenceprop() {
        return sequenceprop.get();
    }

    // second getter returns ID value of the StringProperty
    public StringProperty getSequencepropProperty() {
        return sequenceprop;
    }

    public void setSequenceprop(String Sequenceprop) {
        this.sequenceprop.set(Sequenceprop);
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph(){return this.graph;}
    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph){this.graph=graph; }


    @Override // is this right? the other option is tp return the property but the conversion lasted so long
    public String toString() { return String.valueOf(this.IDprop); }



    /*
    // old code in case property changes doesn't work out
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

     */
}
