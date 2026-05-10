package test.rice.test;

import main.rice.obj.*;
import main.rice.test.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the Tester class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TesterTest {
    // TODO: you are encouraged to add additional tests for edge cases here

    /**
     * The absolute path to this project directory, which we'll use to find the provided
     * pyfiles.
     */
    private static final String userDir = System.getProperty("user.dir");

    /**
     * Lists of test cases for each of the functions under test (function definitions
     * themselves can be found in the test.rice.test.pyfiles package).
     */
    private static final List<TestCase> f0Tests = new ArrayList<>();
    private static final List<TestCase> f1Tests = new ArrayList<>();
    private static final List<TestCase> f2Tests = new ArrayList<>();
    private static final List<TestCase> f3Tests = new ArrayList<>();

    /**
     * Contents of solution files.
     */
    private static final String[] solContentsArray = new String[]{"def func0(intval):\n    return intval",
            "def func1(bool_val, int_val, float_val):\n" +
                    "    if bool_val:\n" +
                    "        return int_val * float_val\n" +
                    "    else:\n" +
                    "        return int_val + float_val",
            "def func2(dict_val):\n" +
                    "    retval = []\n" +
                    "    for key, val in dict_val.items():\n" +
                    "        retval.append(key + str(val))\n" +
                    "\n" +
                    "    retval.sort()\n" +
                    "    return retval",
            "def func3(set_val, list_val, tup_val):\n" +
                    "    if (set_val):\n" +
                    "        return tuple([str(min(set_val)), str(min(set_val) + 1)])\n" +
                    "    elif (len(list_val) > len(tup_val)):\n" +
                    "        return tuple([str(list_val[0]), str(list_val[0] + 1)])\n" +
                    "    elif (tup_val):\n" +
                    "        return tuple([str(tup_val[0]), str(tup_val[0] + 1)])\n" +
                    "    return tuple(['0', '1'])"
    };

    /**
     * Expected contents of expected.py for f3 tests.
     */
    private static String f3resultStr;

    /**
     * Sets up all test cases for all functions under test.
     */
    @BeforeAll
    static void setUp() {
        setUpF0Tests();
        setUpF1Tests();
        setUpF2Tests();
        setUpF3Tests();
    }

    /**
     * Reverts all of the provided solution files.
     */
    @AfterAll
    static void cleanUp() throws IOException {
        for (int i = 0; i < 4; i++) {
            writeSolContents(i);
        }
    }

    /**
     * Tests that an IOException is thrown when the solution path is invalid.
     */
    @Test
    @Order(1)
    void testInvalidSolPath() {
        // Create a tester with an invalid solPath
        Tester tester = new Tester("func0", "/a/b/c/d/e",
                userDir + "/src/test/rice/test/pyfiles/f0oneRight",
                Collections.emptyList());

        try {
            tester.computeExpectedResults();
        } catch (IOException e) {
            return;
        } catch (InterruptedException e) {
            fail();
        }
        fail();
    }

    /**
     * Tests that an IOException is thrown when the impl dir path is invalid.
     */
    @Test
    @Order(2)
    void testInvalidImplDirPath() {
        // Create a tester with an invalid implDirPath
        Tester tester = new Tester("func0",
                userDir + "/src/test/rice/test/pyfiles/sols/func0sol.py",
                "/a/b/c/d/e",
                Collections.emptyList());

        try {
            tester.computeExpectedResults();
            tester.runTests();
        } catch (IOException e) {
            return;
        } catch (InterruptedException e) {
            fail();
        }
        fail();
    }

    /**
     * Tests that the solution file doesn't keep growing longer (footer gets overwritten,
     * not appended to, on subsequent runs).
     */
    @Test
    @Order(3)
    void testSolutionNotGrowing() {
        String solPath = userDir + "/src/test/rice/test/pyfiles/sols/func0sol.py";
        Tester tester = new Tester("func0", solPath,
                userDir + "/src/test/rice/test/pyfiles/f0oneRight",
                Collections.singletonList(f0Tests.get(0)));

        try {
            // Run computeExpectedResults() twice, grabbing the contents of the solution
            // after each
            writeSolContents(0);
            tester.computeExpectedResults();
            String oldContents = Files.readString(
                    Paths.get(solPath));
            tester.computeExpectedResults();
            String newContents = Files.readString(
                    Paths.get(solPath));

            // Make sure the contents didn't change
            assertEquals(oldContents, newContents);

        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests computeExpectedResults() using a single test on a function that takes one
     * simple argument.
     */
    @Test
    @Order(4)
    void testGetExpectedResultsOneTest() {
        expectedHelper("func0", Collections.singletonList(f0Tests.get(3)),
                "func0sol.py", List.of("3"));
    }

    /**
     * Tests computeExpectedResults() using multiple tests on a function that takes one
     * simple argument.
     */
    @Test
    @Order(5)
    void testGetExpectedResultsOneArgSimple() {
        List<String> expected = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            expected.add(String.valueOf(i));
        }
        expectedHelper("func0", f0Tests, "func0sol.py", expected);
    }

    /**
     * Checks that computeExpectedResults() correctly creates expected.py.
     */
    @Test
    @Order(6)
    void testWritesExpectedPyFile() {
        String implDirPath = userDir + "/src/test/rice/test/pyfiles/f0oneRight";
        String expectedContents = "results = [0, 1, 2, 3, 4]";
        Tester tester = new Tester("func0", userDir +
                "/src/test/rice/test/pyfiles/sols/func0sol.py", implDirPath, f0Tests);
        try {
            // Compute the actual results and compare to the expected
            List<String> actual = tester.computeExpectedResults();
            String actualContents = Files.readString(Paths.get(implDirPath + "/expected.py"));
            assertEquals(expectedContents, actualContents);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Tests computeExpectedResults() using multiple tests on a function that takes one
     * nested argument.
     */
    @Test
    @Order(7)
    @SuppressWarnings("unchecked")
    void testGetExpectedResultsOneArgNested() {
        // Generate expected (expected) results
        List<String> expected = new ArrayList<>();
        for (TestCase test : f2Tests) {
            List<String> vals = new ArrayList<>();
            Map<PyStringObj, PyIntObj> map = (Map<PyStringObj, PyIntObj>)
                    test.getArgs().get(0).getValue();

            for (Map.Entry<PyStringObj, PyIntObj> entry : map.entrySet()) {
                vals.add(entry.getKey().toString().substring(1, 5) + entry.getValue()
                        .getValue());
            }

            Collections.sort(vals);
            List<PyStringObj> pyVals = new ArrayList<>();
            for (String val : vals) {
                pyVals.add(new PyStringObj(val));
            }
            expected.add(new PyListObj<>(pyVals).toString());
        }

        // Run tests and compare expected (expected) results to actual (expected) results
        expectedHelper("func2", f2Tests, "func2sol.py", expected);
    }

    /**
     * Tests computeExpectedResults() using multiple tests on a function that takes
     * multiple simple arguments.
     */
    @Test
    @Order(8)
    void testGetExpectedResultsMultipleArgsSimple() {
        // Generate expected (expected) results
        List<String> expected = new ArrayList<>();
        for (TestCase test : f1Tests) {
            if ((boolean) test.getArgs().get(0).getValue()) {
                expected.add(String.valueOf((int) test.getArgs().get(1).getValue()
                        * (double) test.getArgs().get(2).getValue()));
            } else {
                expected.add(String.valueOf((int) test.getArgs().get(1).getValue()
                        + (double) test.getArgs().get(2).getValue()));
            }
        }

        // Run tests and compare expected (expected) results to actual (expected) results
        expectedHelper("func1", f1Tests, "func1sol.py", expected);
    }

    /**
     * Tests computeExpectedResults() using multiple tests on a function that takes
     * multiple nested arguments.
     */
    @Test
    @Order(9)
    @SuppressWarnings("unchecked")
    void testGetExpectedResultsMultipleArgsNested() {
        // Generate expected (expected) results
        List<String> expected = new ArrayList<>();

        for (TestCase test : f3Tests) {
            if (((Set<PyIntObj>) test.getArgs().get(0).getValue()).size() > 0) {
                expected.add("('3', '4')");
            } else if (((List<PyIntObj>) test.getArgs().get(1).getValue()).size()
                    > ((List<PyIntObj>) test.getArgs().get(2).getValue()).size()) {
                expected.add("('4', '5')");
            } else {
                expected.add("('5', '6')");
            }
        }

        // Run tests and compare expected (expected) results to actual (expected) results
        expectedHelper("func3", f3Tests, "func3sol.py", expected);
    }

    /**
     * Tests running a single failing test on a single implementation of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(10)
    void testRunTestsOneFileOneTestFails() {
        runTestsHelper("func0", Collections.singletonList(f0Tests.get(0)), "f0oneWrong",
                "results = [0]", Set.of(0), List.of(Set.of(0)), 0);
    }

    /**
     * Tests running a single failing test on a single implementation of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(11)
    void testRunTestsOneFileOneTestFails2() {
        runTestsHelper("func0", Collections.singletonList(f0Tests.get(0)), "f0oneWrong",
                "results = [0]", Set.of(0), List.of(Set.of(0)), 1);
    }

    /**
     * Tests running a single failing test on a single implementation of a function that
     * takes one simple argument; checks that original list of test cases is encapsulated in result.
     */
    @Test
    @Order(12)
    void testRunTestsReturnsTests() {
        runTestsHelper("func0", Collections.singletonList(f0Tests.get(0)), "f0oneWrong",
                "results = [0]", Set.of(0), List.of(Set.of(0)), 2);
    }

    /**
     * Tests running a single passing test on a single implementation of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(13)
    void testRunTestsOneFileOneTestPasses() {
        runTestsHelper("func0", Collections.singletonList(f0Tests.get(4)), "f0oneRight",
                "results = [4]", Set.of(), List.of(Set.of()), 0);
    }

    /**
     * Tests running a single passing test on a single implementation of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(14)
    void testRunTestsOneFileOneTestPasses2() {
        runTestsHelper("func0", Collections.singletonList(f0Tests.get(4)), "f0oneRight",
                "results = [4]", Set.of(), List.of(Set.of()), 1);
    }

    /**
     * Tests running multiple failing tests on a single implementation of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(15)
    void testRunTestsOneFileFailsAll() {
        runTestsHelper("func0", f0Tests, "f0oneWrong",
                "results = [0, 1, 2, 3, 4]", Set.of(0), List.of(Set.of(0), Set.of(0),
                        Set.of(0), Set.of(0), Set.of(0)), 0);
    }

    /**
     * Tests running multiple failing tests on a single implementation of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(16)
    void testRunTestsOneFileFailsAll2() {
        runTestsHelper("func0", f0Tests, "f0oneWrong",
                "results = [0, 1, 2, 3, 4]", Set.of(0), List.of(Set.of(0), Set.of(0),
                        Set.of(0), Set.of(0), Set.of(0)), 1);
    }

    /**
     * Tests running multiple passing tests on a single implementation of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(17)
    void testRunTestsOneFilePassesAll() {
        runTestsHelper("func0", f0Tests, "f0oneRight",
                "results = [0, 1, 2, 3, 4]", Set.of(), List.of(Set.of(), Set.of(),
                        Set.of(), Set.of(), Set.of()), 0);
    }

    /**
     * Tests running multiple passing tests on a single implementation of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(18)
    void testRunTestsOneFilePassesAll2() {
        runTestsHelper("func0", f0Tests, "f0oneRight",
                "results = [0, 1, 2, 3, 4]", Set.of(), List.of(Set.of(), Set.of(),
                        Set.of(), Set.of(), Set.of()), 1);
    }

    /**
     * Tests running a mix of passing and failing tests on a single implementation of a
     * function that takes one simple argument; checks wrongSet
     */
    @Test
    @Order(19)
    void testRunTestsOneFileMixed() {
        runTestsHelper("func0", f0Tests, "f0oneMixed",
                "results = [0, 1, 2, 3, 4]", Set.of(0), List.of(Set.of(0), Set.of(),
                        Set.of(0), Set.of(), Set.of(0)), 0);
    }

    /**
     * Tests running a mix of passing and failing tests on a single implementation of a
     * function that takes one simple argument.
     */
    @Test
    @Order(20)
    void testRunTestsOneFileMixed2() {
        runTestsHelper("func0", f0Tests, "f0oneMixed",
                "results = [0, 1, 2, 3, 4]", Set.of(0), List.of(Set.of(0), Set.of(),
                        Set.of(0), Set.of(), Set.of(0)), 1);
    }

    /**
     * Tests running multiple failing tests on multiple implementations of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(21)
    void testRunTestsMultipleFilesFailAll() {
        runTestsHelper("func0", f0Tests, "f0multipleWrong",
                "results = [0, 1, 2, 3, 4]", Set.of(0, 1), List.of(Set.of(0, 1),
                        Set.of(0, 1), Set.of(0, 1), Set.of(0, 1), Set.of(0, 1)), 0);
    }

    /**
     * Tests running multiple failing tests on multiple implementations of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(22)
    void testRunTestsMultipleFilesFailAll2() {
        runTestsHelper("func0", f0Tests, "f0multipleWrong",
                "results = [0, 1, 2, 3, 4]", Set.of(0, 1), List.of(Set.of(0, 1),
                        Set.of(0, 1), Set.of(0, 1), Set.of(0, 1), Set.of(0, 1)), 1);
    }

    /**
     * Tests running multiple passing tests on multiple implementations of a function that
     * takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(23)
    void testRunTestsMultipleFilesPassAll() {
        runTestsHelper("func0", f0Tests, "f0multipleRight",
                "results = [0, 1, 2, 3, 4]", Set.of(),
                List.of(Set.of(), Set.of(), Set.of(), Set.of(), Set.of()), 0);
    }

    /**
     * Tests running multiple passing tests on multiple implementations of a function that
     * takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(24)
    void testRunTestsMultipleFilesPassAll2() {
        runTestsHelper("func0", f0Tests, "f0multipleRight",
                "results = [0, 1, 2, 3, 4]", Set.of(),
                List.of(Set.of(), Set.of(), Set.of(), Set.of(), Set.of()), 1);
    }

    /**
     * Tests running a mix of passing and failing tests on multiple implementations of a
     * function that takes one simple argument; checks wrongSet.
     */
    @Test
    @Order(25)
    void testRunTestsMultipleFilesMixed() {
        runTestsHelper("func0", f0Tests, "f0multipleMixed",
                "results = [0, 1, 2, 3, 4]", Set.of(0, 1),
                List.of(Set.of(0), Set.of(1), Set.of(0), Set.of(1), Set.of(0)), 0);
    }

    /**
     * Tests running a mix of passing and failing tests on multiple implementations of a
     * function that takes one simple argument; checks caseToFiles.
     */
    @Test
    @Order(26)
    void testRunTestsMultipleFilesMixed2() {
        runTestsHelper("func0", f0Tests, "f0multipleMixed",
                "results = [0, 1, 2, 3, 4]", Set.of(0, 1),
                List.of(Set.of(0), Set.of(1), Set.of(0), Set.of(1), Set.of(0)), 1);
    }

    /**
     * Tests running a mix of tests on multiple implementations of a function that takes
     * one simple argument, where one implementation is buggy and the other is not;
     * checks wrongSet.
     */
    @Test
    @Order(27)
    void testRunTestsMultipleFilesMixedCorrectness() {
        runTestsHelper("func0", f0Tests, "f0multipleMixed2",
                "results = [0, 1, 2, 3, 4]", Set.of(0),
                List.of(Set.of(0), Collections.emptySet(), Set.of(0), Collections.emptySet(),
                        Set.of(0)), 0);
    }

    /**
     * Tests running a mix of tests on multiple implementations of a function that takes
     * one simple argument, where one implementation is buggy and the other is not;
     * checks caseToFiles.
     */
    @Test
    @Order(28)
    void testRunTestsMultipleFilesMixedCorrectness2() {
        runTestsHelper("func0", f0Tests, "f0multipleMixed2",
                "results = [0, 1, 2, 3, 4]", Set.of(0),
                List.of(Set.of(0), Collections.emptySet(), Set.of(0), Collections.emptySet(),
                        Set.of(0)), 1);
    }

    /**
     * Tests running multiple failing tests on multiple implementations of a function that
     * takes multiple nested arguments; checks wrongSet.
     */
    @Test
    @Order(29)
    void testRunTestsMultipleFilesFailAllComplex() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expected.add(Set.of(0, 1, 2));
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleWrong",
                f3resultStr, Set.of(0, 1, 2), expected, 0);
    }

    /**
     * Tests running multiple failing tests on multiple implementations of a function that
     * takes multiple nested arguments; checks caseToFiles.
     */
    @Test
    @Order(30)
    void testRunTestsMultipleFilesFailAllComplex2() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expected.add(Set.of(0, 1, 2));
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleWrong",
                f3resultStr, Set.of(0, 1, 2), expected, 1);
    }

    /**
     * Tests running multiple passing tests on multiple implementations of a function that
     * takes multiple nested arguments; checks wrongSet.
     */
    @Test
    @Order(31)
    void testRunTestsMultipleFilesPassAllComplex() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expected.add(Set.of());
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleRight",
                f3resultStr, Set.of(), expected, 0);
    }

    /**
     * Tests running multiple passing tests on multiple implementations of a function that
     * takes multiple nested arguments; checks caseToFiles.
     */
    @Test
    @Order(32)
    void testRunTestsMultipleFilesPassAllComplex2() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            expected.add(Set.of());
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleRight",
                f3resultStr, Set.of(),
                expected, 1);
    }

    /**
     * Tests running a mix of passing and failing tests on multiple implementations of a
     * function that takes multiple nested arguments; checks wrongSet.
     */
    @Test
    @Order(33)
    @SuppressWarnings("unchecked")
    void testRunTestsMultipleFilesMixedComplex() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        int i = 0;
        for (TestCase test : f3Tests) {
            if (((Set<PyIntObj>) test.getArgs().get(0).getValue()).size() != 0) {
                expected.add(Set.of(2));
            } else {
                Set<Integer> wrongSet = new HashSet<>();
                wrongSet.add(1);
                if (((List<PyIntObj>) test.getArgs().get(2).getValue()).size()
                        >= ((List<PyIntObj>) test.getArgs().get(1).getValue()).size()) {
                    wrongSet.add(0);
                }
                expected.add(wrongSet);
            }
            i++;
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleMixed",
                f3resultStr,
                Set.of(0, 1, 2), expected, 0);
    }

    /**
     * Tests running a mix of passing and failing tests on multiple implementations of a
     * function that takes multiple nested arguments; checks caseToFiles.
     */
    @Test
    @Order(34)
    @SuppressWarnings("unchecked")
    void testRunTestsMultipleFilesMixedComplex2() {
        // Generate expected results
        List<Set<Integer>> expected = new ArrayList<>();
        int i = 0;
        for (TestCase test : f3Tests) {
            if (((Set<PyIntObj>) test.getArgs().get(0).getValue()).size() != 0) {
                expected.add(Set.of(2));
            } else {
                Set<Integer> wrongSet = new HashSet<>();
                wrongSet.add(1);
                if (((List<PyIntObj>) test.getArgs().get(2).getValue()).size()
                        >= ((List<PyIntObj>) test.getArgs().get(1).getValue()).size()) {
                    wrongSet.add(0);
                }
                expected.add(wrongSet);
            }
            i++;
        }

        // Run tests and compare expected results to actual results
        runTestsHelper("func3", f3Tests, "f3multipleMixed",
                f3resultStr,
                Set.of(0, 1, 2), expected, 1);
    }

    /**
     * Sets up the test cases for function f0, which takes one simple argument.
     */
    private static void setUpF0Tests() {
        // Create five test cases with one argument that's an integer
        for (int i = 0; i < 5; i++) {
            f0Tests.add(new TestCase(Collections.singletonList(new PyIntObj(i))));
        }
    }

    /**
     * Sets up the test cases for function f1, which takes multiple simple arguments.
     */
    private static void setUpF1Tests() {
        // Create eight test cases with (bool, int, float) arguments
        for (boolean b : new boolean[]{true, false}) {
            for (int i = 0; i < 2; i++) {
                for (double f : new double[]{4.4, -6.07}) {
                    f1Tests.add(new TestCase(Arrays
                            .asList(new PyBoolObj(b), new PyIntObj(i), new PyFloatObj(f))));
                }
            }
        }
    }

    /**
     * Sets up the test cases for function f2, which takes one nested argument.
     */
    private static void setUpF2Tests() {
        // Create nine possible test cases with (dict(string:int)) arguments
        for (int val1 = -3; val1 < 0; val1++) {
            for (int val2 = -1; val2 < 2; val2++) {
                f2Tests.add(new TestCase(Collections.singletonList(new PyDictObj<>(
                        Map.of(new PyStringObj("key1"), new PyIntObj(val1),
                                new PyStringObj("key2"), new PyIntObj(val2))))));
            }
        }
    }

    /**
     * Sets up the test cases for function f3, which takes multiple nested arguments.
     */
    private static void setUpF3Tests() {
        // Create two possible set(int)s
        PySetObj<PyIntObj> set1 = new PySetObj<>(Set.of());
        PySetObj<PyIntObj> set2 = new PySetObj<>(Set.of(new PyIntObj(3)));

        // Create two possible list(int)s
        PyListObj<PyIntObj> list1 =
                new PyListObj<>(Collections.singletonList(new PyIntObj(4)));
        PyListObj<PyIntObj> list2 =
                new PyListObj<>(List.of(new PyIntObj(4), new PyIntObj(4)));

        // Create two possible tuple(int)s
        PyTupleObj<PyIntObj> tup1 =
                new PyTupleObj<>(Collections.singletonList(new PyIntObj(5)));
        PyTupleObj<PyIntObj> tup2 =
                new PyTupleObj<>(List.of(new PyIntObj(5), new PyIntObj(5)));

        // Create eight test cases with (set(int), list(int), tuple(int)) arguments
        for (PySetObj<?> set : new PySetObj[]{set1, set2}) {
            for (PyListObj<?> list : new PyListObj[]{list1, list2}) {
                for (PyTupleObj<?> tup : new PyTupleObj[]{tup1, tup2}) {
                    f3Tests.add(new TestCase(List.of(set, list, tup)));
                }
            }
        }

        f3resultStr =
                "results = [('5', '6'), ('5', '6'), ('4', '5'), ('5', '6'), ('3', '4'), "
                        + "('3', '4'), ('3', '4'), ('3', '4')]";
    }

    /**
     * Helper function for testing the computeExpectedResults() function; instantiates a
     * Tester object, computes the expected results, and compares those to the
     * manually-created expected results.
     *
     * @param funcName the name of the function under test
     * @param tests    the set of tests to be run
     * @param solName  the filename of the reference solution, which can be found in * the
     *                 test.rice.test.pyfiles.sols package
     * @param expected the expected (expected) results
     */
    private static void expectedHelper(String funcName, List<TestCase> tests, String solName, List<String> expected) {
        int solNum = Integer.parseInt(String.valueOf(funcName.charAt(funcName.length() - 1)));

        // Note that this is hard-coded to use the same directory for its expected.py output regardless of which
        // function is under test. This is because we are testing computeExpectedResults() and runTests()
        // independently, so runTests() will never actually use the expected.py that is written here.
        Tester tester = new Tester(funcName, userDir +
                "/src/test/rice/test/pyfiles/sols/" + solName, userDir +
                "/src/test/rice/test/pyfiles/f0oneRight", tests);
        try {
            // Compute the actual results and compare to the expected
            writeSolContents(solNum);
            List<String> actual = tester.computeExpectedResults();
            assertEquals(expected, actual);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    /**
     * Helper function for writing the contents of a solution file from scratch.
     *
     * @param solNum the number of the solution to be written; valid numbers are {0, 1, 2, 3}
     * @throws IOException if something goes wrong
     */
    private static void writeSolContents(int solNum) throws IOException {
        // Get path and contents
        String solPath = userDir + "/src/test/rice/test/pyfiles/sols/func" + solNum + "sol.py";
        String solContents = solContentsArray[solNum];

        // Write solution file from scratch
        FileWriter writer = new FileWriter(solPath);
        writer.write(solContents);
        writer.close();
    }

    /**
     * Helper function for testing the runTests() function; instantiates a Tester object,
     * fakes computation of the expected results, gets the actual results, and compares
     * both the wrongSet and caseToFile mappings between the actual and expected results.
     *
     * @param funcName    name of the function under test
     * @param tests       the set of tests to be run
     * @param implDir     the path to the directory containing the buggy implementations
     * @param solResults  the expected contents of expected.py, assuming
     *                    computeExpectedResults() is correct
     * @param expWrongSet the expected wrongSet
     * @param expResults  the expected caseToFile list
     */
    private static void runTestsHelper(String funcName, List<TestCase> tests, String implDir,
                                       String solResults, Set<Integer> expWrongSet, List<Set<Integer>> expResults,
                                       int outputToCheck) {
        Tester tester = new Tester(funcName, null,
                userDir + "/src/test/rice/test/pyfiles/" + implDir, tests);
        try {
            // Generate the expected.py file (to fake computing the expected results
            // without creating a dependency on computeExpectedResults())
            FileWriter writer = new FileWriter(userDir +
                    "/src/test/rice/test/pyfiles/" + implDir + "/expected.py");
            writer.write(solResults);
            writer.close();

            // Run the tester
            TestResults results = tester.runTests();
            if (outputToCheck == 0) {
                assertEquals(expWrongSet, results.getWrongSet());
            } else if (outputToCheck == 1) {
                assertEquals(expResults, results.getCaseToFiles());
            } else {
                for (int i = 0; i < tests.size(); i++) {
                    assertEquals(tests.get(i), results.getTestCase(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        } finally {
            // Clean up, so that we don't artificially make it look like
            // computeExpectedResults() works
            deletedExpected(implDir);
        }
    }

    /**
     * Deletes the file containing the expected results.
     *
     * @param implDir the path to the directory containing the expected results
     */
    private static void deletedExpected(String implDir) {
        File expFile = new File(userDir + "/src/test/rice/test/pyfiles/" +
                implDir + "/expected.py");
        expFile.delete();
    }
}