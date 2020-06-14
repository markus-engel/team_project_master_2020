import model.io.TaxonomyTree;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeSet;

class TaxonomyTreeTest {
    // test files
    String fileNameNode = "model/io/JUnitTestMarkus.txt";
    String fileNames = "model/io/JUnitTestMarkusNames.txt";

    TreeSet<Integer> idArray = new TreeSet<>();

    TaxonomyTree T = new TaxonomyTree(fileNameNode, fileNames);

    TaxonomyTreeTest() throws IOException {
    }


//    // extract the information from the file into arrays for further processing
//    private void readContent(String fileNameNode) throws IOException {
//        String line;
//        File nodesShort = new File(getClass().getClassLoader().getResource(fileNameNode).getFile()); // consider arguments implementation for testing
//        BufferedReader reader = new BufferedReader(new FileReader(nodesShort));
//        while ((line = reader.readLine()) != null) {
//            String[] parts = line.split("\t");
//            int temp = Integer.parseInt(parts[0].trim());
//            idArray.add(temp);
//
//        }
//        reader.close();
//        for (Object i : idArray) {
//            System.out.println(i);
//        }
////        System.out.println();
//    }




    @Test
    void getParentId() {
    }

    @Test
    void testGetParentId() {
    }

    @Test
    void getRank() {
    }

    @Test
    void main() {
    }
}