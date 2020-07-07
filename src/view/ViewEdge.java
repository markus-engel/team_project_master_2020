package view;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/*
constructed by Antonia and Jonas.
 */

public class ViewEdge extends Group {

    private Line line;
    // V1 and V2 also classvariables?
    
    public ViewEdge(ViewVertex vv1, ViewVertex vv2){
        this.line = new Line(vv1.getCircle().getCenterX(), vv1.getCircle().getCenterY(), vv2.getCircle().getCenterX(), vv2.getCircle().getCenterY());
        line.setFill(Color.BLACK);

        this.getChildren().add(line);
        this.line.startXProperty().bind(vv1.getCircle().centerXProperty());
        this.line.startYProperty().bind(vv1.getCircle().centerYProperty());
        this.line.endXProperty().bind(vv2.getCircle().centerXProperty());
        this.line.endYProperty().bind(vv2.getCircle().centerYProperty());

    }
}
