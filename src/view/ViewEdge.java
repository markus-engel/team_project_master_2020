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

        //TODO: Group actually makes it more complicated here. if you want to interact with the group (which I don't think you'll), you can't atm, because the group itself doesn't have coordinates. either get rid of the group or implement more complex bindings which uses group's coordinates (Caner)
        //the reason we have it as a group, is so that we can add the edges to the panes, becuase i believe they have to be nodes, right? or is there a better way? should we just add the lines themselves? (antonia)
    }
}
