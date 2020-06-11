package model.io;
/*
Parser for taxonomic and contig IDs
provides a method to get the taxonomic ID of a given contig ID
by Thomas and Julia
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TaxIdParser {

    private final HashMap<String, Integer> contigToTaxID = new HashMap<>();

    public TaxIdParser() throws IOException {
        parseTaxIDs();
    }

    // Parser of the taxonomic IDs matching the contig IDs from jeon2n3_miniasm.r2c.txt (resources/model/io)
    private void parseTaxIDs() throws IOException {

        // Thomas' original code from main method
        /*if(args.length!=1){throw new IOException("Enter single file with contig ID and taxonomic ID!");}

        File file = new File(args[0]);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        */

        File file = new File(getClass().getClassLoader().getResource("model/io/jeon2n3_miniasm.r2c.txt").getFile());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] temp = line.split("\t");
            if (temp.length != 2) {
                throw new IOException("Please use requested format!");
            }

            String conID = temp[0];
            Integer taxID = Integer.valueOf(temp[1]);

            contigToTaxID.put(conID, taxID);
        }
        br.close();
    }

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
}