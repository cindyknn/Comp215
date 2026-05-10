package main.rice.basegen;

import main.rice.node.APyNode;
import main.rice.obj.APyObj;
import main.rice.test.TestCase;

import java.util.*;

// TODO: implement the BaseSetGenerator class here
/**
 * A class responsible for generating the base test set.
 */
public class BaseSetGenerator {

    /**
     * A list of PyNodes encapsulating the type and domain specifications for each parameter to the function under test.
     */
    private List<APyNode<?>> typeDomainSpecs;
    /**
     * An integer representing the number of random tests cases to generate.
     */
    private int numRandTests;

    /**
     * Constructor for a BaseSetGenerator.
     * @param nodes A list of PyNodes encapsulating the type and domain specifications.
     * @param numRand An integer representing the number of random tests cases to generate.
     */
    public BaseSetGenerator(List<APyNode<?>> nodes, int numRand) {
        this.typeDomainSpecs = nodes;
        this.numRandTests = numRand;
    }

    /**
     * Generates the base test set, which is the union of all TestCases that are within the exhaustive domain and a numRand tests selected from the random domain.
     * @return a list of TestCases representing the base test set.
     */
    public List<TestCase> genBaseSet() {
        //Generate an exhaustive set of tests
        Set<TestCase> exTests = this.genExTests();

        //Generate a random set of tests
        Set<TestCase> randTests = this.genRandTests(exTests);

        //Create a list of base test cases
        List<TestCase> baseSet = new ArrayList<>();

        //Add all of exTests and all of randTests to baseSet
        baseSet.addAll(exTests);
        baseSet.addAll(randTests);

        return baseSet;
    }

    /**
     * Generates the set of all test cases adhering to the type and exhaustive domain specifications in the list of nodes that was passed during construction.
     * @return a set of TestCases adhering to the specifications.
     */
    public Set<TestCase> genExTests() {
        Set<TestCase> retSet = new HashSet<>();

        //Accumulate the exhaustive domains for each node in a list
        List<Set<? extends APyObj<?>>> nodeExDoms = new ArrayList<>();
        for (APyNode<?> node : this.typeDomainSpecs) {
            Set<? extends APyObj<?>> temp = node.genExVals();
            nodeExDoms.add(temp);
        }

        //Recursive call to genExtTestsRecHelper()
        Set<List<APyObj<?>>> helper = this.genExTestsRecHelper(this.typeDomainSpecs.size() - 1, nodeExDoms);

        //Only generate TestCases for the objects that have lengths in the domain.
        int len = this.typeDomainSpecs.size();
        for (List<APyObj<?>> possibleTest : helper) {
            //Check for the correct size.
            if (possibleTest.size() == len) {
                TestCase temp = new TestCase(possibleTest);
                retSet.add(temp);
            }
        }
        return retSet;
    }


    /**
     * Generates a random selection of test cases adhering to the type and random domain specifications in the input list of nodes.
     * @param exTests a Set of exhaustive tests.
     * @return a random selection of test cases adhering to the specifications.
     */
    public Set<TestCase> genRandTests(Set<TestCase> exTests) {

        Set<TestCase> randTests = new HashSet<>();

        //Generate new random TestCases until we have the desired number of tests.
        while (randTests.size() != this.numRandTests) {
            List<APyObj<?>> randArgs = new ArrayList<>();

            //For each node in the specifications, add a random value from the node's random domain to the random arguments list.
            for (APyNode<?> node : this.typeDomainSpecs) {
                randArgs.add(node.genRandVal());
            }
            //Create a TestCase given the randomly generated arguments list.
            TestCase tempRandTest = new TestCase(randArgs);

            if (!exTests.contains(tempRandTest)) {
                randTests.add(tempRandTest);
            }
        }
        return randTests;
    }


    /**
     * Recursive helper function for genExTests that generates possible TestCase argument lists given the nodes' exhaustive domains.
     * @param nodeIndex the index of the current node.
     * @param nodeExDoms the exhaustive domains of the nodes.
     * @return a set of lists where the type of the i-th element of each list is the type of the i-th PyNode element in typeDomainSpecs.
     */
    private Set<List<APyObj<?>>> genExTestsRecHelper(int nodeIndex, List<Set<? extends APyObj<?>>> nodeExDoms) {
        //Base case when length is zero.
        if (nodeIndex == 0) {
            Set<List<APyObj<?>>> minAllPerms = new HashSet<>(Set.of(new ArrayList<>()));

            //For each APyObj in the exhaustive domain at index 0, generate a list containing the only one APyObj and add it to the set.
            for (APyObj<?> obj : nodeExDoms.get(0)) {
                List<APyObj<?>> temp = new ArrayList(List.of(obj));
                minAllPerms.add(temp);
            }
            return minAllPerms;
        }
        //Recursive case when length is greater than zero.
        else {
            //Accumulate all argument lists.
            Set<List<APyObj<?>>> allPerms = new HashSet<>(Set.of(new ArrayList<>()));
            //Recursive call to this function using nodeIndex-1
            Set<List<APyObj<?>>> smallPerms = this.genExTestsRecHelper(nodeIndex - 1, nodeExDoms);

            //Generate new larger argument lists from the smaller lists.
            for (List<APyObj<?>> smallPerm : smallPerms) {
                for (APyObj<?> exVal : nodeExDoms.get(nodeIndex)) {
                    //For each smaller list, make a copy of the list and add a value from current node to it, then add the new list to the set.
                    List<APyObj<?>> temp = new ArrayList<>(smallPerm);
                    temp.add(exVal);
                    allPerms.add(temp);
                }
            }
            return allPerms;
        }
    }

}