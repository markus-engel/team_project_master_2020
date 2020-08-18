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
    private Tab tabSelection;

    @FXML
    private Tab tabContigLengthDistribution;

    @FXML
    private Slider nodeSizeManualSliderPlot;

    @FXML
    private RadioButton nodeSizeManualRadioButtonPlot;

    @FXML
    private RadioButton nodeSizeDefaultRadioButtonPlot;

    @FXML
    private RadioButton coloringTaxonomyRadioButton;

    @FXML
    private RadioButton coloringDefaultRadioButton;


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

    public Tab getTabSelection() {
        return tabSelection;
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

    public RadioButton getColoringTaxonomyRadioButton () {
        return coloringTaxonomyRadioButton;
    }

    public RadioButton getColoringDefaultRadioButton () {
        return coloringDefaultRadioButton;
    }

    public void setTabSelection(Tab tabElse) {
        this.tabSelection = tabElse;
    }

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot, Tab tab) { tab.setContent(gcCoveragePlot);
    }

    public void setCDPlot(BarChart<String, Number> cdPlot, Tab tab){tab.setContent(cdPlot);}
}
