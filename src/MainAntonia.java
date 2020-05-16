import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

/*
This is my main method to test, constructed by antonia.
*/


public class MainAntonia {

    public static void main(String[] args) {
        UndirectedSparseGraph<myVertex, myEdge> parsedGraph;

        //das sollte fast reichen, oder? wenn man readfile static macht? wir könnten args[0] noch als argument
        // für die zu lesende datei mitgeben
        parsedGraph = readFile();


        //folgendes soll im parser erstellt werden, und dann soll der Parser einen UndirectedSparseGraph rausgeben.
        UndirectedSparseGraph<myVertex, myEdge> graph = new UndirectedSparseGraph<myVertex, myEdge>();
        myVertex firstVector = new myVertex("first", "ABCD");
        myVertex secondVector = new myVertex("second", "BBBB");

        //ich gebe in Edge jetzt nur den graphen ein, da UndirectedSparseGraph eine Edge aus Obj. und Pair
        // of Vertices/Endpoints erstellt
        myEdge myEdge = new myEdge(graph);

        graph.addEdge(myEdge, new Pair<myVertex>(firstVector, secondVector));
        graph.addVertex(firstVector);
        graph.addVertex(secondVector);

    }
}
