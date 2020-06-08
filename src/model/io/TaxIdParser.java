package model.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class TaxIdParser {

    public static void main(String[] args) throws IOException{
        if(args.length!=1){throw new IOException("Enter single file with contig ID and taxonomic ID!");}

        File file = new File(args[0]);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        String line;

        while((line = br.readLine())!= null){
            String[] temp = line.split("\t");
            if(temp.length!=2){throw new IOException("Please use requested format!");}

            String conID = temp[0];
            Integer taxID = Integer.valueOf(temp[1]);

            map.put(conID, taxID);
        }

        br.close();

        for(String s : map.keySet()){
            System.out.println("Contig ID: " +  s + "\t" + "Taxonomic ID: " + map.get(s));
        }
    }
}