import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.graph.MyEdge;
import model.graph.MyVertex;
import model.io.ContigProperty;
import model.io.GraphParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

/*
Test class for the GraphParser
by Julia
 */

class GraphParserTest {

    @Test
    void readFile() throws IOException {
        UndirectedSparseGraph<MyVertex, MyEdge> graph = GraphParser.readFile(getClass().getClassLoader().getResource("gfaTestFile.gfa").getFile());
        assertEquals(4, graph.getVertices().size());
        assertEquals(2, graph.getEdges().size());
        for (MyVertex v : graph.getVertices()) {
            if (v.getID().equals("contig1")) {
                assertEquals(1.0, v.getProperty(ContigProperty.GC));
                assertEquals(8, v.getProperty(ContigProperty.LENGTH));
                assertEquals("GCCGGGcg", v.getSequenceprop());
            }
            if (v.getID().equals("contig2")) {
                assertEquals(0.0, v.getProperty(ContigProperty.GC));
                assertEquals(31, v.getProperty(ContigProperty.LENGTH));
                assertEquals("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", v.getSequenceprop());
            }
            if (v.getID().equals("contig3")) {
                assertEquals(0.5, v.getProperty(ContigProperty.GC));
                assertEquals(4, v.getProperty(ContigProperty.LENGTH));
                assertEquals("ACCT", v.getSequenceprop());
            }
            if (v.getID().equals("contig4")) {
                assertEquals(0.0, v.getProperty(ContigProperty.GC));
                assertEquals(14, v.getProperty(ContigProperty.LENGTH));
                assertEquals("ATTTATTATTTATT", v.getSequenceprop());
            }
        }
        for (MyEdge e : graph.getEdges()) {
            assertTrue(graph.getVertices().contains(e.getFirst()));
            assertTrue(graph.getVertices().contains(e.getSecond()));
        }
    }

    @Test
    void calculateGCcontent() {
        assertEquals(1.0, GraphParser.calculateGCcontent("GCCGGGcg"));
        assertEquals(0.0, GraphParser.calculateGCcontent("AAAAAAAAAAAAAAAAAAAAAAAAA"));
        assertEquals(0.1, GraphParser.calculateGCcontent("ATTATGTAAT"));
        assertEquals(0.0, GraphParser.calculateGCcontent(""));
    }

    /*
    @Test
    void getVertexFromMapOrNew() {
    }
     */
}