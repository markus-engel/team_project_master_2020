package previousWork;

/*
This is the first draft. For current version see MainParserGraph.
This variant simply prints the parsed information from a gfa file
From Anna (if you have questions)
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


class GFAparser {

    public static void readFile(String file) throws IOException {
        FileReader fr = new FileReader(file); // can be changed into not hard coded if needed
        BufferedReader br = new BufferedReader(fr);
        String line = ""; // will hold the current reading line
        String ID, sequence; // will hold the sequence information
        String eSource, eDestination; // will hold the edge information
        int countS = 0; // could be useful in the future to know how many sequences there are in total
        int countE = 0; // total count edges

        while (( line = br.readLine() ) != null) {
            if (line.startsWith("S")) { // lines with S are segment lines (= sequences)
                countS++;
                String[] seqList = line.split("\t"); // holds the sequence ID and the sequence
                ID = seqList[1];
                sequence = seqList[2];
                System.out.println(ID + ": " + sequence);

            } else if (line.startsWith("L")) { // lines with L are link lines (= edges)
                countE++;
                String[] edgList = line.split("\t"); // holds the edges [start node][end node]
                eSource = edgList[1];
                eDestination = edgList[3];
                System.out.println("overlapping: " + eSource + " -> " + eDestination);
            }
        }
        //System.out.println("total sequence count: " + countS + "\ntotal edge count: " + countE);
        br.close();
        fr.close();
    }

    //TODO: delete if not necessary anymore (Caner)
    public static void main(String[] args) throws IOException {
        GFAparser parser = new GFAparser();
        parser.readFile("D:/Master/Sem 2/Teamprojekt/jeon2n3_miniasm.gfa");
    }
}
