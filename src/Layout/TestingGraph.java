package Layout;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.awt.*;
import java.awt.geom.Point2D;


public class TestingGraph {


        public static void main(String[] args){
            UndirectedSparseGraph<MyVertex, MyEdge> graph;
            graph = makeGraph();

            FRLayout<MyVertex,MyEdge> layout = new FRLayout<MyVertex,MyEdge>(graph);
            Layout<MyVertex, MyEdge> layoutKK = new KKLayout<MyVertex, MyEdge>(graph);

            layoutKK.initialize();
            layout.initialize();
            //layout.setAttractionMultiplier(3);
            layout.setSize(new Dimension(20,10 ));
            layoutKK.setSize(new Dimension(20,10));





            for (MyVertex v: graph.getVertices()){

                Point2D p= layout.apply(v);
                Point2D p2 = layoutKK.apply(v);


                System.out.println( " x " + " "+p.getX());
                System.out.println( " y " + " "+p.getY());

            }



            //VisualizationViewer<MyVertex, MyEdge> vv =  new VisualizationViewer<String,Integer>(layout, new Dimension(600,600));

            System.out.println(graph.toString());

        }

        public static UndirectedSparseGraph<MyVertex, MyEdge> makeGraph(){

            UndirectedSparseGraph<MyVertex, MyEdge> graph = new UndirectedSparseGraph<MyVertex, MyEdge>();

            StringProperty first = new SimpleStringProperty("HAHAHA");

            MyVertex firstVector = new MyVertex(new SimpleStringProperty("first"), new SimpleStringProperty("ABCD"));
            MyVertex secondVector = new MyVertex(new SimpleStringProperty("second"), new SimpleStringProperty("BBBB"));
            MyVertex thirdVector = new MyVertex(new SimpleStringProperty("third"),new SimpleStringProperty("CCCC"));
            MyVertex fourthVector = new MyVertex(new SimpleStringProperty("fourth"), new SimpleStringProperty("DDDD"));
            //String[] vectorIds = {"first", "second", "third", "fourth"};
            //String[] vectorSeq = {"A", "B","C"};


            //ich gebe in Edge jetzt nur den graphen ein, da UndirectedSparseGraph eine Edge aus Obj. und Pair
            // of Vertices/Endpoints erstellt
            //MyEdge myEdge = new MyEdge(graph);

            //for (String ID:vectorIds){
            //MyVertex currentVector = new MyVertex(ID);
            //graph.addVertex(currentVector);
            //}
            //I cannot seem to access the attributes of my Vertex. this is bad. why not? i cant even to find the object with specific stuff.
            graph.addVertex(firstVector);
            graph.addVertex(secondVector);
            graph.addVertex(thirdVector);
            graph.addVertex(fourthVector);

            graph.addEdge(new MyEdge(graph, new Pair<>(firstVector, secondVector)), firstVector, secondVector);
            graph.addEdge(new MyEdge(graph, new Pair<>(firstVector, secondVector)),firstVector, thirdVector);
            graph.addEdge(new MyEdge(graph, new Pair<>(firstVector, fourthVector)), firstVector, fourthVector);
            graph.addEdge(new MyEdge(graph, new Pair<>(secondVector, thirdVector)), secondVector, thirdVector);

            return graph;

        }
    }


