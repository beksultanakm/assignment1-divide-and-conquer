package org.example;

import java.util.Arrays;

public class ClosestPairSolver {

    public long distanceChecks;
    public int maxDepth;

    private Point[] buffer;

    public double solve(Point[] points) {
        distanceChecks = 0;
        maxDepth = 0;
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("Need at least 2 points");
        }

        Point[] p = points.clone();
        Arrays.sort(p, (a, b) -> Double.compare(a.x, b.x));
        buffer = new Point[p.length];
        return closest(p, 0, p.length - 1, 1);
    }

    private double closest(Point[] p, int left, int right, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        if (right - left + 1 <= 3) {
            double best = Double.MAX_VALUE;
            for (int i = left; i <= right; i++) {
                for (int j = i + 1; j <= right; j++) {
                    best = Math.min(best, dist(p[i], p[j]));
                }
            }
            Arrays.sort(p, left, right + 1, (a, b) -> Double.compare(a.y, b.y));
            return best;
        }

        int mid = left + (right - left) / 2;
        double midX = p[mid].x;

        double d = Math.min(closest(p, left, mid, depth + 1),
                closest(p, mid + 1, right, depth + 1));

        mergeByY(p, left, mid, right);

        Point[] strip = new Point[right - left + 1];
        int size = 0;
        for (int i = left; i <= right; i++) {
            if (Math.abs(p[i].x - midX) < d) strip[size++] = p[i];
        }

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size && strip[j].y - strip[i].y < d; j++) {
                d = Math.min(d, dist(strip[i], strip[j]));
            }
        }
        return d;
    }

    private double dist(Point a, Point b) {
        distanceChecks++;
        double dx = a.x - b.x;
        double dy = a.y - b.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    private void mergeByY(Point[] p, int left, int mid, int right) {
        for (int k = left; k <= right; k++) buffer[k] = p[k];
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (buffer[i].y <= buffer[j].y) p[k++] = buffer[i++];
            else p[k++] = buffer[j++];
        }
        while (i <= mid) p[k++] = buffer[i++];
        while (j <= right) p[k++] = buffer[j++];
    }

    public static double bruteForce(Point[] pts) {
        double best = Double.MAX_VALUE;
        for (int i = 0; i < pts.length; i++) {
            for (int j = i + 1; j < pts.length; j++) {
                double dx = pts[i].x - pts[j].x;
                double dy = pts[i].y - pts[j].y;
                best = Math.min(best, Math.sqrt(dx * dx + dy * dy));
            }
        }
        return best;
    }
}