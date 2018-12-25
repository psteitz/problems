package com.steitz.problems.senilewaiter;

import java.util.HashMap;
import java.util.Map;

import org.hipparchus.random.RandomDataGenerator;
import org.hipparchus.util.CombinatoricsUtils;

public class SenileWaiter {

    /**
     * Compute the number of permutations of cardinality that have the given
     * number of fixed points.
     *
     * @param numberOfFixedPoints number of fixed points
     * @param cardinality total number of elements
     * @return the number of permutations of cardinality that have
     *         numberOfFixedPoints fixed points
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

        // Multiply by the number of ways can the others can be deranged
        return fixedPointSelections *
               derangements(cardinality - numberOfFixedPoints);
    }

    /**
     * Compute the number of derangements of cardinality. A derangement is a
     * permutation with no fixed points.
     *
     * @param cardinality number of elements
     * @return the number of derangements of a list of length cardinality
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
        for (int i = 1; i <= cardinality; i++) { //
            count += CombinatoricsUtils.binomialCoefficientDouble(cardinality,
                                                                  i) *
                     derangements(cardinality - i);
        }
        return CombinatoricsUtils.factorialDouble(cardinality) - count;
    }

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
        for (int i = 0; i <= cardinality; i++) {
            out.put(i, counts[i] / (double) iterations);
        }
        return out;
    }
}
