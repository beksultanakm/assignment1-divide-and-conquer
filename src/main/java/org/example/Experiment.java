package org.example;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class Experiment {

    static final int[] SIZES = {100, 1000, 10000, 100000, 500000};
    static final String[] TYPES = {"Random", "Sorted", "Reversed", "Duplicates"};
    static final int RUNS = 5;
    static final Random rnd = new Random(42);

    public static void run(String path) throws Exception {
        new File(path).getParentFile().mkdirs();

        try (PrintWriter out = new PrintWriter(path)) {
            out.println("algorithm,inputType,n,timeMs,maxDepth,comparisons");
            warmUp();

            for (int n : SIZES) {
                for (String type : TYPES) {
                    int[] data = makeInts(type, n);

                    MergeSorter ms = new MergeSorter();
                    double t1 = time(() -> ms.sort(data.clone()));
                    write(out, "MergeSort", type, n, t1, ms.maxDepth, ms.comparisons);

                    QuickSorter qs = new QuickSorter();
                    double t2 = time(() -> qs.sort(data.clone()));
                    write(out, "QuickSort", type, n, t2, qs.maxDepth, qs.comparisons);

                    DeterministicSelector sel = new DeterministicSelector();
                    double t3 = time(() -> sel.select(data.clone(), n / 2));
                    write(out, "DeterministicSelect", type, n, t3, sel.maxDepth, sel.comparisons);

                    // нүктелер үшін тек Random және Duplicates түрлері
                    if (type.equals("Random") || type.equals("Duplicates")) {
                        Point[] pts = makePoints(type, n);
                        ClosestPairSolver cp = new ClosestPairSolver();
                        double t4 = time(() -> cp.solve(pts));
                        write(out, "ClosestPair", type, n, t4, cp.maxDepth, cp.distanceChecks);
                    }

                    System.out.println("done: n=" + n + " type=" + type);
                }
            }
        }
    }

    static double time(Runnable task) {
        double[] t = new double[RUNS];
        for (int r = 0; r < RUNS; r++) {
            long start = System.nanoTime();
            task.run();
            t[r] = (System.nanoTime() - start) / 1e6;
        }
        Arrays.sort(t);
        return t[RUNS / 2];
    }

    static void warmUp() {
        int[] d = makeInts("Random", 20000);
        Point[] p = makePoints("Random", 20000);
        for (int i = 0; i < 5; i++) {
            new MergeSorter().sort(d.clone());
            new QuickSorter().sort(d.clone());
            new DeterministicSelector().select(d.clone(), d.length / 2);
            new ClosestPairSolver().solve(p);
        }
    }

    static void write(PrintWriter out, String algo, String type, int n,
                      double ms, int depth, long comparisons) {
        out.println(String.format(Locale.US, "%s,%s,%d,%.3f,%d,%d",
                algo, type, n, ms, depth, comparisons));
    }
    static int[] makeInts(String type, int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = type.equals("Duplicates") ? rnd.nextInt(10) : rnd.nextInt(1_000_000);
        }
        if (type.equals("Sorted") || type.equals("Reversed")) Arrays.sort(a);
        if (type.equals("Reversed")) {
            for (int i = 0; i < n / 2; i++) {
                int tmp = a[i];
                a[i] = a[n - 1 - i];
                a[n - 1 - i] = tmp;
            }
        }
        return a;
    }
    static Point[] makePoints(String type, int n) {
        int range = type.equals("Duplicates") ? 100 : 1_000_000;
        Point[] p = new Point[n];
        for (int i = 0; i < n; i++) {
            p[i] = new Point(rnd.nextInt(range), rnd.nextInt(range));
        }
        return p;
    }
}