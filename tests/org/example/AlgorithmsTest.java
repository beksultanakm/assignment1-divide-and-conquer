package org.example;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class AlgorithmsTest {

    Random rnd = new Random(1);

    void checkSorts(int[] original) {
        int[] expected = original.clone();
        Arrays.sort(expected);

        int[] m = original.clone();
        new MergeSorter().sort(m);
        int[] q = original.clone();
        new QuickSorter().sort(q);

        assertArrayEquals(expected, m);
        assertArrayEquals(expected, q);
    }

    @Test
    void randomArray() {
        checkSorts(rnd.ints(10000, 0, 1_000_000).toArray());
    }

    @Test
    void sortedArray() {
        int[] a = rnd.ints(10000, 0, 1_000_000).toArray();
        Arrays.sort(a);
        checkSorts(a);
    }

    @Test
    void reverseSortedArray() {
        int[] a = rnd.ints(10000, 0, 1_000_000).toArray();
        Arrays.sort(a);
        for (int i = 0; i < a.length / 2; i++) {
            int tmp = a[i];
            a[i] = a[a.length - 1 - i];
            a[a.length - 1 - i] = tmp;
        }
        checkSorts(a);
    }

    @Test
    void duplicateHeavyArray() {
        checkSorts(rnd.ints(10000, 0, 5).toArray());
    }

    @Test
    void emptyArray() {
        checkSorts(new int[0]);
    }

    @Test
    void singleElement() {
        checkSorts(new int[]{5});
    }

    @Test
    void selectMatchesSortedArray() {
        for (int t = 0; t < 100; t++) {
            int n = 1 + rnd.nextInt(1000);
            int[] a = rnd.ints(n, 0, (t % 2 == 0) ? 1_000_000 : 10).toArray();
            int k = rnd.nextInt(n);

            int[] sorted = a.clone();
            Arrays.sort(sorted);

            assertEquals(sorted[k], new DeterministicSelector().select(a.clone(), k));
        }
    }

    @Test
    void selectInvalidKThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new DeterministicSelector().select(new int[]{1, 2, 3}, 5));
    }

    @Test
    void closestPairMatchesBruteForce() {
        for (int t = 0; t < 100; t++) {
            int n = 2 + rnd.nextInt(1999);
            int range = (t % 2 == 0) ? 1_000_000 : 20;
            Point[] pts = new Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point(rnd.nextInt(range), rnd.nextInt(range));
            }
            assertEquals(ClosestPairSolver.bruteForce(pts),
                    new ClosestPairSolver().solve(pts), 1e-9);
        }
    }

    @Test
    void closestPairNeedsTwoPoints() {
        assertThrows(IllegalArgumentException.class,
                () -> new ClosestPairSolver().solve(new Point[]{new Point(1, 1)}));
    }
}