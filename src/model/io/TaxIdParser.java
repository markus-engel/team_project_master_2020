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

public class TaxIdParser {

    //private final HashMap<String, Integer> contigToTaxID = new HashMap<>();
    UndirectedSparseGraph<MyVertex, MyEdge> graph;
    String path;
    TaxonomyTree tree;

    public TaxIdParser(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path, TaxonomyTree tree) throws IOException {
        this.graph = graph;
        this.path = path;
        this.tree = tree;
        parseTaxIDs(graph, path, tree);
    }

    // Parser of the taxonomic IDs matching the contig IDs of vertices in the graph
    private void parseTaxIDs(UndirectedSparseGraph<MyVertex, MyEdge> graph, String path, TaxonomyTree tree) throws IOException {

        // Thomas' original code from main method
        /*if(args.length!=1){throw new IOException("Enter single file with contig ID and taxonomic ID!");}

        File file = new File(args[0]);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        */

        //File file = new File(getClass().getClassLoader().getResource("model/io/jeon2n3_miniasm.r2c.txt").getFile());
        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        while ((line = br.readLine()) != null) {
            String[] temp = line.split("\t");
            if (temp.length != 2) {
                throw new IOException("Please use requested format!");
            }

            String conID = temp[0];
            Integer taxID = Integer.valueOf(temp[1]);
            // Comparing the IDs of the vertices to the contig ID in this line of the file
            for (MyVertex v : graph.getVertices()) {
                if (v.getIDpropProperty().toString().equals(conID)) {
                    v.addProperty("taxonomy", tree.getTaxNode(taxID));
                    v.addProperty("taxID", taxID);
                }
            }
            //contigToTaxID.put(conID, taxID);
        }
        br.close();
    }
}

/*
    public HashMap<String, Integer> getContigToTaxID() {
        return contigToTaxID;
    }

    // Returns the taxonomic ID of a given contig ID
    public int getTaxID(String contigID) {
        return contigToTaxID.get(contigID);
    }

    // for testing only: Prints all pairs of contig and taxonomic IDs
    public static void main(String[] args) throws IOException {
        TaxIdParser taxIDs = new TaxIdParser();

        for (String s : taxIDs.getContigToTaxID().keySet()) {
            System.out.println("Contig ID: " + s + "\t" + "Taxonomic ID: " + taxIDs.getContigToTaxID().get(s));
        }
    }
}*/
