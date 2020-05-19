import java.io.*;
import java.util.*;

public class TaxonomyTree {

    HashMap<Node, Edge> tree = new HashMap<Node, Edge>();   // actual tree
    HashMap<Integer, Node> nodeIds = new HashMap<>();       // map to get nodes by their id

    // maybe something additional about names according to id

    TaxonomyTree() throws IOException {

        // Parsing the taxonomic information from file nodes.dmp to the maps "tree" and "nodeIds"
        String line;
        File nodes = new File(getClass().getClassLoader().getResource("nodes.dmp").getFile());
        BufferedReader reader = new BufferedReader(new FileReader(nodes));
        while ((line = reader.readLine()) != null) {
            line = line.replace("\t", "");
            line = line.replace("|", ":");
            String[] parts = line.split(":");
            int nodeId = Integer.parseInt(parts[0]);
            int parentId = Integer.parseInt(parts[1]);
            String rank = parts[2];
            Node node = new Node(nodeId, rank);
            nodeIds.put(nodeId, node);
            Edge edge = new Edge(node, parentId);
            tree.put(node, edge);
        }
    }

    // Method to get the parent-node-id of a node
    public int getParentId (int id) {
        return tree.get(nodeIds.get(id)).parentId;
    }

    // Method to get the parent-node-id of a specific rank by going from parent to parent, e.g. order of 11
    public int getParentId (int id, String rank) {
        Node current = nodeIds.get(id);
        while (!current.getRank().equals(rank) & tree.containsKey(current)) {
            current = nodeIds.get(this.getParentId(current.getId()));
        }
        if (current.getRank().equals(rank)) return current.getId();
        else return 0;
    }

    // Method to get the rank of a node, e.g. "order", "family", "species", ...
    public String getRank (int id) {
        return nodeIds.get(id).getRank();
    }

    public static void main(String[] args) throws IOException {

        /* Example test
        TaxonomyTree t = new TaxonomyTree();
        System.out.println(t.getParentId(11));
        System.out.println(t.getParentId(11,"order"));
        System.out.println(t.getRank(11));
        System.out.println(t.getParentId(1707));
        System.out.println(t.getParentId(t.getParentId(1707)));
        System.out.println(t.getRank(85006));*/

    }

    static class Node {

        private final int id;
        private String rank;

        public Node(int id, String rank) {
            this.id = id;
            this.rank = rank;
        }

        public Node(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public String getRank() {
            return rank;
        }
    }

    static class Edge {

        private final Node node;
        private final int parentId;

        public Edge(Node node, int parentId) {
            this.node = node;
            this.parentId = parentId;
        }

        public Node getNode() {
            return node;
        }

        public int getParentId() {
            return parentId;
        }
    }
}







