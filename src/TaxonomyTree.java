import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class TaxonomyTree {

        public static void main( String[] args ) throws IOException
        {
            String filePath = "C:/Users/nnkar/Downloads/taxdmp/nodes.dmp";
            HashMap<Integer,Integer> map = new HashMap<Integer, Integer>();


            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));

            while ((line = reader.readLine()) != null) {
                line = line.replace("\t", "");
                line = line.replace("|", ":");
                String[] parts = line.split(":");

                for (int i=0; i <= 2; i++)
                {
                    Integer key = Integer.parseInt(parts[0]);
                    Integer value = Integer.parseInt(parts[1]);
                    map.put(key, value);
                }

                System.out.print(map);







                ;
            }}}







