import java.util.Random;

public class QuickSorter {

    private final Random random = new Random();

    public long comparisons;
    public long swaps;
    public int maxDepth;

    public void sort(int[] a) {
        comparisons = 0;
        swaps = 0;
        maxDepth = 0;

        if (a == null || a.length < 2) return;

        quickSort(a, 0, a.length - 1, 1);
    }

    private void quickSort(int[] a, int left, int right, int depth) {
        if (depth > maxDepth) maxDepth = depth;

        while (left < right) {
            int pivotIndex = left + random.nextInt(right - left + 1);
            int pivot = a[pivotIndex];

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


            if (lt - left < right - gt) {
                quickSort(a, left, lt - 1, depth + 1);
                left = gt + 1;
            } else {
                quickSort(a, gt + 1, right, depth + 1);
                right = lt - 1;
            }
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