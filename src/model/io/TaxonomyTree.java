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
import java.util.Map;

public class TaxonomyTree {

    // The tree has taxonomic IDs as keys and Nodes as values
    // The Nodes themselves contain rank, scientific name, as well as Edges to parent and all children in the tree structure
    Map<Integer, Node> tree = new HashMap<>();

    public TaxonomyTree() throws IOException { // parameter inclusion line 85 args[0]
        parseNodes();
        parseNames();
    }

    // Node Parser parsing the taxonomic information from nodesShort.dmp to the tree
    private void parseNodes() throws IOException {
        String line;
        File nodesShort = new File(getClass().getClassLoader().getResource("model/io/nodesShort.dmp").getFile()); // consider arguments implementation for testing
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
                parent.addEdgeToDescendent(child);
            }
            // if the parent-node is not known yet, a dummy-parent is created with an edge to this child
            else {
                parent = new Node(parentId, child);
            }

            // Add/replace the parent-node in the tree
            tree.put(parentId, parent);

            // Add the the edge to the parent in the child-node and add/replace the child-node in the tree
            child.addEdgeToAncestor(parent);
            tree.put(childId, child);
        }
        reader.close();
    }

    // Name Parser parsing the scientific names of the nodes from namesShort.txt to the nodes in the tree
    private void parseNames() throws IOException {
        String line;
        File namesShort = new File(getClass().getClassLoader().getResource("model/io/namesShort.dmp").getFile()); // consider arguments implementation for testing
        BufferedReader reader = new BufferedReader(new FileReader(namesShort));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split("\t");
            int taxID = Integer.parseInt(parts[0]);
            String scientificName = parts[1];
            tree.get(taxID).setScientificName(scientificName);
        }
    }

    // returns the complete tree -> mostly for testing
    public Map<Integer, Node> getTree() {
        return tree;
    }

    // returns the complete Node with the given ID
    public Node getTaxNode(int taxId) {
        return tree.get(taxId);
    }

    /*// Method to get the id of a node's ancestor
    public int getAncestorId(int taxId) {
        if (tree.containsKey(taxId))
            return tree.get(taxId).getParent().getId();
        else return -1;
    }

    // Method to get the name of a node's ancestor
    public String getAncestorName(int taxId) {
        if (tree.containsKey(taxId)) return tree.get(taxId).getParent().getScientificName();
        else return null;
    }

    // Method to get the id of an ancestor of a specific rank, e.g. id of the order of node 11
    public int getAncestorId(int taxId, String rank) {
        if (tree.containsKey(taxId)) {
            Node current = tree.get(taxId);
            while (!current.getRank().equals(rank) & tree.containsKey(current.getId())) {
                current = tree.get(this.getAncestorId(current.getId()));
            }
            if (current.getRank().equals(rank)) { return current.getId(); }
            else return -1;
        } else return -1;
    }

    // Method to get the name of an ancestor of a specific rank, e.g. id of the order of node 11
    public String getAncestorName(int taxId, String rank) {
        if (tree.containsKey(taxId)) {
            Node current = tree.get(taxId);
            while (!current.getRank().equals(rank) & tree.containsKey(current.getId())) {
                current = tree.get(this.getAncestorId(current.getId()));
            }
            if (current.getRank().equals(rank)) {
                return current.getScientificName();
            }
            else return null;
        } else return null;
    }

    // Method to get the scientific name of a node
    public String getScientificName(int taxId) {
        if (tree.containsKey(taxId)) return tree.get(taxId).getScientificName();
        else return null;
    }

    // Method to get the rank of a node, returns e.g. "order", "family", "species", ...
    public String getRank(int taxId) {
        if (tree.containsKey(taxId)) return tree.get(taxId).getRank();
        else return null;
    }*/

    static class Node {

        private final int id;
        private String scientificName;
        private String rank;
        private Edge edgeToAncestor;

        // List of all edges to the child-nodes
        private final List<Edge> edges = new LinkedList<>();

        public Node(int id, String rank) {
            this.id = id;
            this.rank = rank;
        }

        // Constructor for a dummy-parent (unknown rank)
        // necessary for reading the file
        public Node(int id, Node descendent) {
            this.id = id;
            this.addEdgeToDescendent(descendent);
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

        public Edge getEdgeToAncestor() {
            return edgeToAncestor;
        } // single value

        public Node getAncestor() {
            return edgeToAncestor.ancestor;
        }

        public List<Edge> getEdges() {
            return edges;
        }  // get list of all edges multiple list

        public void addEdgeToAncestor(Node ancestor) {
            edgeToAncestor = new Edge(ancestor, this);
        } // if ancestor or descendent is unknown

        public void addEdgeToDescendent(Node descendent) {
            edges.add(new Edge(this, descendent));
        }

        // Method to get the id of a node's ancestor
        public int getAncestorId() {
            if (edgeToAncestor != null)
                return this.getAncestor().id;
            else return -1;
        }

        // Method to get the name of a node's ancestor
        public String getAncestorName() {
            if (edgeToAncestor != null) return this.getAncestor().getScientificName();
            else return null;
        }

        // Method to get the id of an ancestor of a specific rank, e.g. id of the order of node 11
        public int getAncestorId(String rank) {
            Node current = this;
            while (!current.getRank().equals(rank) & current.getAncestor() != null) {
                current = current.getAncestor();
            }
            if (current.getRank().equals(rank)) {
                return current.getId();
            } else return -1;
        }

        // Method to get the name of an ancestor of a specific rank, e.g. id of the order of node 11
        public String getAncestorName(String rank) {
            Node current = this;
            while (!current.getRank().equals(rank) & current.getAncestor() != null) {
                current = current.getAncestor();
            }
            if (current.getRank().equals(rank)) {
                return current.getScientificName();
            } else return null;
        }
    }

    static class Edge {

        private final Node ancestor;
        private final Node descendent;

        public Edge(Node ancestor, Node descendent) {
            this.ancestor = ancestor;
            this.descendent = descendent;
        }

        public Node getAncestor() {
            return ancestor;
        }

        public Node getDescendent() {
            return descendent;
        }

    }
}