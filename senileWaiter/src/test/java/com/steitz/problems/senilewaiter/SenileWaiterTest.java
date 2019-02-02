package com.steitz.problems.senilewaiter;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.hipparchus.util.CombinatoricsUtils;
import org.junit.Test;

public class SenileWaiterTest {

    @Test
    public void testAccuracy() {
        final SenileWaiter waiter = new SenileWaiter();
        final int iterations = 10000000;
        for (int i = 4; i < 7; i++) {
            final double universe = CombinatoricsUtils.factorialDouble(i);
            final Map<Integer, Double> truePmf = new HashMap<>();
            for (int j = 0; j <= i; j++) {
                final double freq = waiter.frequency(j, i);
                final double trueP = freq / universe;
                assertEquals(pmf(i, j), trueP, 1e-12);
                truePmf.put(j, trueP);
            }
            final Map<Integer, Double> dist = waiter.simulatedPmf(i,
                                                                  iterations);
            for (int j = 0; j <= i; j++) {
                assertEquals(truePmf.get(j), dist.get(j), 1e-3);
            }
        }
    }

    @Test
    public void testDerangements() {
        final SenileWaiter waiter = new SenileWaiter();
        // Sequence of derangement counts starting at n = 1.
        // Copied from from Wikipedia on 2/1/2019:
        //
        // 0, 1, 2, 9, 44, 265, 1854, 14833, 133496, 1334961, 14684570,
        // 176214841, 2290792932
        final double[] expected = {
                                    0, 1, 2, 9, 44, 265, 1854, 14833, 133496,
                                    1334961, 14684570, 176214841, 2290792932d
        };
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], waiter.derangements(i + 1), 1e-2);
        }

    }

    public double pmf(int n, int k) {
        // Compute p(permutation of n fixes k points).
        // There are n! possible permutations. Every permutation that fixes k
        // points consists of the identity function on those k points extended
        // by a permutation on the n - k remaining points that has no fixed
        // points (a derangement of n - k). Therefore for each n, k, we should
        // have
        // pmf(n,k) = (b(n,k) * derangements(n - k))/n!
        // where b(n,k) is the binomial coefficient "n choose k"
        // Above does not work for k = n, because while there is one permutation
        // that fixes all of the points, there are no derangements of a set with
        // no elements.
        //
        final SenileWaiter waiter = new SenileWaiter();

        return k < n ? (CombinatoricsUtils.binomialCoefficientDouble(n, k) *
                        waiter.derangements(n - k)) /
                       CombinatoricsUtils.factorialDouble(n)
                     : 1 / CombinatoricsUtils.factorialDouble(n);
    }
}
