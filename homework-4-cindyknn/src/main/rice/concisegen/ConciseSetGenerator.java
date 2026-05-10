package main.rice.concisegen;

import main.rice.test.TestCase;
import main.rice.test.TestResults;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

// TODO: implement the ConciseSetGenerator class here

/**
 * A class for generating a concise test set from a base test set.
 */
public class ConciseSetGenerator {

    /**
     * Finds an approximately minimal set of test cases that "cover" or "hit" all of the known buggy implementations using the greedy hitting set algorithm.
     *
     * @param results a TestResults object that is the provided base test set.
     * @return the concise set of TestCase objects that "covers" all known buggy implementations.
     */
    public static Set<TestCase> setCover(TestResults results) {
        Set<TestCase> conciseSet = new HashSet<TestCase>();

        // Create a copy of allCases, caseToFiles, wrongSet.
        List<TestCase> allCasesCopy = new ArrayList<>();
        List<Set<Integer>> caseToFilesCopy = new ArrayList<>();
        List<Set<Integer>> caseToFiles = results.getCaseToFiles();
        for (int caseIndex = 0; caseIndex < caseToFiles.size(); caseIndex++) {
            allCasesCopy.add(results.getTestCase(caseIndex));
            Set<Integer> fileSet = new HashSet<>(caseToFiles.get(caseIndex));
            caseToFilesCopy.add(fileSet);
        }
        Set<Integer> wrongSetCopy = new HashSet<>(results.getWrongSet());

        // Generate conciseSet by running hitting set algorithm until there no files left in wrongSetCopy.
        while (!wrongSetCopy.isEmpty()) {

            // Find the largest set in caseToFiles using helper method heuristic.
            Set<Integer> maxSet = heuristic(caseToFilesCopy);

            // Add maxSet to conciseSet.
            conciseSet.add(allCasesCopy.get(caseToFilesCopy.indexOf(maxSet)));

            // Remove all files in maxSet from wrongSetCopy.
            wrongSetCopy.removeAll(maxSet);

            // Iterate through every set in caseToFilesCopy and remove all files in common with maxSet.
            for (Set<Integer> set : caseToFilesCopy) {
                set.removeAll(maxSet);
            }

        }
        return conciseSet;
    }

    /**
     * Helper method for setCover that uses the heuristic of finding the largest set of Integers.
     *
     * @param caseToFiles the List of Sets of Integers passed in by setCover method
     * @return the largest set of integers in caseToFiles
     */
    private static Set<Integer> heuristic(List<Set<Integer>> caseToFiles) {

        int caseMaxSize = 0;
        int caseIndex = 0;
        int caseNum = 0;
        // Iterate over caseToFiles to find the largest set.
        for (Set<Integer> set : caseToFiles) {
            // If equal to max size then return the last largest set.
            if (set.size() >= caseMaxSize) {
                caseMaxSize = set.size();
                caseNum = caseIndex;
            }
            caseIndex++;
        }
        return caseToFiles.get(caseNum);
    }

}