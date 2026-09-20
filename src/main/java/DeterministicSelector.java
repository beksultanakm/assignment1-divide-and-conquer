public class DeterministicSelector {

    public long comparisons;
    public long swaps;
    public long recursiveCalls;
    public int maxDepth;

    private int ltBound;
    private int gtBound;


    public int select(int[] a, int k) {
        comparisons = 0;
        swaps = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (a == null || a.length == 0) {
            throw new IllegalArgumentException("Array is empty");
        }
        if (k < 0 || k >= a.length) {
            throw new IllegalArgumentException("k out of range");
        }
        return select(a, 0, a.length - 1, k, 1);
    }

    private int select(int[] a, int left, int right, int k, int depth) {
        recursiveCalls++;
        if (depth > maxDepth) maxDepth = depth;

        while (true) {
            if (right - left + 1 <= 5) {
                insertionSort(a, left, right);
                return a[k];
            }

            int pivot = medianOfMedians(a, left, right, depth);

            partition(a, left, right, pivot);

            if (k < ltBound) {
                right = ltBound - 1;
            } else if (k > gtBound) {
                left = gtBound + 1;
            } else {
                return pivot;
            }
        }
    }

    private int medianOfMedians(int[] a, int left, int right, int depth) {
        int n = right - left + 1;
        int numGroups = (n + 4) / 5;

        for (int i = 0; i < numGroups; i++) {
            int groupLeft = left + i * 5;
            int groupRight = Math.min(groupLeft + 4, right);

            insertionSort(a, groupLeft, groupRight);

            int medianIndex = groupLeft + (groupRight - groupLeft) / 2;
            swap(a, left + i, medianIndex);
        }

        int mid = left + (numGroups - 1) / 2;
        return select(a, left, left + numGroups - 1, mid, depth + 1);
    }

    private void partition(int[] a, int left, int right, int pivot) {
        int lt = left;
        int gt = right;
        int i = left;

        while (i <= gt) {
            comparisons++;
            if (a[i] < pivot) {
                swap(a, lt, i);
                lt++;
                i++;
            } else if (a[i] > pivot) {
                swap(a, i, gt);
                gt--;
            } else {
                i++;
            }
        }
        ltBound = lt;
        gtBound = gt;
    }

    private void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left) {
                comparisons++;
                if (a[j] > key) {
                    a[j + 1] = a[j];
                    j--;
                } else {
                    break;
                }
            }
            a[j + 1] = key;
        }
    }
    private void swap(int[] a, int i, int j) {
        if (i == j) return;
        int tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
        swaps++;
    }
}