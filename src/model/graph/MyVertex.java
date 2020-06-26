package model.graph;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/*
Class defining the vertices used in UndirectedSparseGraph.
Constructed by Antonia.
Modified by Anna: Object properties with getters & setters
Modified by Julia: further optional properties storable in a map "furtherProperties"
 */

public class MyVertex {
    StringProperty IDprop = new SimpleStringProperty(this, "ID", "");
    StringProperty sequenceprop = new SimpleStringProperty(this, "Sequence", "");
    private UndirectedSparseGraph<MyVertex, MyEdge> graph;          //graph of the Vertex
    ObservableMap<String, Object> furtherProperties = FXCollections.observableHashMap();

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

    // Add new properties, like a taxId, to the vertex (called by parsers)
    public void addProperty(String propertyName, Object propertyValue) {
        furtherProperties.put(propertyName, propertyValue);
    }

    // Get a specific property of the vertex via the property name, e.g. "taxonomy"
    public Object getProperty(String propertyName) {
        return furtherProperties.getOrDefault(propertyName, null);
    }

    public UndirectedSparseGraph<MyVertex, MyEdge> getGraph() {
        return this.graph;
    }

    public void setGraph(UndirectedSparseGraph<MyVertex, MyEdge> graph) {
        this.graph = graph;
    }


    @Override // is this right? the other option is tp return the property but the conversion lasted so long
    public String toString() {
        return String.valueOf(this.IDprop);
    }



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
