

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
This is my main method to test, constructed by anna and antonia.

Latest update: I am getting  error: package edu.uci.ics.jung.graph does not exist and error cannot find symbol for
myVertex and myEdge...
*/


public class MainParserGraph {

    public static UndirectedSparseGraph<myVertex, myEdge> readFile(String file) throws IOException {
        FileReader fr = new FileReader(file); // can be changed into not hard coded if needed
        BufferedReader br = new BufferedReader(fr);
        String line = ""; // will hold the current reading line
        String ID, sequence; // will hold the sequence information
        String eSource, eDestination; // will hold the edge information
        int countS = 0; // could be useful in the future to know how many sequences there are in total
        int countE = 0; // total count edges


        UndirectedSparseGraph<myVertex, myEdge> graph = new UndirectedSparseGraph<myVertex, myEdge>();

        while (( line = br.readLine() ) != null) {
            if (line.startsWith("S")) { // lines with S are segment lines (= sequences)
                countS++;
                String[] seqList = line.split("\t"); // holds the sequence ID and the sequence
                ID = seqList[1];
                sequence = seqList[2];
                boolean notInGraph=true;


                //check if new vertex already in graph
                for (myVertex v: graph.getVertices()){
                    if (v.getID().equals(ID)){
                        v.setSequence(sequence);
                        notInGraph = false;
                    }
                }

                //if not in graph, add new vertex to graph
                if (notInGraph){
                    graph.addVertex(new myVertex(ID, sequence));
                }


            } else if (line.startsWith("L")) { // lines with L are link lines (= edges)
                countE++;
                String[] edgList = line.split("\t"); // holds the edges [start node][end node]
                eSource = edgList[1];
                eDestination = edgList[3];
                System.out.println("overlapping: " + eSource + " -> " + eDestination);

                //myEdge only defined by the graph its in
                myEdge currentEdge = new myEdge(graph);
                myVertex vSource = null;
                myVertex vDestination = null;

                //check if source and destination Vertex of Edge already in graph
                for (myVertex v: graph.getVertices()){
                    if (v.getID().equals(eSource)){
                        vSource=v;
                    }
                    if (v.getID().equals(eDestination)){
                        vDestination=v;
                    }
                }

                //construct vertex, if not already found in graph
                if (vSource==null){ new myVertex(eSource);};
                if (vDestination==null){ new myVertex(eDestination);};

                //add Edge with source and destination Vertex to graph
                graph.addEdge(currentEdge, new Pair<myVertex>(vSource, vDestination));
            }
        }
        //System.out.println("total sequence count: " + countS + "\ntotal edge count: " + countE);
        br.close();
        fr.close();
        return graph;
    }


    public static void main(String[] args) throws IOException {

        //GFAparser parser = new GFAparser();

        UndirectedSparseGraph<myVertex, myEdge> parsedGraph;
        parsedGraph = readFile(args[0]);

        System.out.println(parsedGraph);

    }
}
