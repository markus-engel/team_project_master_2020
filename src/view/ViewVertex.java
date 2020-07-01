package view;

/*
Created by Antonia.
Visualizes Vertex.
 */

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import model.graph.MyVertex;

public class ViewVertex extends Group {

    private StringProperty ID;
    private Circle shape;


    public ViewVertex(String ID, int size, double x, double y){
        this.ID = new SimpleStringProperty(ID);
        this.shape = new Circle(x, y, size);
        shape.setFill(Color.CORAL);

        this.getChildren().add(shape);
    }

    public Circle getShape() {
        return shape;
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }
}
