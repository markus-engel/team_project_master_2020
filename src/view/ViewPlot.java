package view;

// Controller class for plot.fxml
// by Julia

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
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
    private Tab tabContigLengthDistribution;

    @FXML
    private Slider nodeSizeManualSliderPlot;

    @FXML
    private RadioButton nodeSizeManualRadioButtonPlot;

    @FXML
    private RadioButton nodeSizeDefaultRadioButtonPlot;


    public TabPane getTabPanePlots() {
        return tabPanePlots;
    }

    public void setTabPanePlots(TabPane tabPanePlots) {
        this.tabPanePlots = tabPanePlots;
    }

    public Tab getTabGcCoverage() {
        return tabGcCoverage;
    }

    public void setTabGcCoverage(Tab tabGcCoverage) {
        this.tabGcCoverage = tabGcCoverage;
    }

    public Tab getTabElse() {
        return tabElse;
    }

    public Tab getTabContigLengthDistribution(){return tabContigLengthDistribution;}

    public void setTabContigLengthDistribution(Tab tabContigLengthDistribution){this.tabContigLengthDistribution = tabContigLengthDistribution;}

    public Slider getNodeSizeManualSliderPlot() {
        return nodeSizeManualSliderPlot;
    }

    public RadioButton getNodeSizeManualRadioButtonPlot() {
        return nodeSizeManualRadioButtonPlot;
    }

    public RadioButton getNodeSizeDefaultRadioButtonPlot() {
        return nodeSizeDefaultRadioButtonPlot;
    }

    public void setTabElse(Tab tabElse) {
        this.tabElse = tabElse;
    }

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot, Tab tab) { tab.setContent(gcCoveragePlot);
    }

    public void setCDPlot(BarChart<String, Number> cdPlot, Tab tab){tab.setContent(cdPlot);}
}
