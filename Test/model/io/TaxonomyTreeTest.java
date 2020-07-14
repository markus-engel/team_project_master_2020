package model.io;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TaxonomyTreeTest {

    TaxonomyTree T = new TaxonomyTree();

    TaxonomyTreeTest() throws IOException {
    }

    @Test
    void getTree() {
    }

    @Test
    void getTaxNode() {
    }

    @Test
    void getAncestorId() {
        assertEquals(0, T.getAncestorId(0));
        System.out.println(T.getScientificName(4));

        System.out.println(T.getScientificName(1));
        System.out.println(T.getScientificName(2));
    }

    @Test
    void getAncestorName() {
    }

    @Test
    void testGetAncestorId() {
    }

    @Test
    void testGetAncestorName() {
    }

    @Test
    void getScientificName() {
    }

    @Test
    void getRank() {
    }
}