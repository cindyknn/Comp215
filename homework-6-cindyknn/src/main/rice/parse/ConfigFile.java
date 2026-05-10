package main.rice.parse;

import java.util.List;
import main.rice.node.APyNode;

// TODO: implement the ConfigFile class here

/**
 * The ConfigFile class that bundles together 3 pieces of data that will be extracted from the config file during the parsing process.
 */
public class ConfigFile {

    /**
     * The name of the function under test.
     */
    private String funcName;

    /**
     * A List of PyNodes that will be used to generate TestCases for the function under test.
     */
    private List<APyNode<?>> nodes;

    /**
     * The number of random test cases to be generated.
     */
    private int numRand;

    /**
     * Constructor for a ConfigFile object, which takes in three pieces of data.
     * @param funcName The name of the function under test.
     * @param nodes A List of PyNodes that will be used to generate TestCases for the function under test.
     * @param numRand The number of random test cases to be generated.
     */
    public ConfigFile(String funcName, List<APyNode<?>> nodes, int numRand) {
        this.funcName = funcName;
        this.nodes = nodes;
        this.numRand = numRand;
    }

    /**
     * Returns the name of the function under test.
     * @return The name of the function under test.
     */
    public String getFuncName() {
        return this.funcName;
    }

    /**
     * Returns a List of PyNodes that will be used to generate TestCases for the function under test.
     * @return A List of PyNodes that will be used to generate TestCases for the function under test.
     */
    public List<APyNode<?>> getNodes() {
        return this.nodes;
    }

    /**
     * Returns the number of random test cases to be generated.
     * @return The number of random test cases to be generated.
     */
    public int getNumRand() {
        return this.numRand;
    }
}