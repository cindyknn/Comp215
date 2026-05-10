package main.rice.test;

// TODO: implement the TestResults class here

import java.util.List;
import java.util.Set;

/**
 * A class representing the results of testing. It bundles together three pieces of data that will be computed during the testing process.
 */
public class TestResults {

    /**
     * The internal representation of the base test set.
     */
    private final List<TestCase> allCases;
    /**
     * The internal representation of the collection indicating which files were "caught" by which test cases.
     */
    private final List<Set<Integer>> caseToFiles;
    /**
     * The internal representation of the set of all files that failed one or more tests.
     */
    private final Set<Integer> wrongSet;

    /**
     * Constructor for a TestResults object, which takes in three pieces of data: allCases, caseToFiles, wrongSet.
     * @param allCases a list of TestCase objects representing the base test set.
     * @param caseToFiles a collection indicating which files were "caught" by which test cases.
     * @param wrongSet a set of all files that failed one or more tests.
     */
    public TestResults(List<TestCase> allCases, List<Set<Integer>> caseToFiles, Set<Integer> wrongSet) {
        this.allCases = allCases;
        this.caseToFiles = caseToFiles;
        this.wrongSet = wrongSet;
    }

    /**
     * If the input index is within the bounds of allCases, returns the test case at the input index. Otherwise, returns null.
     * @param index an integer representing the index of the test case in allCases.
     * @return the test case at the input index if the input index is within the bounds of allCases; null otherwise.
     */
    public TestCase getTestCase(int index) {
        // If index is less than 0, or greater than or equal to size of allCases, it is out of bounds and the method returns null.
        if (index < 0 || index >= this.allCases.size()) {
            return null;
        }
        return this.allCases.get(index);
    }

    /**
     * Returns the collection indicating which files were "caught" by which test cases.
     * @return the collection indicating which files were "caught" by which test cases.
     */
    public List<Set<Integer>> getCaseToFiles() {
        return this.caseToFiles;
    }

    /**
     * Returns the set of files that failed one or more test cases.
     * @return the set of files that failed one or more test cases.
     */
    public Set<Integer> getWrongSet() {
        return this.wrongSet;
    }


}