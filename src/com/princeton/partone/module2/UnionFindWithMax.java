package com.princeton.partone.module2;

public class UnionFindWithMax {
    private int[] parent;
    private int[] size;
    private int[] max;  // stores the maximum element in each component

    public UnionFindWithMax(int n) {
        parent = new int[n];
        size = new int[n];
        max = new int[n];

        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
            max[i] = i;  // initially, each element is its own max
        }
    }

    // Find root with path compression
    private int root(int i) {
        while (i != parent[i]) {
            parent[i] = parent[parent[i]];  // path compression
            i = parent[i];
        }
        return i;
    }

    /**
     * Returns the largest element in the connected component containing i
     * Time complexity: O(log n) with path compression
     */
    public int find(int i) {
        return max[root(i)];
    }

    /**
     * Connects elements p and q
     * Time complexity: O(log n)
     */
    public void union(int p, int q) {
        int rootP = root(p);
        int rootQ = root(q);

        if (rootP == rootQ) {
            return;  // Already connected
        }

        // Weighted union: attach smaller tree to larger tree
        if (size[rootP] < size[rootQ]) {
            parent[rootP] = rootQ;
            size[rootQ] += size[rootP];
            // Update max: the new root should have the maximum of both components
            max[rootQ] = Math.max(max[rootP], max[rootQ]);
        } else {
            parent[rootQ] = rootP;
            size[rootP] += size[rootQ];
            // Update max: the new root should have the maximum of both components
            max[rootP] = Math.max(max[rootP], max[rootQ]);
        }
    }

    /**
     * Returns true if p and q are in the same component
     * Time complexity: O(log n)
     */
    public boolean connected(int p, int q) {
        return root(p) == root(q);
    }

    // For debugging: print component information
    public void printComponentInfo(int i) {
        int rootNode = root(i);
        System.out.println("Element " + i +
                " -> Root: " + rootNode +
                ", Max in component: " + max[rootNode]);
    }

    public static void main(String[] args) {
        // Test case from the problem
        UnionFindWithMax uf = new UnionFindWithMax(10);

        // Create component {1, 2, 6, 9}
        System.out.println("Creating component {1, 2, 6, 9}:");
        uf.union(1, 2);
        uf.union(2, 6);
        uf.union(6, 9);

        // Test find() for each element in the component
        System.out.println("\nTesting find() for component {1, 2, 6, 9}:");
        System.out.println("find(1) = " + uf.find(1) + " (expected: 9)");
        System.out.println("find(2) = " + uf.find(2) + " (expected: 9)");
        System.out.println("find(6) = " + uf.find(6) + " (expected: 9)");
        System.out.println("find(9) = " + uf.find(9) + " (expected: 9)");

        // Create another component {0, 3, 5, 7}
        System.out.println("\nCreating component {0, 3, 5, 7}:");
        uf.union(0, 3);
        uf.union(3, 5);
        uf.union(5, 7);

        System.out.println("\nTesting find() for component {0, 3, 5, 7}:");
        System.out.println("find(0) = " + uf.find(0) + " (expected: 7)");
        System.out.println("find(3) = " + uf.find(3) + " (expected: 7)");
        System.out.println("find(5) = " + uf.find(5) + " (expected: 7)");
        System.out.println("find(7) = " + uf.find(7) + " (expected: 7)");

        // Test connected()
        System.out.println("\nTesting connected():");
        System.out.println("connected(1, 9) = " + uf.connected(1, 9) + " (expected: true)");
        System.out.println("connected(1, 7) = " + uf.connected(1, 7) + " (expected: false)");

        // Merge the two components
        System.out.println("\nMerging components by union(2, 5):");
        uf.union(2, 5);

        System.out.println("\nAfter merging, find() for all elements:");
        System.out.println("find(1) = " + uf.find(1) + " (expected: 9)");
        System.out.println("find(7) = " + uf.find(7) + " (expected: 9)");
        System.out.println("find(0) = " + uf.find(0) + " (expected: 9)");
        System.out.println("connected(1, 7) = " + uf.connected(1, 7) + " (expected: true)");

        // Test isolated elements
        System.out.println("\nTesting isolated elements:");
        System.out.println("find(4) = " + uf.find(4) + " (expected: 4)");
        System.out.println("find(8) = " + uf.find(8) + " (expected: 8)");
    }
}
