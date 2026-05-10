package test.rice.node;

import main.rice.obj.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the PyListNode class.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PyListNodeTest extends AIterablePyNodeTest {

    /**
     * Sets up all static fields for use in the test cases.
     */
    @BeforeAll
    static void setUp() {
        AIterablePyNodeTest.setUp();
    }

    /**
     * Tests that getExDomain() works.
     */
    @Test
    @Order(1)
    void testGetExDomain() {
        assertEquals(Collections.singletonList(0), emptyOnly.getExDomain());
    }

    /**
     * Tests that getRanDomain() works.
     */
    @Test
    @Order(2)
    void testGetRanDomain() {
        assertEquals(Collections.singletonList(1), emptyOnly.getRanDomain());
    }

    /**
     * Tests that getLeftChild() works.
     */
    @Test
    @Order(3)
    void testGetLeftChild() {
        assertEquals(emptyLeftChild, emptyOnly.getLeftChild());
    }

    /**
     * Tests that getRightChild() works.
     */
    @Test
    @Order(4)
    void testGetRightChild() {
        assertNull(emptyOnly.getRightChild());
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element:
     * the empty list.
     */
    @Test
    @Order(5)
    void testGenExValsEmptyOnly() {
        Set<PyListObj<PyBoolObj>> actual = emptyOnly.genExVals();
        Set<PyListObj<PyBoolObj>> expected = Collections.singleton(
                new PyListObj<>(Collections.emptyList()));
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains a single element: a
     * list of length one.
     */
    @Test
    @Order(6)
    void testGenExValsOneLenOne() {
        Set<PyListObj<PyBoolObj>> expected = Collections.singleton(
                new PyListObj<>(Collections.singletonList(new PyBoolObj(true))));
        Set<PyListObj<PyBoolObj>> actual = oneLenOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains two lists of length
     * one.
     */
    @Test
    @Order(7)
    void testGenExValsTwoLenOne() {
        Set<PyListObj<PyBoolObj>> expected = Set.of(
                new PyListObj<>(Collections.singletonList(new PyBoolObj(true))),
                new PyListObj<>(Collections.singletonList(new PyBoolObj(false))));
        Set<PyListObj<PyBoolObj>> actual = twoLenOne.genExVals();
        assertEquals(expected, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains four lists of
     * length two.
     */
    @Test
    @Order(8)
    void testGenExValsFourLenTwo() {
        // Generate the actual set and compare the actual and expected sets
        Set<PyListObj<PyBoolObj>> actual = fourLenTwo.genExVals();
        assertEquals(expectedFourLenTwo, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains many lists of
     * length two.
     */
    @Test
    @Order(9)
    void testGenExValsManyLenTwo() {
        // Generate the actual set and compare the actual and expected sets
        Set<PyListObj<PyIntObj>> actual = manyLenTwo.genExVals();
        assertEquals(expectedManyLenTwo, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains lists of lengths
     * zero and one.
     */
    @Test
    @Order(10)
    void testGenExValsMultLensContig() {
        // Generate the actual set and compare the actual and expected sets
        Set<PyListObj<PyFloatObj>> actual = lensZeroToOne.genExVals();
        assertEquals(expectedLenZeroToOne, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains lists of lengths
     * zero and two.
     */
    @Test
    @Order(11)
    void testGenExValsMultLensNonContig() {
        // Generate the actual set and compare the actual and expected sets
        Set<PyListObj<PyFloatObj>> actual = lensZeroToThree.genExVals();
        assertEquals(expectedLenZeroToThree, actual);
    }

    /**
     * Tests genExVals() in the case that the expected output contains nested lists.
     */
    @Test
    @Order(12)
    void testGenExValsNested() {
        // Generate the actual set and compare the actual and expected sets
        Set<PyListObj<PyListObj<PyBoolObj>>> actual = nestedBools.genExVals();
        assertEquals(expectedNested, actual);
    }

    /**
     * Tests genRandVal() in the case that there is only one possible option: the empty
     * list.
     */
    @Test
    @Order(13)
    void testGenRandValEmpty() {
        Map<PyListObj<PyBoolObj>, Double> expected = new HashMap<>();
        expected.put(new PyListObj<>(new ArrayList<>()), 1.0);
        Map<PyListObj<PyBoolObj>, Double> actual = buildDistribution(oneLenOne, 100);
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there is only one possible option: a list of
     * length one.
     */
    @Test
    @Order(14)
    void testGenRandValOneLenOne() {
        // Build the expected distribution -- just one expected value with a 100% chance
        Map<PyListObj<PyBoolObj>, Double> expected = Map.of(
                new PyListObj<>(Collections.singletonList(new PyBoolObj(true))), 1.0);

        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyBoolObj>, Double> actual = buildDistribution(emptyOnly, 100);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.0));
    }

    /**
     * Tests genRandVal() in the case that there are two possible options of length one.
     */
    @Test
    @Order(15)
    void testGenRandValTwoLenOne() {
        // Build the expected distribution
        Map<PyListObj<PyBoolObj>, Double> expected = Map.of(
                new PyListObj<>(Collections.singletonList(new PyBoolObj(true))), 0.5,
                new PyListObj<>(Collections.singletonList(new PyBoolObj(false))), 0.5);

        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyBoolObj>, Double> actual =
                buildDistribution(fourLenTwo, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are four possible options of length two.
     */
    @Test
    @Order(16)
    void testGenRandValFourLenTwo() {
        // Build the expected distribution
        Map<PyListObj<PyBoolObj>, Double> expected =
                convertExpExToRandEqual(expectedFourLenTwo, 0.25);

        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyBoolObj>, Double> actual =
                buildDistribution(twoLenOne, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options of length two.
     */
    @Test
    @Order(17)
    void testGenRandValManyLenTwo() {
        // Build the expected distribution
        Map<PyListObj<PyIntObj>, Double> expected =
                convertExpExToRandEqual(expectedManyLenTwo, 1.0 / 9.0);

        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyIntObj>, Double> actual =
                buildDistribution(manyLenTwo, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expected, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are possible options of lengths one and
     * three.
     */
    @Test
    @Order(18)
    void testGenRandValMultLensContig() {
        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyFloatObj>, Double> actual = buildDistribution(
                lensZeroToOne, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expectedRandLenZeroToOne, actual, 0.01));
    }

    /**
     * Tests genRandVal() in the case that there are possible options of lengths one and
     * three.
     */
    @Test
    @Order(19)
    void testGenRandValMultLensNonContig() {
        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyFloatObj>, Double> actual = buildDistribution(
                lensZeroToThree, 100000);

        // Compare the actual and expected distributions
        assertTrue(
                compareDistribution(expectedRandLenZeroToThree, actual, 0.015));
    }

    /**
     * Tests genRandVal() in the case that there are many possible options that are
     * nested.
     */
    @Test
    @Order(20)
    void testGenRandValNested() {
        // Run a bunch of trials to get the actual distribution
        Map<PyListObj<PyListObj<PyBoolObj>>, Double> actual =
                buildDistribution(nestedBools, 100000);

        // Compare the actual and expected distributions
        assertTrue(compareDistribution(expectedRandNested, actual, 0.01));
    }
}