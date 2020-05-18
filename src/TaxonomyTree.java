import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;

public class TaxonomyTree {

        public static void main( String[] args ) throws IOException
        {
            String filePath = "C:/Users/nnkar/Downloads/taxdmp/nodes.dmp";
            HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();

            String line;
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split("|", 2);
                if (parts.length >= 2)
                {
                    Int key = parts[0];
                    String value = parts[1];
                    map.put(key, value);
                } else {
                    System.out.println("ignoring line: " + line);
                }
            }

            for (String key : map.keySet())
            {
                System.out.println(key + ":" + map.get(key));
            }
            reader.close();
        }
    }






