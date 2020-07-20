package model.graph;

import com.sun.javafx.geom.Point2D;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import model.io.ContigProperty;

import java.awt.*;

/*
Class defining the vertices used in UndirectedSparseGraph.
Constructed by Antonia.
Modified by Anna: Object properties with getters & setters
Modified by Julia: further optional ContigProperties storable in a map "furtherProperties"
 */

public class MyVertex {
    private StringProperty IDprop = new SimpleStringProperty(this, "ID", "");
    private StringProperty sequenceprop = new SimpleStringProperty(this, "Sequence", "");
    private ObservableMap<ContigProperty, Object> furtherProperties = FXCollections.observableHashMap();
    private double x;
    private double y;

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

    public void setSequenceprop(StringProperty sequenceprop) {
        this.sequenceprop = sequenceprop;
    }

    // Add new properties, like ContigProperty.COVERAGE, to the vertex (called by parsers)
    public void addProperty(ContigProperty propertyName, Object propertyValue) {
        furtherProperties.put(propertyName, propertyValue);
    }

    // Get a specific property of the vertex via the property name,
    // e.g. "getProperty(ContigProperty.TAXONOMY)" returns the Node from the taxonomic tree, to which this contig belongs
    public Object getProperty(ContigProperty propertyName) {
        return furtherProperties.getOrDefault(propertyName, null);
    }
    @Override // is this right? the other option is tp return the property but the conversion lasted so long
    public String toString() {
        return String.valueOf(this.IDprop);
    }

    public double getX() { return x; }

    public void setX(double x) { this.x = x; }

    public double getY() { return y; }

    public void setY(double y) { this.y = y; }
}
