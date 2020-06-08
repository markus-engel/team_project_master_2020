package model.io;

import java.io.*;
import java.util.*;

    /* TODO:
    // parser for names.dmp in line 58
     */

public class TaxonomyTree {

    HashMap<Integer, Node> tree = new HashMap<>();   // actual tree

    public TaxonomyTree() throws IOException { // parameter inclusion line 85 args[0]
        parseNodes();
        parseNames();
    }

    // Node Parser parsing the taxonomic information from nodesShort.dmp to the tree
    private void parseNodes() throws IOException {
        String line;
        File nodesShort = new File(getClass().getClassLoader().getResource("nodesShort.dmp").getFile()); // consider arguments implementation for testing
        BufferedReader reader = new BufferedReader(new FileReader(nodesShort));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
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

    private void parseNames() throws IOException{
        // TODO
    }

    // Method to get the parent-node-id of a node
    public int getParentId (int nodeId) {
        return tree.get(nodeId).getParent().getId();
    }

    // Method to get the parent-node-id of a specific rank of parent by going from parent to parent, e.g. order of 11
    public int getParentId (int nodeId, String rank) {
        Node current = tree.get(nodeId);
        while (!current.getRank().equals(rank) & tree.containsKey(current.getId())) {
            current = tree.get(this.getParentId(current.getId()));
        }
        if (current.getRank().equals(rank)) return current.getId();
        else return 0;
    }

    // Method to get the rank of a node, e.g. "order", "family", "species", ...
    public String getRank (int nodeId) {
        return tree.get(nodeId).getRank();
    }

    public static void main(String[] args) throws IOException {

        // Example test
        TaxonomyTree t = new TaxonomyTree();  // enter file name as parameter mainly for testing
        System.out.println(t.getRank(6));
        System.out.println(t.getParentId(11));
        System.out.println(t.getParentId(11,"order"));
        System.out.println(t.getRank(11));
        System.out.println(t.getParentId(1707));
        System.out.println(t.getParentId(t.getParentId(1707)));
        System.out.println(t.getRank(85006));

    }

    static class Node {

        private final int id;
        private String rank;
        private Edge edgeToParent;

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

        public void addEdgeToParent (Node parent) {
            edgeToParent = new Edge(parent, this);
        } // if parent or child is unkown

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