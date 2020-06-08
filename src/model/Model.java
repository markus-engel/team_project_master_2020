package model;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;

public class Model {

    private String stringProp = new String("start");

    private UndirectedSparseGraph newVSP;

    //MainParserGraph

    public void doSomething() {
        System.out.println("doing something");
    }

    public void changeTheProp(){
        this.stringProp = "lol";
    }

    public String getStringProp() {
        return stringProp;
    }

}
