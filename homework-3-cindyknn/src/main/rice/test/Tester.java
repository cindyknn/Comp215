package main.rice.test;

import main.rice.obj.APyObj;

import java.io.*;
import java.util.*;

/**
 * A class for running a test suite. Encapsulates the ability to run the test suite on a
 * reference implementation to generate the expected results, the ability to create a
 * "wrapper" file for comparing actual results to these expected results, and the ability
 * to run the test suite on a set of files and identify which test cases each file fails
 * on.
 */
public class Tester {
    // TODO: complete the definition of the Tester class here

    /**
     * The internal representation of the name of the function under test.
     */
    private final String funcName;

    /**
     * The internal representation of the absolute path to the file containing the solution implementation.
     */
    private final String solutionPath;

    /**
     * The internal representation of the absolute path to the directory containing the student implementations.
     */
    private final String implDirPath;

    /**
     * The internal representation of the List of test cases to be executed.
     */
    private final List<TestCase> tests;

    /**
     * Constructor for a Tester object, which takes in the four pieces of data below.
     * @param funcName the name of the function under test.
     * @param solutionPath the absolute path to the file containing the solution implementation.
     * @param implDirPath the absolute path to the directory containing the student implementations.
     * @param tests the List of test cases to be executed.
     */
    public Tester(String funcName, String solutionPath, String implDirPath, List<TestCase> tests) {
        this.funcName = funcName;
        this.solutionPath = solutionPath;
        this.implDirPath = implDirPath;
        this.tests = tests;
    }

