package model.io;

/*
Taxonomic tree with access to the tree structure and the scientific names
The taxonomic ID of a contig (provided by TaxIdParser) is needed to access information of the tree
by Nasser and Julia
Comments for testing by Markus
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class TaxonomyTree {

    // The tree has taxonomic IDs as keys and Nodes as values
    // The Nodes themselves contain rank, scientific name, as well as Edges to parent and all children in the tree structure
    HashMap<Integer, Node> tree = new HashMap<>();

//    String fileName = "";

    public TaxonomyTree(String fileNameNode, String fileNames) throws IOException { // parameter inclusion line 85 args[0]

        if (fileNameNode == "" || fileNameNode == "model/io/nodesShort.dmp") {  //ME
            fileNameNode = "model/io/nodesShort.dmp";   //ME
        } else {    //ME
            fileNameNode = fileNameNode;    //ME
        }

        if (fileNames == "" || fileNames == "model/io/namesShort.txt") {  //ME
            fileNames = "model/io/namesShort.txt";   //ME
        } else {    //ME
            fileNames = fileNames;    //ME
        }


        parseNodes(fileNameNode);
        parseNames(fileNames);
    }

    // Node Parser parsing the taxonomic information from nodesShort.dmp to the tree
    private void parseNodes(String fileNameNode) throws IOException {
        String line;
        File nodesShort = new File(getClass().getClassLoader().getResource(fileNameNode).getFile()); // consider arguments implementation for testing
        BufferedReader reader = new BufferedReader(new FileReader(nodesShort));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
//            System.out.println(parts[0]);   //ME
//            System.out.println(parts[1]);   //ME
//            System.out.println(parts[2]);   //ME
            int childId = Integer.parseInt(parts[0]); //actual read node
            int parentId = Integer.parseInt(parts[1]);
            String childRank = parts[2]; // rank of the actual node

            Node child;
            // check, if this new child-node already exists as a parent-dummy (no rank nor parent-node yet)
            if (tree.containsKey(childId)) {
                child = tree.get(childId);
                child.setRank(childRank);
            }
            // if the child-node does not exist yet, it is created as a new node
            else child = new Node(childId, childRank);

            Node parent;
            // check, if parent-node exists and update its edges
            if (tree.containsKey(parentId)) {
                parent = tree.get(parentId);
                parent.addEdgeToChild(child);
            }
            // if the parent-node is not known yet, a dummy-parent is created with an edge to this child
            else {
                parent = new Node(parentId, child);
            }

            // Add/replace the parent-node in the tree
            tree.put(parentId, parent);

            // Add the the edge to the parent in the child-node and add/replace the child-node in the tree
            child.addEdgeToParent(parent);
            tree.put(childId, child);
        }
        reader.close();
    }

    // Name Parser parsing the scientific names of the nodes from namesShort.txt to the nodes in the tree
    private void parseNames(String fileNames) throws IOException {
        String line;
        File namesShort = new File(getClass().getClassLoader().getResource(fileNames).getFile()); // consider arguments implementation for testing
        BufferedReader reader = new BufferedReader(new FileReader(namesShort));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            int taxID = Integer.parseInt(parts[0]);
            String scientificName = parts[1];
            tree.get(taxID).setScientificName(scientificName);
        }
    }

    // Method to get the parent-node-id of a node
    public int getParentId(int nodeId) {
        return tree.get(nodeId).getParent().getId();
    }

    // Method to get the name of a node's parent
    public String getParentName(int nodeId) {
        return tree.get(nodeId).getParent().getScientificName();
    }

    // Method to get the parent-node-id of a specific rank of parent by going from parent to parent, e.g. order of 11
    public int getParentId(int nodeId, String rank) {
        Node current = tree.get(nodeId);
        while (!current.getRank().equals(rank) & tree.containsKey(current.getId())) {
            current = tree.get(this.getParentId(current.getId()));
        }
        if (current.getRank().equals(rank)) return current.getId();
        else return 0;
    }

    // Method to get the parent-node-name of a specific rank of parent by going from parent to parent, e.g. order of 11
    public String getParentName(int nodeId, String rank) {
        Node current = tree.get(nodeId);
        while (!current.getRank().equals(rank) & tree.containsKey(current.getId())) {
            current = tree.get(this.getParentId(current.getId()));
        }
        if (current.getRank().equals(rank)) return current.getScientificName();
        else return "";
    }

    // Method to get the rank of a node, e.g. "order", "family", "species", ...
    public String getRank(int nodeId) {
        return tree.get(nodeId).getRank();
    }

//    public static void main(String[] args) throws IOException {
//
//        String fileNameNode = "model/io/JUnitTestMarkus.txt";   //ME
//        String fileNames = "model/io/JUnitTestMarkusNames.txt"; //ME
//
//        // Example test
//        TaxonomyTree t = new TaxonomyTree(fileNameNode, fileNames);  // enter file name as parameter mainly for testing
//        System.out.println(t.getRank(6));
//        System.out.println(t.getParentName(11));
////        System.out.println(t.getParentId(11, "order"));
//        System.out.println(t.getRank(11));
//    }

    static class Node {

        private final int id;
        private String scientificName;
        private String rank;
        private Edge edgeToParent;
    String fileNameNode = "model/io/JUnitTestMarkus.txt";
    String fileNames = "model/io/JUnitTestMarkusNames.txt";
        // List of all edges to the child-nodes
        private final List<Edge> edges = new LinkedList<>();

        public Node(int id, String rank) {
            this.id = id;
            this.rank = rank;
        }

        // Constructor for a dummy-parent (unknown rank)
        // necessary for reading the file
        public Node(int id, Node child) {
            this.id = id;
            this.addEdgeToChild(child);
        }

        public int getId() {
            return id;
        }

        public String getScientificName() {
            return scientificName;
        }

        public void setScientificName(String scientificName) {
            this.scientificName = scientificName;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public Edge getEdgeToParent() {
            return edgeToParent;
        } // single value

        public Node getParent() {
            return edgeToParent.parent;
        }

        public List<Edge> getEdges() {
            return edges;
        }  // get list of all edges multiple list

        public void addEdgeToParent(Node parent) {
            edgeToParent = new Edge(parent, this);
        } // if parent or child is unknown

        public void addEdgeToChild (Node child) {
            edges.add(new Edge(this, child));
        }
    }

    static class Edge {

        private final Node parent;
        private final Node child;

        public Edge(Node parent, Node child) {
            this.parent = parent;
            this.child = child;
        }

        public Node getParent() {
            return parent;
        }

        public Node getChild() {
            return child;
        }

    }
}