// GFA File parser: current variant simply prints the parsed information

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
        Boolean revCompSource, revCompDest; // can be used later: the file tells if the edges are overlapping with the reverse complement part
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

                // check if the overlapping sequence has to be reversed
                // alternative: + and - indicators can be safed in a String variable
                if (edgList[2] == "+") {
                    revCompSource = false; // if the direction is +, no need for reverse complement
                } else {
                    revCompSource = true;
                }
                if (edgList[4] == "+") {
                    revCompSource = false; // if the direction is +, no need for reverse complement
                } else {
                    revCompSource = true;
                }
            }
        }
        System.out.println("total sequence count: " + countS + "\ntotal edge count: " + countE);
        br.close();
        fr.close();
    }

    public static void main(String[] args) throws IOException {
        GFAparser parser = new GFAparser();
        parser.readFile("D:/Master/Sem 2/Teamprojekt/jeon2n3_miniasm.gfa");
    }
}