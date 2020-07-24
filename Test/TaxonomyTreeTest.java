//import model.io.TaxonomyTree;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class TaxonomyTreeTest {
//
////    TaxonomyTree T = new TaxonomyTree();
////
////    TaxonomyTreeTest() throws IOException {
////    }
//
//    static TaxonomyTree T;
//    @BeforeAll
//    public static void init(){
//        try {
//            T = new TaxonomyTree();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void getAncestorId() {
//        assertEquals(0, T.getAncestorId(1));
//        assertEquals(1, T.getAncestorId(2));
//        assertEquals(1, T.getAncestorId(3));
//        assertEquals(2, T.getAncestorId(4));
//        assertEquals(2, T.getAncestorId(5));
//        assertEquals(2, T.getAncestorId(6));
//        assertEquals(4, T.getAncestorId(10));
//        assertEquals(4, T.getAncestorId(11));
//        assertEquals(5, T.getAncestorId(12));
//        assertEquals(5, T.getAncestorId(13));
//        assertEquals(6, T.getAncestorId(14));
//        assertEquals(6, T.getAncestorId(15));
//    }
//
//    @Test
//    void getAncestorName() {
//        assertEquals("root_0", T.getAncestorName(1));
//        assertEquals("lvl A_1", T.getAncestorName(2));
//        assertEquals("lvl A_1", T.getAncestorName(3));
//        assertEquals("lvl B_2", T.getAncestorName(4));
//        assertEquals("lvl B_2", T.getAncestorName(5));
//        assertEquals("lvl B_2", T.getAncestorName(6));
//        assertEquals("lvl C_4", T.getAncestorName(10));
//        assertEquals("lvl C_4", T.getAncestorName(11));
//        assertEquals("lvl C_5", T.getAncestorName(12));
//        assertEquals("lvl C_5", T.getAncestorName(13));
//        assertEquals("lvl C_6", T.getAncestorName(14));
//        assertEquals("lvl C_6", T.getAncestorName(15));
//    }
////    test for id and rank
//    @Test
//    void testGetAncestorId() {
////        assertEquals(0, T.getAncestorId(1, "kingdom"));
//        assertEquals(1, T.getAncestorId(2, "kingdom"));
//        assertEquals(1, T.getAncestorId(3, "kingdom"));
//        assertEquals(2, T.getAncestorId(4, "phylum"));
//        assertEquals(2, T.getAncestorId(5, "phylum"));
//        assertEquals(2, T.getAncestorId(6, "phylum"));
//        assertEquals(4, T.getAncestorId(10, "class"));
//        assertEquals(4, T.getAncestorId(11, "class"));
//        assertEquals(5, T.getAncestorId(12, "class"));
//        assertEquals(5, T.getAncestorId(13, "class"));
//        assertEquals(6, T.getAncestorId(14, "class"));
//        assertEquals(6, T.getAncestorId(15, "class"));
//    }
//
//    @Test
//    void testGetAncestorName() {
////        assertEquals("root_0", T.getAncestorName(1, "kingdom"));
//        assertEquals("lvl A_1", T.getAncestorName(2, "kingdom"));
//        assertEquals("lvl A_1", T.getAncestorName(3, "kingdom"));
//        assertEquals("lvl B_2", T.getAncestorName(4, "phylum"));
//        assertEquals("lvl B_2", T.getAncestorName(5, "phylum"));
//        assertEquals("lvl B_2", T.getAncestorName(6, "phylum"));
//        assertEquals("lvl C_4", T.getAncestorName(10, "class"));
//        assertEquals("lvl C_4", T.getAncestorName(11, "class"));
//        assertEquals("lvl C_5", T.getAncestorName(12, "class"));
//        assertEquals("lvl C_5", T.getAncestorName(13, "class"));
//        assertEquals("lvl C_6", T.getAncestorName(14, "class"));
//        assertEquals("lvl C_6", T.getAncestorName(15, "class"));
//    }
//
//    @Test
//    void getScientificName() {
//        assertEquals("root_0", T.getScientificName(0));
//        assertEquals("lvl A_1", T.getScientificName(1));
//        assertEquals("lvl B_2", T.getScientificName(2));
//        assertEquals("lvl B_3", T.getScientificName(3));
//        assertEquals("lvl C_4", T.getScientificName(4));
//        assertEquals("lvl C_5", T.getScientificName(5));
//        assertEquals("lvl C_6", T.getScientificName(6));
//        assertEquals("lvl D_10", T.getScientificName(10));
//        assertEquals("lvl D_11", T.getScientificName(11));
//        assertEquals("lvl D_12", T.getScientificName(12));
//        assertEquals("lvl D_13", T.getScientificName(13));
//        assertEquals("lvl D_14", T.getScientificName(14));
//        assertEquals("lvl D_15", T.getScientificName(15));
//    }
//
//    @Test
//    void getRank() {
//        assertEquals("kingdom", T.getRank(1));
//        assertEquals("phylum", T.getRank(2));
//        assertEquals("phylum", T.getRank(3));
//        assertEquals("class", T.getRank(4));
//        assertEquals("class", T.getRank(5));
//        assertEquals("class", T.getRank(6));
//        assertEquals("order", T.getRank(10));
//        assertEquals("order", T.getRank(11));
//        assertEquals("order", T.getRank(12));
//        assertEquals("order", T.getRank(13));
//        assertEquals("order", T.getRank(14));
//        assertEquals("order", T.getRank(15));
//    }
//}