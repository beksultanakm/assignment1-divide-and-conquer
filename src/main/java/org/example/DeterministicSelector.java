package org.example;

public class DeterministicSelector {

    public long comparisons;
    public int maxDepth;

    private int lt, gt;

    public int select(int[] a, int k) {
        comparisons = 0;
        maxDepth = 0;
        if (a == null || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("bad input");
        }
        return select(a, 0, a.length - 1, k, 1);
    }

    private int select(int[] a, int left, int right, int k, int depth) {
        maxDepth = Math.max(maxDepth, depth);

        while (true) {
            if (right - left + 1 <= 5) {
                insertionSort(a, left, right);
                return a[k];
            }

            int pivot = medianOfMedians(a, left, right, depth);
            partition(a, left, right, pivot);

            if (k < lt) right = lt - 1;
            else if (k > gt) left = gt + 1;
            else return pivot;
        }
    }

    private int medianOfMedians(int[] a, int left, int right, int depth) {
        int groups = (right - left + 5) / 5;

        for (int g = 0; g < groups; g++) {
            int gl = left + g * 5;
            int gr = Math.min(gl + 4, right);
            insertionSort(a, gl, gr);
            swap(a, left + g, (gl + gr) / 2);
        }

        return select(a, left, left + groups - 1, left + (groups - 1) / 2, depth + 1);
    }

    private void partition(int[] a, int left, int right, int pivot) {
        lt = left;
        gt = right;
        int i = left;
        while (i <= gt) {
            comparisons++;
            if (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);
            else i++;
        }
    }

    private void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left && a[j] > key) {
                comparisons++;
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;
        }
    }

    private void swap(int[] a, int i, int j) {
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
}