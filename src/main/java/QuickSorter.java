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