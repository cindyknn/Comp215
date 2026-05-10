package main.rice;

/**
 * This class implements a relatively simple algorithm for computing the prime
 * factors of a number.  At initialization, a list of primes is computed. Given a
 * number, this list is used to efficiently compute the prime factors of that number.
 */
public class PrimeFactorizer {

    /**
     * private field representing the maximum integer that the PrimeFactorizer object can factorize
     */
    private int maxNumToFactorize;

    /**
     * private field representing the array of known prime numbers in the PrimeFactorizer object
     */
    private int[] primeNumbers;

    /**
     * This is the constructor for a PrimeFactorizer object, which takes as its input a
     * single integer representing the maximum value that this PrimeFactorizer
     * will be capable of factorizing. It initializes 2 fields: the maximum
     * integer that can be factorized and the array of known prime integers.
     *
     * @param maxNumToFactorize the maximum integer that the PrimeFactorizer object can factorize
     */
    public PrimeFactorizer(int maxNumToFactorize) {
        this.maxNumToFactorize = maxNumToFactorize;
        this.primeNumbers = this.findPrimeNumbers(maxNumToFactorize);
    }

    /**
     * This method computes and returns the prime factorization of the input
     * number, in the form of an array of prime integers.
     *
     * @param numToFactorize the input integer to be factorized
     * @return an integer array of prime factors of the input number
     */
    public int[] computePrimeFactorization(int numToFactorize) {
        int num = numToFactorize;
        // If the input number is less than 2 or greater than maxNumToFactorize, return null.
        if (num < 2 || num > this.maxNumToFactorize) {
            return null;
        }

        // Create temporary array of size large enough for worst case scenario
        int[] tempFactors = new int[this.findWorstArraySize(numToFactorize)];
        int numFactors = 0;

        // Factorize the input number by iterating through precomputed array of prime numbers
        for (int prime : this.primeNumbers) {
            // Divide num by the same prime factor until the remainder is no longer 0
            while (num % prime == 0) {
                tempFactors[numFactors] = prime;
                num /= prime;
                numFactors++;
            }
            // If num is equal to 1 then all prime factors of the input have been found
            // and we can exit out of the for loop iterating through array of prime numbers
            if (num == 1) {
                break;
            }
        }

        // If num is still greater than 1 after iterating through array
        // of prime numbers, then it must also be a prime factor of the input
        if (num != 1) {
            tempFactors[numFactors] = num;
            numFactors++;
        }

        // Copy prime factors from temporary array to final array of correct size
        int[] realFactors = new int[numFactors];
        for (int i = 0; i < numFactors; i++) {
            realFactors[i] = tempFactors[i];
        }
        return realFactors;
    }

    /**
     * This method finds all the prime numbers between 2 and the square root
     * of the input number, inclusive, using the Sieve of Eratosthenes.
     *
     * @param maxNumToFactorize the maximum integer that the PrimeFactorizer object can factorize
     * @return an integer array containing all the prime numbers between 2 and
     * the square root of maxNumToFactorize, inclusive
     */
    private int[] findPrimeNumbers(int maxNumToFactorize) {
        // Find maximum prime number by rounding up the square root of maxNumToFactorize
        int maxPrime = (int) Math.ceil(Math.sqrt(maxNumToFactorize));
        int len = maxPrime-1;

        // Create an integer array of prime candidates containing numbers between
        // 2 and maxPrime, inclusive
        int[] primeCandidates = new int[len];
        for (int i = 2; i<= maxPrime; i++) {
            primeCandidates[i-2] = i;
        }

        int numPrimes = 0;
        // Find prime numbers in primeCandidates by setting all composite numbers to 0
        for (int i = 0; i < len; i++) {
            int curr = primeCandidates[i];
            if (curr != 0) {
                numPrimes++;
                // Set all multiples of curr in primeCandidates to 0
                for (int multiple = i+curr; multiple < len; multiple+=curr) {
                    primeCandidates[multiple] = 0;
                }
            }
        }

        // Copy prime numbers (nonzero elements) from primeCandidates to final array of correct size
        int[] primeNumbers = new int[numPrimes];
        int index = 0;

        for (int curr : primeCandidates) {
            if (curr != 0) {
                primeNumbers[index] = curr;
                index++;
            }
        }
        return primeNumbers;
    }

    /**
     * This method finds the size of the array needed to store the prime factors
     * of the input number, such that it will be sufficiently large in the worst
     * case scenario, but no larger.
     *
     * @param numToFactorize the input integer to be factorized
     * @return an integer representing the appropriate size for the array of
     * prime factors
     */
    private int findWorstArraySize(int numToFactorize) {
        // Use Math.ceil() to round up, Use logarithmic change of base formula
        // to calculate log of numToFactorize with base 2
        return (int) Math.ceil(Math.log(numToFactorize) / Math.log(2));
    }
}

