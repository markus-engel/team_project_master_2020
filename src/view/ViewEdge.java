package view;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class ViewEdge extends Group {

    Line line;

    public ViewEdge(ViewVertex vv1, ViewVertex vv2){
        this.line = new Line(vv1.getShape().getCenterX(), vv1.getShape().getCenterY(), vv2.getShape().getCenterX(), vv2.getShape().getCenterY());
        line.setFill(Color.BLACK);

        this.getChildren().add(line);
        this.line.startXProperty().bind(vv1.getShape().centerXProperty());
        this.line.startYProperty().bind(vv1.getShape().centerYProperty());
        this.line.endXProperty().bind(vv2.getShape().centerXProperty());
        this.line.endYProperty().bind(vv2.getShape().centerYProperty());

    }
}
