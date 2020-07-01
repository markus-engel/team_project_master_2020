package model.io;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import javafx.beans.property.SimpleStringProperty;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

/*
This is the class containing the readFile method to construct a graph from a GFA file.
Constructed by Anna and Antonia.
Modified by Anna: change values to Object properties to use them in myVertex
*/

public class GraphParser {

    public static UndirectedSparseGraph<MyVertex, MyEdge> readFile(String file) throws IOException {
        FileReader fr = new FileReader(file); // can be changed into not hard coded if needed
        BufferedReader br = new BufferedReader(fr);
        String line = ""; // will hold the current reading line
        String ID, sequence; // will hold the sequence information
        String eSource, eDestination; // will hold the edge information
        int countS = 0; // could be useful in the future to know how many sequences there are in total
        int countE = 0; // total count edges

        HashMap<String, MyVertex> vertices = new HashMap<>(); //Hashmap collecting all vertices added to graph, with ID as key, model.graph.MyVertex as object
        UndirectedSparseGraph<MyVertex, MyEdge> graph = new UndirectedSparseGraph<MyVertex, MyEdge>(); //UndirectedSparseGraph readfile returns

        while (( line = br.readLine() ) != null) {
            if (line.startsWith("S")) { // lines with S are segment lines (= sequences)
                countS++;
                String[] seqList = line.split("\t"); // holds the sequence ID and the sequence
                ID = seqList[1];
                sequence = seqList[2];
                boolean notInGraph=true;


                //check if new vertex already in graph
                for (MyVertex v: graph.getVertices()){
                    if (v.getIDpropProperty().equals(ID)){
                        v.setSequenceprop(sequence);
                        notInGraph = false;
                    }
                }

                //if not in graph, add new vertex to graph
                if (notInGraph){
                    graph.addVertex(new MyVertex(new SimpleStringProperty(ID), new SimpleStringProperty(sequence)));
                    vertices.put(ID, new MyVertex(new SimpleStringProperty(ID), new SimpleStringProperty(sequence)));
                }

            } else if (line.startsWith("L")) { // lines with L are link lines (= edges)
                countE++;
                String[] edgList = line.split("\t"); // holds the edges [start node][end node]
                eSource = edgList[1];
                eDestination = edgList[3];

                MyEdge currentEdge = new MyEdge(graph);      //myEdge only defined by its graph
                MyVertex vSource;                           //source vertex
                MyVertex vDestination;                      //destination vertex

                //get the vertices of the IDs
                vSource = vertices.get(eSource);
                vDestination = vertices.get(eDestination);

                //construct vertex, if not found in Hashmap vertices
                if (vSource==null){ new MyVertex(new SimpleStringProperty(eSource));};
                if (vDestination==null){ new MyVertex(new SimpleStringProperty(eDestination));};

                //add Edge with source and destination Vertex to graph
                graph.addEdge(currentEdge, vSource, vDestination);
            }
        }
        br.close();
        fr.close();

        return graph;
    }
}