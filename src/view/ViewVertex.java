package view;

/*
Created by Antonia.
Visualizes Vertex.
 */

import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

/*
constructed by Antonia and Jonas.
 */

public class ViewVertex extends Group {

    private StringProperty ID;
    private Circle circle;
    private boolean selected;

    public ViewVertex(String ID, int size, double x, double y){
        this.ID = new SimpleStringProperty(ID);
        this.circle = new Circle(0, 0, size);
        this.selected = false;
        this.setTranslateX(x);
        this.setTranslateY(y);
        circle.setFill(Color.CORAL);
        circle.setStroke(Color.CORAL);
        this.getChildren().add(circle);
    }

    public Circle getCircle() {
        return circle;
    }

    public String getID() {
        return ID.get();
    }

    public boolean getSelected(){
        return selected;
    }

    public StringProperty getIDProperty() {
        return ID;
    }
    
    public void setSelected(){
        if (selected){
            selected=false;
            circle.setStroke(circle.getFill());
        }
        else{
            selected=true;
            circle.setStroke(Color.BLACK);
        }
    }

    public void animate(double shiftX, double shiftY){
        TranslateTransition tt = new TranslateTransition(Duration.millis(2000),this);
        tt.setToX(shiftX);
        tt.setToY(shiftY);
        tt.play();
    }

    public double getSize() {
        return circle.getRadius();
    }

    public void setSize(double size) {
        this.circle.setRadius(size);
    }
}
