package model.io;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.event.GraphEvent;
import edu.uci.ics.jung.graph.util.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
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
Modified by Julia: one bug fixed
*/

public class GraphParser {
    public static UndirectedSparseGraph<MyVertex, MyEdge> readFile(String file) throws IOException {
        FileReader fr = new FileReader(file); // can be changed into not hard coded if needed
        BufferedReader br = new BufferedReader(fr);
        String line = ""; // will hold the current reading line
        String ID, sequence; // will hold the sequence information
        String eSource, eDestination; // will hold the edge information
        int countS = 0; // could be useful in the future to know how many sequences & edges there are in total
        int countE = 0;


        HashMap<String, MyVertex> vertices = new HashMap<>(); //Hashmap collecting all vertices added to graph, with ID as key, model.graph.MyVertex as object
        UndirectedSparseGraph<MyVertex, MyEdge> graph = new UndirectedSparseGraph<>(); //UndirectedSparseGraph readfile returns

        while ((line = br.readLine()) != null) {
            if (line.startsWith("S")) { // lines with S are segment lines (= sequences)
                countS++;
                String[] seqList = line.split("\t"); // holds the sequence ID and the sequence
                ID = seqList[1];
                sequence = seqList[2];
                double finalGC = calculateGCcontent(sequence);

                // Add sequence and GC-content to existing vertex
                if (vertices.containsKey(ID)) {
                    for (MyVertex v : graph.getVertices()) {
                        if (v.getIDprop().equals(ID)) {
                            v.setSequenceprop(new SimpleStringProperty(sequence));
                            v.addProperty(ContigProperty.GC, finalGC);
                        }
                    }
                }

                //if not in graph, add new vertex to graph and map
                else {
                    MyVertex newVertex = new MyVertex(new SimpleStringProperty(ID), new SimpleStringProperty(sequence));
                    newVertex.addProperty(ContigProperty.GC, finalGC);
                    graph.addVertex(newVertex);
                    vertices.put(ID, newVertex);
                }

            } else if (line.startsWith("L")) { // lines with L are link lines (= edges)
                countE++;
                String[] edgList = line.split("\t"); // holds the edges [start node][end node]
                eSource = edgList[1];
                eDestination = edgList[3];


                MyVertex vSource = getVertexFromMapOrNew(eSource, vertices);     //source vertex
                MyVertex vDestination = getVertexFromMapOrNew(eDestination, vertices);     //destination vertex
                MyEdge currentEdge = new MyEdge(graph,new Pair<>(vSource, vDestination));      //myEdge only defined by its graph

                /*//get the vertices of the IDs
                vSource = vertices.get(eSource);
                vDestination = vertices.get(eDestination);

                //construct vertex, if not found in Hashmap vertices
                if (vSource==null){ vSource = new MyVertex(new SimpleStringProperty(eSource));};
                if (vDestination==null){ vDestination = new MyVertex(new SimpleStringProperty(eDestination));};
                */

                //add Edge with source and destination Vertex to graph
                graph.addEdge(currentEdge, vSource, vDestination);
                vertices.putIfAbsent(eSource, vSource);
                vertices.putIfAbsent(eDestination, vDestination);
            }
        }
        br.close();
        fr.close();

        /*System.out.println("Lines \"S\": " + countS);
        System.out.println("Lines \"L\": " + countE);
        System.out.println("Vertices in the graph: " + graph.getVertices().size());
        System.out.println("Edges in the graph: " + graph.getEdges().size());*/


        return graph;
    }


    public static double calculateGCcontent(String sequence) {
        int GCcount = 0;
        for (int i = 0; i < sequence.length(); i++) {
            if (sequence.charAt(i) == 'C' || sequence.charAt(i) == 'G' || sequence.charAt(i) == 'g' || sequence.charAt(i) == 'c') {
                GCcount++;
            }
        }
        return (double) GCcount / (double) sequence.length();
    }

    public static MyVertex getVertexFromMapOrNew(String vertexID, HashMap<String, MyVertex> vertices) {
        return vertices.getOrDefault(vertexID, new MyVertex(new SimpleStringProperty(vertexID)));
    }

}