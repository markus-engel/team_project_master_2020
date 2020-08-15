package model.io;

import java.util.LinkedList;
import java.util.List;

public class Node {

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
        Object output = new Object();
        if (edgeToAncestor == null) {
            output = null;
        }
        else if (edgeToAncestor.getAncestor() == null) {
            output = null;
        }
        else if (edgeToAncestor.getAncestor() != null) {
            output = edgeToAncestor.getAncestor();
        }
        return (Node) output;
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
        while (current != null && current.getAncestor() != null && !current.getRank().equals(rank)) {
            current = current.getAncestor();
        }

        if (current == null) {
            return -1;
        }
        else if (current.getRank() == null) {
            return -1;
        }
        else if (current.getRank().equals(rank)) {
            return current.getId();
        } else return -1;
    }

    // Method to get the name of an ancestor of a specific rank, e.g. id of the order of node 11
    public Object getAncestorName(String rank) {
        Node current = this;
        while (current != null && current.getAncestor() != null && !current.getRank().equals(rank)) {
            current = current.getAncestor();
        }

        if (current == null) {
            return -1;
        }
        else if (current.getRank() == null) {
            return -1;
        }
        else if (current.getRank().equals(rank)) {
            return current.getScientificName();
        } else return -1;
    }

    // Method to get the id of an ancestor of a specific rank, e.g. id of the order of node 11
    // test ME
    public int getSpecificAncestorId(String rank) {
        Node current = this;
        while (current != null && current.getAncestor() != null && !current.getRank().equals(rank)) {
            current = current.getAncestor();
        }

        if (current == null) {
            return -1;
        }
        else if (current.getRank() == null) {
            return -1;
        }
        else if (current.getRank().equals(rank)) {
            return current.getId();
        } else return -1;
    }
}
