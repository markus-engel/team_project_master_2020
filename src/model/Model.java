package model;

import model.io.MainParserGraph;
import model.io.TaxIdParser;
import model.io.TaxonomyTree;
import java.io.IOException;

public class Model {

    // create needed classes to use them in presenter
    TaxIdParser currentTaxIdParser = new TaxIdParser();
    MainParserGraph currentParserGraph = new MainParserGraph();
    TaxonomyTree currentTaxTree = new TaxonomyTree();

    public Model() throws IOException {
    }

    // example method for model
    public void doSomething() {
        System.out.println("doing something");
    }


}
