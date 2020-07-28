package view;

// Controller class for plot.fxml by Anna

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;

public class ViewSelection {

    public final double MAX_SCALE = 2.d;
    public final double MIN_SCALE = .5d;

    @FXML
    private TabPane tabPaneSele;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Tab tabSelection;

    @FXML
    private Group viewObjects;

    @FXML
    private Group innerViewObjects;

    public void setViewObjects(Group viewObjects) { this.viewObjects = viewObjects;}

    public Group getViewObjects(){ return viewObjects;}


    public void addVertex(ViewVertex vv) {
        viewObjects.getChildren().add(vv);
        innerViewObjects.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        viewObjects.getChildren().add(viewEdge);
        innerViewObjects.getChildren().add(viewEdge);
    }

    public void setPane() {
        tabSelection.setContent(viewObjects);
    }

    public void setScrollPane() {
        scrollPane.setContent(viewObjects);
        makeZoomable();
    }

    public ScrollPane getScrollPane(){ return scrollPane;}

    private void makeZoomable() {
        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.isControlDown()){
                    final double scale = calculateScaleForZooming(scrollEvent);
                    innerViewObjects.setScaleX(scale);
                    innerViewObjects.setScaleY(scale);
                    scrollEvent.consume();
                }
            }
        });
    }

    private double calculateScaleForZooming(ScrollEvent scrollEvent) {
        double scale = innerViewObjects.getScaleX() + scrollEvent.getDeltaY()/100;
        if (scale <= MIN_SCALE) {
            scale = MIN_SCALE;
        } else if (scale >= MAX_SCALE) {
            scale = MAX_SCALE;
        }
        return scale;
    }

}
