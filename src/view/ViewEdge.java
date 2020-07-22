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
        this.line = new Line(vv1.getTranslateX(), vv1.getTranslateY(), vv2.getTranslateX(), vv2.getTranslateY());
        line.setFill(Color.BLACK);

        this.getChildren().add(line);
        this.line.startXProperty().bind(vv1.translateXProperty());
        this.line.startYProperty().bind(vv1.translateYProperty());
        this.line.endXProperty().bind(vv2.translateXProperty());
        this.line.endYProperty().bind(vv2.translateYProperty());

    }
}
