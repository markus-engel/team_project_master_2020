package model;

import model.io.MainParserGraph;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;

import java.io.IOException;

public class Model {

    public Model() throws IOException {
    }

    // create needed objects of the IO classes to use them in presenter
    public void actionParseGraph(String path) throws IOException {
        MainParserGraph currentParserGraph = new MainParserGraph();
        currentParserGraph.readFile(path);

        TaxIdParser currentTaxIdParser = new TaxIdParser();
        // should we change the TaxIdParser? Because now its working through its main and there's no method to call

        TaxonomyTree currentTaxTree = new TaxonomyTree();
        currentTaxTree.getRank(9); // as method call example
    }


    // example method for model
    public void doSomething() {
        System.out.println("doing something");
    }
}
