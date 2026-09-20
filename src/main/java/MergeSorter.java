public class MergeSorter {

    private static final int CUTOFF = 16;
    private int[] buffer;

    public long comparisons;
    public long recursiveCalls;
    public int maxDepth;

    public void sort(int[] a) {
        comparisons = 0;
        recursiveCalls = 0;
        maxDepth = 0;

        if (a == null || a.length < 2) return;

        buffer = new int[a.length];
        mergeSort(a, 0, a.length - 1, 1);
    }

    private void mergeSort(int[] a, int left, int right, int depth) {
        recursiveCalls++;
        if (depth > maxDepth) maxDepth = depth;

        if (right - left + 1 <= CUTOFF) {
            insertionSort(a, left, right);
            return;
        }

        int mid = left + (right - left) / 2;

        mergeSort(a, left, mid, depth + 1);
        mergeSort(a, mid + 1, right, depth + 1);

        merge(a, left, mid, right);
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

    private void merge(int[] a, int left, int mid, int right) {
        // 1) [left..right] бөлігін буферге көшіреміз
        for (int k = left; k <= right; k++) {
            buffer[k] = a[k];
        }

        int i = left;
        int j = mid + 1;
        int k = left;

        while (i <= mid && j <= right) {
            comparisons++;
            if (buffer[i] <= buffer[j]) {
                a[k++] = buffer[i++];
            } else {
                a[k++] = buffer[j++];
            }
        }

        while (i <= mid) {
            a[k++] = buffer[i++];
        }
        while (j <= right) {
            a[k++] = buffer[j++];
        }
    }
}