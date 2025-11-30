package com.princeton.partone.module2;

public class SuccessorWithDelete {
    private int[] parent;
    private boolean[] deleted;
    private int n;

    /**
     * Initialize a set S = {0, 1, ..., n-1}
     * Time complexity: O(n)
     */
    public SuccessorWithDelete(int n) {
        this.n = n;
        parent = new int[n + 1];  // extra slot for n (sentinel)
        deleted = new boolean[n + 1];

        // Initially, each element is its own parent
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
        }
    }

    /**
     * Find the root (successor) with path compression
     * Time complexity: O(log n) amortized, nearly O(1)
     */
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]);  // path compression
        }
        return parent[x];
    }

    /**
     * Remove x from the set S
     * After removal, x points to its successor
     * Time complexity: O(log n)
     */
    public void remove(int x) {
        if (x < 0 || x >= n || deleted[x]) {
            return;  // already deleted or invalid
        }

        deleted[x] = true;

        // Union x with x+1 (its immediate successor)
        // This makes x point to the next available element
        if (x + 1 <= n) {
            parent[x] = find(x + 1);
        }
    }

    /**
     * Find the successor of x: smallest y in S such that y >= x
     * Returns the successor, or -1 if no successor exists
     * Time complexity: O(log n)
     */
    public int successor(int x) {
        if (x < 0 || x > n) {
            return -1;
        }

        // If x is not deleted, it is its own successor
        if (x < n && !deleted[x]) {
            return x;
        }

        // Find the successor (which might be n, meaning no successor)
        int succ = find(x);

        // If successor is n (sentinel), no valid successor exists
        if (succ >= n) {
            return -1;
        }

        return succ;
    }

    /**
     * Check if element x is in the set
     */
    public boolean contains(int x) {
        return x >= 0 && x < n && !deleted[x];
    }

    public static void main(String[] args) {
        System.out.println("Test 1: Basic operations");
        System.out.println("========================");
        SuccessorWithDelete s = new SuccessorWithDelete(10);

        System.out.println("Initial set: {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}");
        System.out.println("successor(5) = " + s.successor(5) + " (expected: 5)");

        System.out.println("\nRemoving 5...");
        s.remove(5);
        System.out.println("successor(5) = " + s.successor(5) + " (expected: 6)");
        System.out.println("successor(4) = " + s.successor(4) + " (expected: 4)");
        System.out.println("successor(6) = " + s.successor(6) + " (expected: 6)");

        System.out.println("\n\nTest 2: Multiple consecutive deletions");
        System.out.println("=======================================");
        SuccessorWithDelete s2 = new SuccessorWithDelete(10);

        // Remove 2, 3, 4, 5
        System.out.println("Removing 2, 3, 4, 5...");
        s2.remove(2);
        s2.remove(3);
        s2.remove(4);
        s2.remove(5);

        System.out.println("successor(2) = " + s2.successor(2) + " (expected: 6)");
        System.out.println("successor(3) = " + s2.successor(3) + " (expected: 6)");
        System.out.println("successor(4) = " + s2.successor(4) + " (expected: 6)");
        System.out.println("successor(5) = " + s2.successor(5) + " (expected: 6)");
        System.out.println("successor(1) = " + s2.successor(1) + " (expected: 1)");
        System.out.println("successor(6) = " + s2.successor(6) + " (expected: 6)");

        System.out.println("\n\nTest 3: Delete all elements");
        System.out.println("============================");
        SuccessorWithDelete s3 = new SuccessorWithDelete(5);

        System.out.println("Removing all: 0, 1, 2, 3, 4...");
        s3.remove(0);
        s3.remove(1);
        s3.remove(2);
        s3.remove(3);
        s3.remove(4);

        System.out.println("successor(0) = " + s3.successor(0) + " (expected: -1)");
        System.out.println("successor(2) = " + s3.successor(2) + " (expected: -1)");
        System.out.println("successor(4) = " + s3.successor(4) + " (expected: -1)");

        System.out.println("\n\nTest 4: Delete in reverse order");
        System.out.println("================================");
        SuccessorWithDelete s4 = new SuccessorWithDelete(10);

        System.out.println("Removing 9, 8, 7...");
        s4.remove(9);
        s4.remove(8);
        s4.remove(7);

        System.out.println("successor(7) = " + s4.successor(7) + " (expected: -1)");
        System.out.println("successor(8) = " + s4.successor(8) + " (expected: -1)");
        System.out.println("successor(9) = " + s4.successor(9) + " (expected: -1)");
        System.out.println("successor(6) = " + s4.successor(6) + " (expected: 6)");

        System.out.println("\n\nTest 5: Interleaved operations");
        System.out.println("===============================");
        SuccessorWithDelete s5 = new SuccessorWithDelete(10);

        s5.remove(3);
        System.out.println("After removing 3:");
        System.out.println("  successor(3) = " + s5.successor(3) + " (expected: 4)");

        s5.remove(4);
        System.out.println("After removing 4:");
        System.out.println("  successor(3) = " + s5.successor(3) + " (expected: 5)");
        System.out.println("  successor(4) = " + s5.successor(4) + " (expected: 5)");

        s5.remove(5);
        System.out.println("After removing 5:");
        System.out.println("  successor(3) = " + s5.successor(3) + " (expected: 6)");
        System.out.println("  successor(2) = " + s5.successor(2) + " (expected: 2)");

        s5.remove(6);
        System.out.println("After removing 6:");
        System.out.println("  successor(3) = " + s5.successor(3) + " (expected: 7)");
    }
}
