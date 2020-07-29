package previousWork;

// Controller class for plot.fxml by Anna

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.ScrollEvent;
import view.ViewEdge;
import view.ViewVertex;

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
    private Group viewObjectsSele;

    @FXML
    private Group innerViewObjectsSele;

    public void setViewObjects(Group viewObjects) { this.viewObjectsSele = viewObjects;}

    public Group getViewObjects(){ return viewObjectsSele;}


    public void addVertex(ViewVertex vv) {
        viewObjectsSele.getChildren().add(vv);
        innerViewObjectsSele.getChildren().add(vv);
    }

    public void addEdge(ViewEdge viewEdge) {
        viewObjectsSele.getChildren().add(viewEdge);
        innerViewObjectsSele.getChildren().add(viewEdge);
    }

    public void setPane() {
        tabSelection.setContent(viewObjectsSele);
    }

    public void setScrollPane() {
        scrollPane.setContent(viewObjectsSele);
        makeZoomable();
    }

    public ScrollPane getScrollPane(){ return scrollPane;}

    private void makeZoomable() {
        scrollPane.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent scrollEvent) {
                if(scrollEvent.isControlDown()){
                    final double scale = calculateScaleForZooming(scrollEvent);
                    innerViewObjectsSele.setScaleX(scale);
                    innerViewObjectsSele.setScaleY(scale);
                    scrollEvent.consume();
                }
            }
        });
    }

    private double calculateScaleForZooming(ScrollEvent scrollEvent) {
        double scale = innerViewObjectsSele.getScaleX() + scrollEvent.getDeltaY()/100;
        if (scale <= MIN_SCALE) {
            scale = MIN_SCALE;
        } else if (scale >= MAX_SCALE) {
            scale = MAX_SCALE;
        }
        return scale;
    }

}