    /**
     * Runs every test case in the List of test cases on the reference solution and returns a List representing the
     * results of running these tests.
     * @return a List of Strings representing the results of running the tests.
     * @throws IOException if any file I/O goes wrong.
     * @throws InterruptedException if the thread that is executing the code is interrupted by the operating system.
     */
    public List<String> computeExpectedResults() throws IOException, InterruptedException {

        File sol = new File(this.solutionPath);

        //Build the correct footer string using helper function.
        String footer = buildFooter();

        //Check to see if a footer exists by looking for a block that starts with "import sys".
        BufferedReader reader = new BufferedReader(new FileReader(sol));
        boolean hasFooter = false;
        StringBuilder sbOriginal = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.equals("import sys")) {
                hasFooter = true;
                break;
            }
            sbOriginal.append(line).append("\n");
        }
        reader.close();

        //Append footer string to the end of the reference implementation depending on if footer exists or not.
        if(hasFooter) {
            FileWriter writer = new FileWriter(sol);
            writer.write(sbOriginal.toString());
            writer.write(footer);
            writer.close();
        } else {
            FileWriter writer = new FileWriter(sol, true);
            writer.write("\n" + footer);
            writer.close();
        }

        //Run each test on the reference implementation using helper function and add last output line to the results.
        List<String> results = new ArrayList<>();
        for (TestCase test : this.tests) {
            List<String> filePaths = new ArrayList<>();
            filePaths.add(this.solutionPath);
            String lastLine = runTestCase(test, filePaths);
            results.add(lastLine);
        }

        //Output the results to expected.py file and then return the results list.
        FileWriter writer = new FileWriter(this.implDirPath + File.separatorChar + "expected.py");
        System.out.println(this.implDirPath + File.separatorChar + "expected.py");
        writer.write("results = " + results.toString());
        writer.close();
        return results;

    }

    /**
     * Runs the List of tests on every file in the directory of buggy implementations and returns the results in the
     * form of a TestResults object.
     * @return a TestResults object representing the results of testing.
     * @throws IOException if any file I/O goes wrong.
     * @throws InterruptedException if the thread that is executing the code is interrupted by the operating system.
     */
    public TestResults runTests() throws IOException, InterruptedException {

        // Build the wrapper.py file using helper function.
        buildWrapperFile();

        //Initialize caseToFiles and wrongSet.
        List<Set<Integer>> caseToFiles = new ArrayList<>();
        for (int i = 0; i < this.tests.size(); i++) {
            Set<Integer> currCaseToFiles = new HashSet<>();
            caseToFiles.add(currCaseToFiles);
        }
        Set<Integer> wrongSet = new HashSet<>();

        //List and sort the buggy implementation files in implDirPath. Include validFiles as a counter to keep track of
        // the index of files that we can run tests on (i.e. not wrapper.py, expected.py, or non-.py files).
        File impl = new File(this.implDirPath);
        String[] filenames = impl.list();
        if (filenames == null) {
            throw new IOException();
        }
        Arrays.sort(filenames);
        int validFiles = 0;

        //Iterate through each buggy implementation, check to see if it is a valid file, then iterate through the tests
        for (String filename : filenames) {
            if (filename.endsWith(".py") && !(filename.equals("wrapper.py")) && !(filename.equals("expected.py"))) {
                for (int indexTest = 0; indexTest < this.tests.size(); indexTest++) {
                    TestCase test = this.tests.get(indexTest);
                    //Create list of file path strings to pass to helper function to run each test on the current implementation.
                    List<String> filePaths = new ArrayList<>();
                    filePaths.add(this.implDirPath + File.separatorChar + "wrapper.py");
                    filePaths.add(String.valueOf(indexTest));
                    filePaths.add(filename);
                    filePaths.add(this.funcName);
                    String lastLine = runTestCase(test, filePaths);
                    //Check if the last output line is "True" then test passed, otherwise test failed.
                    if (!(lastLine.equals("True"))) {
                        caseToFiles.get(this.tests.indexOf(test)).add(validFiles);
                        wrongSet.add(validFiles);
                    }
                }
                validFiles++;
            }
        }

        deletePyCache();
        return new TestResults(this.tests, caseToFiles, wrongSet);

    }

    /**
     * Creates and returns the string representing the footer to be appended to the end of the reference implementation.
     * @return a string representing the footer to be written into the reference implementation file.
     */
    private String buildFooter() {
        //Create the string representing the footer.
        StringBuilder sbFooter = new StringBuilder();
        sbFooter.append("import sys\n\n");
        sbFooter.append("if __name__ == \"__main__\":\n");
        sbFooter.append("    args = sys.argv[1:]\n");
        sbFooter.append("    args = [eval(arg) for arg in args]\n");
        sbFooter.append("    result = ");
        sbFooter.append(this.funcName);
        sbFooter.append("(*args)\n");
        sbFooter.append("    print (repr(result))");
        return sbFooter.toString();
    }

    /**
     * Creates the string representing the entire contents of the wrapper.py file, and writes the string into the wrapper.py file.
     * @throws IOException if any file I/O goes wrong.
     */
    private void buildWrapperFile() throws IOException {
        //Create the string representing the entire contents of the wrapper.py file.
        StringBuilder sbWrapper = new StringBuilder();
        sbWrapper.append("import sys\n");
        sbWrapper.append("from importlib import import_module\n");
        sbWrapper.append("from expected import results\n\n");

        sbWrapper.append("def test_buggy_impl(case_num, impl_name, fname, args):\n");
        sbWrapper.append("    mod_name = impl_name[:-3]\n");
        sbWrapper.append("    mod = import_module(mod_name)\n");
        sbWrapper.append("    func = getattr(mod, fname)\n");
        sbWrapper.append("    actual = func(*args)\n");
        sbWrapper.append("    expected = results[case_num]\n");
        sbWrapper.append("    return (actual == expected)\n\n");

        sbWrapper.append("if __name__ == \"__main__\":\n");
        sbWrapper.append("    case_num = int(sys.argv[1])\n");
        sbWrapper.append("    impl_name = sys.argv[2]\n");
        sbWrapper.append("    fname = sys.argv[3]\n");
        sbWrapper.append("    args = sys.argv[4:]\n");
        sbWrapper.append("    new_args = [eval(arg) for arg in args]\n");
        sbWrapper.append("    print (test_buggy_impl(case_num, impl_name, fname, new_args))");

        //Write the string into the wrapper.py file.
        FileWriter writer = new FileWriter(this.implDirPath + File.separatorChar + "wrapper.py");
        writer.write(sbWrapper.toString());
        writer.close();

    }

    /**
     * Runs the input test case on the input implementation file and returns the last output line as a string.
     * @param test a test case in the field List<TestCase> tests of the Tester class.
     * @param filePaths a list of strings representing the file paths that need to be included in the command string
     *                  passed to the process.
     * @return a string representing the last line outputted to the Input Stream after running the test case on the implementation file.
     * @throws IOException if any file I/O goes wrong.
     * @throws InterruptedException if the thread that is executing the code is interrupted by the operating system.
     */
    private String runTestCase(TestCase test, List<String> filePaths) throws IOException, InterruptedException {
        //Create the command (containing "python", all file paths, all arguments) and pass it to the process
        ProcessBuilder pb = new ProcessBuilder();
        List<String> cmd = new ArrayList<>();
        cmd.add("python");
        cmd.addAll(filePaths);
        for (APyObj<?> arg : test.getArgs()) {
            cmd.add(arg.toString());
        }
        pb.command(cmd);
        Process proc = pb.start();

        //Read the output from the Input Stream after running the test.
        BufferedReader readerOutput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        proc.waitFor();

        //Return the last output line after running the test.
        String curr;
        String prev = "";
        while ((curr = readerOutput.readLine()) != null) {
            prev = curr;
        }
        return prev;

    }

    /**
     * Helper function for deleting all cached Python files, so that an old cached version
     * of expected.pyc doesn't accidentally get invoked.
     *
     * @throws IOException if the path to the pycache is invalid or a deletion operation
     *                     fails
     */
    private void deletePyCache() throws IOException {
        // Get the list of all files in the pycache
        File pyCacheDir = new File(this.implDirPath + "/__pycache__/");
        String[] filepaths = pyCacheDir.list();

        if (filepaths != null) {
            for (String filepath : filepaths) {
                // Only delete .pyc files
                if (filepath.contains(".pyc")) {
                    File cachedFile =
                            new File(this.implDirPath + "/__pycache__/" + filepath);
                    if (!cachedFile.delete()) {
                        throw new IOException("could not delete cached file " + filepath);
                    }
                }
            }
        }
        pyCacheDir.delete();
    }
}