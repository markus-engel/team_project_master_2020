package Layout;

import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;

import model.graph.MyVertex;
import model.io.GraphParser;

public class TestingLayout extends Application {

    UndirectedSparseGraph<MyVertex, MyEdge> graph;

    @Override
    public void start(Stage primaryStage) throws IOException {
        //BorderPane root = new BorderPane();

        //graph = TestingGraph.makeGraph();

        graph = GraphParser.readFile("/Users/antonia/IdeaProjects/2020teamproject/src/jeon2n3_miniasm.gfa");

        //Scene scene = new Scene(root, 1024, 768);
        //scene.setFill(Paint.valueOf("white"));
        //scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());


        // create a canvas
        Canvas canvas = new Canvas(1000.0f, 750.0f);


        // graphics context
        GraphicsContext graphics_context =canvas.getGraphicsContext2D();

        FRLayout<MyVertex, MyEdge> layout = new FRLayout<MyVertex,MyEdge>(graph);
        Layout<MyVertex, MyEdge> layoutKK = new KKLayout<MyVertex, MyEdge>(graph);
        Layout<MyVertex, MyEdge> circle = new CircleLayout<>(graph);

        layoutKK.initialize();
        layout.initialize();
        circle.initialize();
        //layout.setAttractionMultiplier(3);
        layout.setSize(new Dimension(950,650 ));
        layoutKK.setSize(new Dimension(950,650));
        circle.setSize(new Dimension(950,650));
        layout.setMaxIterations(10000);
        layout.setRepulsionMultiplier(50);
        layout.setAttractionMultiplier(10);





        double colour= 0;

        //go through vertices and add circles for each vertex
        for (MyVertex v1:graph.getVertices()){
            Point2D p = layout.apply(v1);
            //layout.setLocation(v1,layout.getX(v1),layout.getY(v1)); //sets the location of the Vertex, in case we need it later on..

            //go through neighboring vertices and draw connecting edges
            for (MyVertex v2:graph.getPredecessors(v1)){
                Point2D endLine =layout.apply(v2);
                graphics_context.strokeLine(layout.getX(v1), layout.getY(v1), layout.getX(v2), layout.getY(v2));
            }
            
            graphics_context.setFill(Color.RED);
            graphics_context.fillOval(layout.getX(v1)-5, layout.getY(v1)-5, 10, 10);
            graphics_context.fillText(v1.getID(),layout.getX(v1)+10,layout.getY(v1));


            colour=colour+0.15;
        }

        //Node view = new Rectangle();
        Group group = new Group(canvas);


        Scene scene = new Scene(group, 1030, 750);
        // scene.setFill(Paint.valueOf("white"));

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

