package main.rice;

// TODO: implement the Main class here

import main.rice.basegen.BaseSetGenerator;
import main.rice.concisegen.ConciseSetGenerator;
import main.rice.node.APyNode;
import main.rice.parse.ConfigFile;
import main.rice.parse.ConfigFileParser;
import main.rice.parse.InvalidConfigException;
import main.rice.test.TestCase;
import main.rice.test.TestResults;
import main.rice.test.Tester;

import java.io.IOException;
import java.util.*;

/**
 * The Main class of FEAT that is the entry point to the test case generator.
 */
public class Main {

    /**
     * The main method that delegates to generateTests() in order to compute the concise test set and print it to the console,
     *      along with an appropriate message explaining what's being printed.
     * @param args a String array that contains 3 arguments: the path to the config file, the path to the reference solution,
     *             the path to the directory containing the buggy implementations.
     * @throws IOException when there are file I/O problems.
     * @throws InvalidConfigException when there are invalid config file problems.
     * @throws InterruptedException when there are interruption problems.
     */
    public static void main(String[] args) throws IOException, InvalidConfigException, InterruptedException {
        Set<TestCase> genTestRes = generateTests(args);
        System.out.println("Concise Set Generation: " + genTestRes);
    }

    /**
     * A helper function for main() that utilizes the components built in homeworks 1-6 to perform end-to-end test case generation, returning the concise test set.
     * @param args the exact same String array of arguments as in main().
     * @return a Set<TestCase> representing the concise test set.
     * @throws IOException when there are file I/O problems.
     * @throws InvalidConfigException when there are invalid config file problems.
     * @throws InterruptedException when there are interruption problems.
     */
    public static Set<TestCase> generateTests(String[] args) throws IOException, InvalidConfigException, InterruptedException {
        //Variables for the 3 file path arguments.
        String configFilePath = args[0];
        String solutionPath = args[1];
        String buggyDirPath= args[2];

        //Read and parse the config file.
        String configFile = ConfigFileParser.readFile(configFilePath);
        ConfigFile parsedConfig = ConfigFileParser.parse(configFile);

        List<APyNode<?>> nodes = parsedConfig.getNodes();
        String funcName = parsedConfig.getFuncName();
        int numRanTests = parsedConfig.getNumRand();

        //Create a BaseSetGenerator and generate a base test set.
        BaseSetGenerator baseSetGen = new BaseSetGenerator(nodes, numRanTests);
        List<TestCase> baseSet = baseSetGen.genBaseSet();

        //Create a Tester and compute the expected results. Run the tests and store the results.
        Tester test = new Tester(funcName, solutionPath, buggyDirPath, baseSet);
        test.computeExpectedResults();
        TestResults testResults = test.runTests();

        //Generate and return the concise test set.
        return ConciseSetGenerator.setCover(testResults);
    }
}
