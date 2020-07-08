package view;

// by Julia

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.ScatterChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewPlot {

    private Stage thisStage;

    private final View view;

    @FXML
    private TabPane tabPanePlots;

    @FXML
    private Tab tabGcCoverage;

    @FXML
    private ScatterChart<Number, Number> gcCoveragePlot;

    @FXML
    private Tab tabElse;

    public ViewPlot(View view) throws IOException {
        this.view = view;
        thisStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../plot.fxml"));
        Parent root = loader.load();

        //loader.setController(this);

        // Load the scene
        thisStage.setScene(new Scene(root));

        // Setup the window/stage
        thisStage.setTitle("Plots hopefully");

    }

    public void showStage() {
        thisStage.showAndWait();
    }

    public ScatterChart<Number, Number> getgcCoveragePlot() {
        return gcCoveragePlot;
    }

    public void setGcPlot(ScatterChart<Number, Number> gcCoveragePlot) {
        this.gcCoveragePlot = gcCoveragePlot;
        this.gcCoveragePlot.setVisible(true);
    }

}
