package view;

// Controller class for plot.fxml
// by Julia

import javafx.fxml.FXML;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class ViewPlot {

    @FXML
    private TabPane tabPanePlots;

    @FXML
    private Tab tabGcCoverage;

    @FXML
    private Tab tabElse;

    @FXML
    private Slider nodeSizeManualSliderPlot;

    @FXML
    private RadioButton nodeSizeManualRadioButtonPlot;

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot) {
        this.tabGcCoverage.setContent(gcCoveragePlot);
    }

    public Slider getNodeSizeManualSliderPlot() {
        return nodeSizeManualSliderPlot;
    }

    public RadioButton getNodeSizeManualRadioButtonPlot() {
        return nodeSizeManualRadioButtonPlot;
    }
}
