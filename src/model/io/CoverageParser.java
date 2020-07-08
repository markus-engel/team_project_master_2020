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
        int lineCount = 0; // line counter
        int count = 0;
        int countNoMatch = 0;
        while ((line = br.readLine()) != null) {
            // first line contains headline and should be skipped
            if (lineCount > 0) {
                String[] pair = line.split("\t");
                if (pair.length != 2) {
                    throw new IOException("Please use requested format!");
                }
                String conID = pair[0];
                double coverage = Double.parseDouble(pair[1]);

                // Comparing the IDs of the vertices to the contig ID in this line of the file
                for (MyVertex v : graph.getVertices()) {
                    if (v.getIDprop().equals(conID)) {
                        v.addProperty(ContigProperty.COVERAGE, coverage);
                        count++;
                    } else countNoMatch++;
                }
            }
            lineCount++;
        }
        //System.out.println("Number of parsed coverages: "+count);
        //System.out.println("contig not found: "+countNoMatch);
        //System.out.println("Numer of lines in COV file: "+lineCount);
        br.close();
    }
}