package com.steitz.problems.senilewaiter;

import java.util.Map;

import org.hipparchus.util.CombinatoricsUtils;
import org.junit.Test;

public class SenileWaiterTest {

    @Test
    public void testAccuracy() {
        final SenileWaiter waiter = new SenileWaiter();
        final int maxIter = 10000000;
        for (int i = 4; i < 7; i++) {
            System.out.println("*********************");
            System.out.println(i);
            final double universe = CombinatoricsUtils.factorialDouble(i);
            for (int j = 0; j <= i; j++) {
                final double freq = waiter.frequency(j, i);
                System.out.println("j: " + j + " frequency: " + freq +
                                   " true density: " + freq / universe);
            }
            final Map<Integer, Double> dist = waiter.simulatedPmf(i, maxIter);
            for (int j = 0; j <= i; j++) {
                System.out
                    .println("j: " + j + " simulateed density: " + dist.get(j));
            }
            System.out.println(waiter.derangements(i));
            System.out.println(waiter.derangements(i) / universe);
        }
    }

    @Test
    public void testDerangements() {
        final SenileWaiter waiter = new SenileWaiter();
        for (int i = 3; i < 7; i++) {
            System.out
                .println("i: " + i + " derangements " + waiter.derangements(i));
        }
    }
}
