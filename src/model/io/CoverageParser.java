package model.io;
/*
Parser for the coverage of the contigs
needs the path to the file containing contig IDs and coverage
needs the graph and adds the coverage information as new property to the vertices
by Julia
 */

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CoverageParser {

    public CoverageParser(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path) throws IOException {
        parseCoverage(graph, path);
    }

    private void parseCoverage(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] pair = line.split("\t");
            if (pair.length != 2) {
                throw new IOException("Please use requested format!");
            }
            String conID = pair[0];
            String coverage = pair[1];

            // Comparing the IDs of the vertices to the contig ID in this line of the file
            for (MyVertex v : graph.getVertices()) {
                if (v.getID().equals(conID)) {
                    v.addProperty(ContigProperty.COVERAGE, Double.parseDouble(coverage));
                }
            }
        }
        br.close();
    }
}