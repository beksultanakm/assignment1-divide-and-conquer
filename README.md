# Assignment 1: Divide-and-Conquer Algorithm Analysis

## A. Project Overview

The goal of this assignment is to implement four classic divide-and-conquer
algorithms in Java, analyze their running time with recurrences, measure them
on different inputs, and compare theory with practice.

Implemented algorithms:
1. **MergeSort** (linear merge, reusable buffer, insertion-sort cutoff = 16)
2. **QuickSort** (random pivot, in-place 3-way partition, recurse on the smaller part)
3. **Deterministic Select** (median-of-medians, groups of 5, in-place)
4. **Closest Pair of Points** (sort by x, divide and conquer, strip check by y)

Project structure: `src/main/java` (code), `tests/` (JUnit tests),
`results/results.csv` (measurements), `docs/plots` and `docs/screenshots`.

How to run: run `Main.java` (it runs all checks and the experiment and writes
`results/results.csv`), then `python plots.py` to build the plots.

## B. Algorithm Analysis

### MergeSort
Split the array in half, sort each half recursively, merge two sorted halves in
linear time. One auxiliary buffer is created once and reused. Parts of size <= 16
are sorted with insertion sort.
- Recurrence: T(n) = 2T(n/2) + Θ(n)
- Master Theorem: a = 2, b = 2, f(n) = n = n^(log2 2), so case 2 gives **Θ(n log n)**
- Space: Θ(n) (buffer). Recursion depth: about log2(n/16)

### QuickSort
Choose a random pivot, partition in place into `< pivot`, `== pivot`, `> pivot`,
recurse into the smaller part and loop over the larger part.
- Average: T(n) = 2T(n/2) + Θ(n) = **Θ(n log n)**
- Worst case: T(n) = T(n-1) + Θ(n) = **O(n²)** (very unlikely with a random pivot)
- Space: O(log n) stack, because we always recurse into the smaller part

### Deterministic Select (Median of Medians)
Split into groups of 5, take the median of each group, find the median of these
medians recursively (this is the pivot), partition, and continue only in the part
that contains the k-th element.
- Recurrence: T(n) <= T(n/5) + T(7n/10) + Θ(n)
- Since 1/5 + 7/10 = 9/10 < 1, the work shrinks geometrically. By Akra–Bazzi
  intuition the solution is **Θ(n)** in the worst case
- Space: O(log n) recursion for the medians

### Closest Pair of Points
Sort points by x once. Split into two halves, find the closest distance d in each
half, merge halves by y, build a strip of points closer than d to the middle line,
and compare each strip point only with the next points whose y-difference is < d
(at most a constant number of them).
- Recurrence: T(n) = 2T(n/2) + Θ(n) = **Θ(n log n)**
- Space: Θ(n)

## C. Experimental Results

Setup: sizes n = 100, 1 000, 10 000, 100 000, 500 000; input types Random, Sorted,
Reversed, Duplicates (Closest Pair: Random and Duplicates only). Each time is the
median of 5 runs after a JVM warm-up, measured with `System.nanoTime()`. The measured
time includes `clone()` of the input array, which is negligible compared with the
sorting itself. Metrics: time (ms), max recursion depth, comparisons (for Closest
Pair: distance computations).

### Execution time (ms), Random input

| Algorithm            | n=100 | n=1 000 | n=10 000 | n=100 000 | n=500 000 |
|----------------------|------:|--------:|---------:|----------:|----------:|
| MergeSort            | 0.002 |   0.031 |    0.700 |     8.420 |    49.545 |
| QuickSort            | 0.005 |   0.056 |    0.694 |     8.112 |    45.756 |
| Deterministic Select | 0.001 |   0.015 |    0.297 |     3.052 |    15.514 |
| Closest
