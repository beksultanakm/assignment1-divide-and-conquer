package org.example;

import java.util.Arrays;
import java.util.Random;

public class Main {

    static Random rnd = new Random();

    public static void main(String[] args) throws Exception {
        testSorts();
        testSelect();
        testClosest();

        Experiment.run("results/results.csv");
        System.out.println("Results saved to results/results.csv");
    }

    static void testSorts() {
        String[] names = {"Random", "Sorted", "Reversed", "Duplicates", "Empty", "Single"};
        int[][] cases = new int[6][];

        cases[0] = rnd.ints(100000, 0, 1_000_000).toArray();
        cases[1] = cases[0].clone();
        Arrays.sort(cases[1]);
        cases[2] = new int[cases[1].length];
        for (int i = 0; i < cases[2].length; i++) {
            cases[2][i] = cases[1][cases[1].length - 1 - i];
        }
        cases[3] = rnd.ints(100000, 0, 10).toArray();
        cases[4] = new int[0];
        cases[5] = new int[]{5};

        for (int i = 0; i < cases.length; i++) {
            int[] expected = cases[i].clone();
            Arrays.sort(expected);

            int[] m = cases[i].clone();
            new MergeSorter().sort(m);
            int[] q = cases[i].clone();
            new QuickSorter().sort(q);

            System.out.println(names[i] + " | Merge: " + Arrays.equals(m, expected)
                    + " | Quick: " + Arrays.equals(q, expected));
        }
    }

    static void testSelect() {
        int passed = 0;
        for (int t = 0; t < 200; t++) {
            int n = 1 + rnd.nextInt(2000);
            int bound = (t % 2 == 0) ? 1_000_000 : 10;
            int[] a = rnd.ints(n, 0, bound).toArray();
            int k = rnd.nextInt(n);

            int[] sorted = a.clone();
            Arrays.sort(sorted);

            if (new DeterministicSelector().select(a.clone(), k) == sorted[k]) passed++;
        }
        System.out.println("Select: " + passed + "/200 passed");
    }

    static void testClosest() {
        int passed = 0;
        for (int t = 0; t < 100; t++) {
            int n = 2 + rnd.nextInt(1999);
            int range = (t % 2 == 0) ? 1_000_000 : 20;
            Point[] pts = new Point[n];
            for (int i = 0; i < n; i++) {
                pts[i] = new Point(rnd.nextInt(range), rnd.nextInt(range));
            }

            double expected = ClosestPairSolver.bruteForce(pts);
            double got = new ClosestPairSolver().solve(pts);
            if (Math.abs(expected - got) < 1e-9) passed++;
        }
        System.out.println("Closest: " + passed + "/100 passed");
    }
}