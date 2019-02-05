package com.steitz.problems.senilewaiter;

import java.util.HashMap;
import java.util.Map;

import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.util.CombinatoricsUtils;

public class SenileWaiter {

    /**
     * Compute the number of permutations of a set of size {@code cardinality}
     * that have the given number of fixed points.
     *
     * @param numberOfFixedPoints number of fixed points
     * @param cardinality total number of elements
     * @return the number of permutations of of a set of size
     *         {@code cardinality} that have {@code numberOfFixedPoints} fixed
     *         points
     */
    public double frequency(int numberOfFixedPoints, int cardinality) {
        if (numberOfFixedPoints > cardinality) {
            return 0;
        }
        if (numberOfFixedPoints == cardinality) {
            return 1;
        }
        // Number of ways to choose numberOfFixedPoints from cardinality
        final double fixedPointSelections = CombinatoricsUtils
            .binomialCoefficientDouble(cardinality, numberOfFixedPoints);

        // Multiply by the number of ways the others can be deranged
        return fixedPointSelections *
               derangements(cardinality - numberOfFixedPoints);
    }

    /**
     * Compute the number of derangements of a set with {@code cardinality}
     * elements. A derangement is a permutation (one-to-one, onto mapping) with
     * no fixed points. For example, the function f from {0, 1, 2} to itself
     * defined by f(0) = 1, f(1) = 2, f(2) = 0 is a derangement of 3
     * (set-theoretically, 3 is the set {0, 1, 2}).
     *
     * @param cardinality set size
     * @return the number of derangements of a set with {@code cardinality}
     *         elements
     */
    public double derangements(int cardinality) {
        // Return quickly for cardinality <= 2
        if (cardinality <= 1) {
            return 0;
        }
        if (cardinality == 2) {
            return 1;
        }

        // Count the complement and subtract that from the total number of
        // permutations.
        //
        // Complement is 1 fixed point + ... + cardinality fixed points
        double count = 1; // Identity - all fixed points
        // For each i = 1, ..., cardinality - 1, the number of permutations with
        // i fixed points is the number of i-element subsets in a
        // cardinality-sized set times derangements(cardinality - i).
        for (int i = 1; i <= cardinality; i++) { //
            count += CombinatoricsUtils.binomialCoefficientDouble(cardinality,
                                                                  i) *
                     derangements(cardinality - i);
        }
        return CombinatoricsUtils.factorialDouble(cardinality) - count;
    }

    /**
     * Generates {@code iterations} random permutations of a set with
     * {@code cardinality} elements and keeps track of how many permutations fix
     * i points for i = 0, ..., {@code cardinality}. Returns a map of
     * {@code <Integer,
     * Double>} where the keys are numbers of fixed points and the values are
     * estimated probabilities that a random permutation has the given number of
     * fixed points.
     * <p>
     * More precisely, the keyset of the returned map is {@code {0,...,
     * cardinality}} and for each i = 0, ..., cardinality, the value associated
     * with i is the number of simulated permutations that have exactly i fixed
     * points divided by the number of samples taken ({@code iterations}). So
     * if @code{<i, p>} is in the map, that means that the proportion of all
     * simulated permutations that fixed exactly i points was p.
     * <p>
     * The returned map viewed as a function from {0, ..., cardinality} to [0,
     * 1] is an estimate of the probability mass function for the random
     * variable defined as the number of points fixed by a randomly selected
     * permutation of a set of size {@code cardinality}.
     * 
     * @param cardinality
     * @param iterations
     * @return a map of <Integer,Double> mapping numbers of fixed points to
     *         simulated probability that a random permutation has that number
     *         of fixed points
     */
    public Map<Integer, Double> simulatedPmf(int cardinality, long iterations) {
        final HashMap<Integer, Double> out = new HashMap<>();
        long ct = 0;
        final double[] counts = new double[cardinality + 1];
        final RandomDataGenerator generator = new RandomDataGenerator();
        while (ct < iterations) {
            final int[] sample = generator.nextPermutation(cardinality,
                                                           cardinality);
            // Count the number of fixed points
            int fixed = 0;
            for (int j = 0; j < cardinality; j++) {
                if (j == sample[j]) {
                    fixed++;
                }
            }
            counts[fixed] = counts[fixed] + 1;
            ct++;
        }
        // Compute densities and return these in the map
        final double universe = (double) iterations;
        for (int i = 0; i <= cardinality; i++) {
            out.put(i, counts[i] / universe);
        }
        return out;
    }
}
