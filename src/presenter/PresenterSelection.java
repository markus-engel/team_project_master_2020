package presenter;

// Presenter for Selection.fxml by Anna


import model.Model;
import model.graph.MyEdge;
import model.graph.MyVertex;
import view.ViewEdge;
import view.ViewSelection;
import view.ViewVertex;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;


public class PresenterSelection extends Presenter {
    Model model;
    ViewSelection viewSelection;
    HashMap<String, ViewVertex> viewVerticesSele = new HashMap<>();  //Hashmap of view vertex objects


    public PresenterSelection(Model model, ViewSelection viewSelection) throws IOException {
        super();
        this.model = model;
        this.viewSelection = viewSelection;
        showSelectionGraph();

    }


    // show Selection Graph
    public void showSelectionGraph() throws IOException {
        double shiftX = 0.0;
        double shiftY = 0.0;
        Dimension setDimension = new Dimension(MAX_WINDOW_DIMENSION.width,MAX_WINDOW_DIMENSION.height);
        model.applyLayout(seleGraph, setDimension, shiftX,shiftY);
        visualizeGraph(5);
        // visualization is missing
    }

    private void visualizeGraph(int size) {

        // add view vertices
        for (MyVertex v1 : seleGraph.getVertices()) {
            ViewVertex vv = new ViewVertex(v1.getIDprop(), size, v1.getX(), v1.getY());
            viewSelection.addVertex(vv);
            viewVerticesSele.put(v1.getIDprop(), vv);
            makeDraggable(vv, size);
        }
        // add view edges
        for (MyEdge edge : seleGraph.getEdges()) {
            ViewEdge ve = new ViewEdge(viewVerticesSele.get(edge.getFirst().getIDprop()), viewVerticesSele.get(edge.getSecond().getIDprop()));
            viewSelection.addEdge(ve);
        }
        viewSelection.setPane();
        System.out.println("test");
    }

    private void makeDraggable(ViewVertex viewVertex, int size) {
        viewVertex.setOnMouseDragged(event -> {
            int x = (int) Math.ceil(event.getX());
            int y = (int) Math.ceil(event.getY());
            if (x < 0 + size) {
                x = 0 + size;
            }
            if (y < 0 + size) {
                y = 0 + size;
            }
            if (x > MAX_WINDOW_DIMENSION.width - size) {
                x = MAX_WINDOW_DIMENSION.width - size;
            }
            if (y > MAX_WINDOW_DIMENSION.height - size) {
                y = MAX_WINDOW_DIMENSION.height - size;
            }
            viewVertex.setCoords(x, y);
            viewVertex.toFront();
        });
    }
}
