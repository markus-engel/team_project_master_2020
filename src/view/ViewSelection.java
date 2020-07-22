package view;

// Controller class for plot.fxml by Anna

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ViewSelection {

    @FXML
    private TabPane tabPaneSele;

    @FXML
    private Tab tabSelection;

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot) {
        this.tabSelection.setContent(gcCoveragePlot);
    }
}
