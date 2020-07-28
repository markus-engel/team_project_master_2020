package model.io;

public class Edge {

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
