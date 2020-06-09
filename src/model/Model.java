package model;

import model.io.MainParserGraph;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;
import java.io.IOException;

public class Model {

    public Model() throws IOException {
    }

    // create needed classes to use them in presenter
    TaxIdParser currentTaxIdParser = new TaxIdParser();
    TaxonomyTree currentTaxTree = new TaxonomyTree();

    public void actionParseGraph(String path) throws IOException {
        MainParserGraph currentParserGraph = new MainParserGraph();
        currentParserGraph.readFile(path);
    }


    // example method for model
    public void doSomething() {
        System.out.println("doing something");
    }
}
