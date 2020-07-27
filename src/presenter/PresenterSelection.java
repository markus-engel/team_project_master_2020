package presenter;

// Presenter for Selection.fxml by Anna


import model.Model;
import view.ViewSelection;
import view.ViewVertex;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class PresenterSelection extends Presenter {
    Model model;
    ViewSelection viewSelection;
    Map<String, ViewVertex> viewVerticesSele = new HashMap<>();  //Hashmap of view vertex objects
    //UndirectedSparseGraph<MyVertex,MyEdge> seleGraph = new UndirectedSparseGraph<>();;


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
        model.applyLayoutAndShiftCoords(seleGraph, setDimension, shiftX,shiftY);

        // visualization is missing
    }
}
