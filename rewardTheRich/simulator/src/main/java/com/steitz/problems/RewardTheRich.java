package com.steitz.problems;

import java.util.Arrays;

import org.hipparchus.random.RandomDataGenerator;

/**
 * Generates random increasing sequences of doubles, computes dot product and
 * then generates random permutations of one of the vectors and checks that the
 * dot product goes down for any non-identity permutation.
 *
 * Should generate no output.
 */
public class RewardTheRich {
    private static final long ITERATIONS = 100000;
    private static final int DIMENSION = 5;
    private static final RandomDataGenerator RANDOM_DATA_GENERATOR = new RandomDataGenerator();

    public static void main(String[] args) {
        // Generate random x and y vecrors
        final double[] x = randomIncreasingSequence(DIMENSION);
        final double[] y = randomIncreasingSequence(DIMENSION);
        final double[] yRef = Arrays.copyOf(y, DIMENSION);

        // Compute unpermuted dot product
        final double xDotY = dot(x, y);

        // Generate ITERATIONS random permutations and check dot product
        for (int k = 0; k < ITERATIONS; k++) {
            // Generate a permuation of 0, 1, ... , n - 1.
            final int[] sigma = RANDOM_DATA_GENERATOR.nextPermutation(DIMENSION, DIMENSION);
            // map y[i] -> y[sigma[i]]
            final double[] yCopy = Arrays.copyOf(y, y.length);
            for (int j = 0; j < DIMENSION; j++) {
                y[j] = yCopy[sigma[j]];
            }
            // Compute permuted dot product and compare to unpermuted
            if (dot(x, y) >= xDotY && !Arrays.equals(yRef, y)) {
                System.out.println("Got one: " + Arrays.toString(y));
            }
        }
    }

    /**
     * Generate an increasing sequence from [0, 1].
     * <p>
     * Starts with initial uniform random value and then just adds a constant value
     * based on the difference from 1. So for example, if the initial random deviate
     * is .6 and length is 4, the values will be .6, .7, .8, .9.
     *
     * @param length length of the sequence
     * @return increasing sequence from [0,1] of the given length
     */
    private static double[] randomIncreasingSequence(int length) {
        final double[] out = new double[DIMENSION];

        final double outMin = RANDOM_DATA_GENERATOR.nextDouble();
        final double increment = (1 - outMin) / DIMENSION;
        out[0] = outMin;
        for (int i = 1; i < DIMENSION; i++) {
            out[i] = out[i - 1] + increment;
        }
        return out;
    }

    /**
     * Computes the dot product of two vectors.
     *
     * @param x array representing the first vector
     * @param y array representing the second vector
     * @return x dot y
     */
    private static double dot(double[] x, double[] y) {
        double xDotY = 0;
        for (int i = 0; i < DIMENSION; i++) {
            xDotY += x[i] * y[i];
        }
        return xDotY;
    }
}
