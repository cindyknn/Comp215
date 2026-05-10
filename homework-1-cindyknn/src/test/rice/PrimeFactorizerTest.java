package test.rice;

import main.rice.PrimeFactorizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * A test suite for the PrimeFactorizer class. Every method with the annotation "@Test"
 * will be called when running the test with JUnit.
 */
public class PrimeFactorizerTest {

    /**
     * A prime factorizer with an upper bound of 100.
     */
    private static final PrimeFactorizer factorizer100 = new PrimeFactorizer(100);
    /**
     * A prime factorizer with an upper bound of 50000.
     */
    private static final PrimeFactorizer factorizer50000 = new PrimeFactorizer(50000);

    /**
     * Tests that attempting factorization of a negative number rightfully returns null.
     */
    @Test
    void testFactorizeNegative() {
        int[] actual = factorizer100.computePrimeFactorization(-1);
        assertNull(actual);
    }

    /**
     * Tests that attempting factorization of 0 rightfully returns null.
     */
    @Test
    void testFactorize0() {
        int[] actual = factorizer100.computePrimeFactorization(0);
        assertNull(actual);
    }

    /**
     * Tests that attempting factorization of 1 rightfully returns null.
     */
    @Test
    void testFactorize1() {
        int[] actual = factorizer100.computePrimeFactorization(1);
        assertNull(actual);
    }

    /**
     * Tests factorization of the smallest prime 2 that can be factorized .
     */
    @Test
    void testFactorize2() {
        int[] actual = factorizer100.computePrimeFactorization(2);
        int[] expected = new int[]{2};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a prime that can be factorized.
     */
    @Test
    void testFactorize7() {
        int[] actual = factorizer100.computePrimeFactorization(7);
        int[] expected = new int[]{7};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a prime that can be factorized.
     */
    @Test
    void testFactorize83() {
        int[] actual = factorizer100.computePrimeFactorization(83);
        int[] expected = new int[]{83};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a composite that can be factorized.
     */
    @Test
    void testFactorize36() {
        int[] actual = factorizer100.computePrimeFactorization(36);
        int[] expected = new int[]{2, 2, 3, 3};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a composite that includes more than one distinct prime.
     */
    @Test
    void testFactorize72() {
        int[] actual = factorizer100.computePrimeFactorization(77);
        int[] expected = new int[]{7, 11};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a composite that includes repeats.
     */
    @Test
    void testFactorize81() {
        int[] actual = factorizer100.computePrimeFactorization(81);
        int[] expected = new int[]{3,3,3,3};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of the largest power of 2 less than 100 which is 64 (worst case scenario).
     */
    @Test
    void testFactorize64() {
        int[] actual = factorizer100.computePrimeFactorization(64);
        int[] expected = new int[]{2,2,2,2,2,2};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of the maxNumToFactorize 100 (Edge case).
     */
    @Test
    void testFactorize100() {
        int[] actual = factorizer100.computePrimeFactorization(100);
        int[] expected = new int[]{2,2,5,5};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of the number 1 greater than the maxNumToFactorize 101 (Edge case).
     */
    @Test
    void testFactorize101() {
        int[] actual = factorizer100.computePrimeFactorization(101);
        assertNull(actual);
    }

    /**
     * Tests factorization of a prime that is too large to be factorized.
     */
    @Test
    void testFactorize257() {
        int[] actual = factorizer100.computePrimeFactorization(257);
        assertNull(actual);
    }

    /**
     * Tests factorization of a composite that is too large to be factorized.
     */
    @Test
    void testFactorize255() {
        int[] actual = factorizer100.computePrimeFactorization(255);
        assertNull(actual);
    }

    /**
     * Tests factorization of a very large (> 10000) prime that can be factorized.
     */
    @Test
    void testFactorize36559() {
        int[] actual = factorizer50000.computePrimeFactorization(36559);
        int[] expected = new int[]{36559};
        assertArrayEquals(expected, actual);
    }

    /**
     * Tests factorization of a very large (> 10000) composite that can be factorized.
     */
    @Test
    void testFactorize43210() {
        int[] actual = factorizer50000.computePrimeFactorization(43210);
        int[] expected = new int[]{2, 5, 29, 149};
        assertArrayEquals(expected, actual);
    }

}
