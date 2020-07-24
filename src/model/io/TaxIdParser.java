package model.io;
/*
Parser for taxonomic IDs of contigs
adds the taxIDs (provided in a file) and the corresponding nodes from the taxonomy tree
as new properties to the vertices in the given graph
by Thomas and Julia
 */

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import model.graph.MyEdge;
import model.graph.MyVertex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

public class TaxIdParser {

    UndirectedSparseGraph<MyVertex, MyEdge> graph;
    String path;
    TaxonomyTree tree;
    private TreeSet taxons;

    public TaxIdParser(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path, TaxonomyTree tree, TreeSet taxons) throws IOException {
        this.graph = graph;
        this.path = path;
        this.tree = tree;
        this.taxons = taxons;
        parseTaxIDs(graph, path, tree, taxons);
    }

    // Parser of the taxonomic IDs matching the contig IDs of vertices in the graph
    private void parseTaxIDs(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path, TaxonomyTree tree, TreeSet taxons) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] temp = line.split("\t");
            if (temp.length != 2) {
                throw new IOException("Please use requested format!");
            }

            String conID = temp[0];
            int taxID = Integer.parseInt(temp[1]);
            taxons.add(taxID);
            // Comparing the IDs of the vertices to the contig ID in this line of the file
            for (MyVertex v : graph.getVertices()) {
                if (v.getIDpropProperty().toString().equals(conID)) {
                    v.addProperty(ContigProperty.TAXONOMY, tree.getTaxNode(taxID));
                }
            }
        }
        br.close();
    }
}
