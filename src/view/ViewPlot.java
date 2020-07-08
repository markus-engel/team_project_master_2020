package view;

// Controller class for plot.fxml
// by Julia

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ViewPlot {

    @FXML
    private TabPane tabPanePlots;

    @FXML
    private Tab tabGcCoverage;

    @FXML
    private Tab tabElse;

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot) {
        this.tabGcCoverage.setContent(gcCoveragePlot);
    }
}
