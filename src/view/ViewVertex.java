package view;

/*
Created by Antonia.
Visualizes Vertex.
 */

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/*
constructed by Antonia and Jonas.
 */

public class ViewVertex extends Group {

    private StringProperty ID;
    private Circle circle;


    public ViewVertex(String ID, int size, double x, double y){
        this.ID = new SimpleStringProperty(ID);
        this.circle = new Circle(0, 0, size);
        this.setTranslateX(x);
        this.setTranslateY(y);
        circle.setFill(Color.CORAL);
        this.getChildren().add(circle);
    }



    public Circle getCircle() {
        return circle;
    }

    public void setCoords(int x, int y){
        this.circle.setCenterX(x);
        this.circle.setCenterY(y);
    }

    public String getID() {
        return ID.get();
    }

    public StringProperty IDProperty() {
        return ID;
    }
}
