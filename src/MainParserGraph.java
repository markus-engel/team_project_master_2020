import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
This is my main method to test, constructed by antonia.
*/


public class MainParserGraph {

    public static UndirectedSparseGraph readFile(String file) throws IOException {
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

                myVertex currentVertex = new myVertex(ID, sequence);
                graph.addVertex(currentVertex);


            } else if (line.startsWith("L")) { // lines with L are link lines (= edges)
                countE++;
                String[] edgList = line.split("\t"); // holds the edges [start node][end node]
                eSource = edgList[1];
                eDestination = edgList[3];
                System.out.println("overlapping: " + eSource + " -> " + eDestination);

                // need to add vertex in addEdge method but the edge is a String ID.
                myEdge currentEdge = new myEdge(graph);
                graph.addEdge(currentEdge, new Pair<myVertex>(eSource, eDestination));
            }
        }
        //System.out.println("total sequence count: " + countS + "\ntotal edge count: " + countE);
        br.close();
        fr.close();
        return graph;
    }


    public static void main(String[] args) throws IOException {

        GFAparser parser = new GFAparser();
        //UndirectedSparseGraph<myVertex, myEdge> parsedGraph;
        parsedGraph = parser.readFile("path");

    }
}
