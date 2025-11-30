package com.princeton.partone.module3.analysisofalgorithm;

public class EggDrop {

    /**
     * VERSION 0: 1 egg, ≤T tosses
     *
     * Strategy: Linear search from floor 1 upward
     * - Drop from floor 1, 2, 3, ... until egg breaks
     * - When egg breaks at floor i, T = i
     * - If egg never breaks, T = n+1
     *
     * Tosses: Exactly T in worst case (when T is the answer)
     */
    public static int version0_findT(int n, int T) {
        System.out.println("\n=== VERSION 0: 1 egg, ≤T tosses ===");
        System.out.println("Strategy: Linear search upward");

        int tosses = 0;
        for (int floor = 1; floor <= n; floor++) {
            tosses++;
            boolean breaks = (floor >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + floor +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                System.out.println("Found T = " + floor + " in " + tosses + " tosses");
                return floor;
            }
        }

        System.out.println("Egg never broke, T = " + (n + 1));
        return n + 1;
    }

    /**
     * VERSION 1: ~lg(n) eggs and ~lg(n) tosses
     *
     * Strategy: Binary search
     * - Drop from middle floor
     * - If breaks: search lower half (use new egg)
     * - If doesn't break: search upper half (keep same egg)
     *
     * Tosses: ~lg(n)
     * Eggs used: ~lg(n) in worst case (each break uses one egg)
     */
    public static int version1_findT(int n, int T) {
        System.out.println("\n=== VERSION 1: ~lg(n) eggs, ~lg(n) tosses ===");
        System.out.println("Strategy: Binary search");

        int low = 1, high = n;
        int tosses = 0;
        int eggsUsed = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            tosses++;
            boolean breaks = (mid >= T);

            System.out.println("Toss " + tosses + ": Drop from floor " + mid +
                    " (range [" + low + ", " + high + "]) -> " +
                    (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        System.out.println("Found T = " + low + " in " + tosses + " tosses, " +
                eggsUsed + " eggs used");
        return low;
    }

    /**
     * VERSION 2: ~lg(T) eggs and ~2lg(T) tosses
     *
     * Strategy: Two-phase approach
     * Phase 1: Exponential search to find range containing T
     *   - Drop from floors 1, 2, 4, 8, 16, ... (powers of 2)
     *   - Uses ~lg(T) tosses, 1 egg
     * Phase 2: Binary search in the range found
     *   - Uses ~lg(T) tosses, ~lg(T) eggs
     *
     * Total: ~2lg(T) tosses, ~lg(T) eggs
     */
    public static int version2_findT(int n, int T) {
        System.out.println("\n=== VERSION 2: ~lg(T) eggs, ~2lg(T) tosses ===");
        System.out.println("Strategy: Exponential search + Binary search");

        int tosses = 0;
        int eggsUsed = 0;

        // Phase 1: Exponential search
        System.out.println("\nPhase 1: Exponential search to find range");
        int floor = 1;
        int prevFloor = 0;

        while (floor <= n) {
            tosses++;
            boolean breaks = (floor >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + floor +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                break;
            }

            prevFloor = floor;
            floor *= 2;
        }

        // Phase 2: Binary search in range [prevFloor + 1, min(floor, n)]
        System.out.println("\nPhase 2: Binary search in range [" +
                (prevFloor + 1) + ", " + Math.min(floor, n) + "]");

        int low = prevFloor + 1;
        int high = Math.min(floor, n);

        while (low <= high) {
            int mid = low + (high - low) / 2;
            tosses++;
            boolean breaks = (mid >= T);

            System.out.println("Toss " + tosses + ": Drop from floor " + mid +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }

        System.out.println("Found T = " + low + " in " + tosses + " tosses, " +
                eggsUsed + " eggs used");
        return low;
    }

    /**
     * VERSION 3: 2 eggs and ~2√n tosses
     *
     * Strategy: Jump by √n, then linear search
     * - First egg: Drop from floors √n, 2√n, 3√n, ... until it breaks
     * - If breaks at k√n, we know T is in ((k-1)√n, k√n]
     * - Second egg: Linear search from (k-1)√n + 1 to k√n
     *
     * Tosses: ~√n + √n = ~2√n
     * Eggs: 2
     */
    public static int version3_findT(int n, int T) {
        System.out.println("\n=== VERSION 3: 2 eggs, ~2√n tosses ===");
        System.out.println("Strategy: Jump by √n, then linear search");

        int jump = (int) Math.ceil(Math.sqrt(n));
        int tosses = 0;
        int eggsUsed = 0;

        // Phase 1: Jump by √n with first egg
        System.out.println("\nPhase 1: Jump by " + jump + " floors");
        int floor = jump;
        int prevFloor = 0;

        while (floor <= n) {
            tosses++;
            boolean breaks = (floor >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + floor +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                break;
            }

            prevFloor = floor;
            floor += jump;
        }

        // If first egg never broke, T > n
        if (floor > n && prevFloor < n) {
            prevFloor = floor - jump;
            floor = n;
        }

        // Phase 2: Linear search with second egg
        System.out.println("\nPhase 2: Linear search in range [" +
                (prevFloor + 1) + ", " + Math.min(floor, n) + "]");

        for (int f = prevFloor + 1; f <= Math.min(floor, n); f++) {
            tosses++;
            boolean breaks = (f >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + f +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                System.out.println("Found T = " + f + " in " + tosses + " tosses, " +
                        eggsUsed + " eggs used");
                return f;
            }
        }

        System.out.println("Found T = " + (floor + 1) + " in " + tosses + " tosses");
        return floor + 1;
    }

    /**
     * VERSION 4: 2 eggs and ≤c√T tosses
     *
     * Strategy: Modified jump search with variable step size
     * - Jump by increasing intervals: 1, 2, 3, 4, ... until egg breaks
     * - If egg breaks after k jumps, we've covered 1+2+3+...+k = k(k+1)/2 floors
     * - Then linear search backward with second egg
     *
     * Key insight: If we use jumps of size 1, 2, 3, ..., k and the egg breaks
     * after k jumps, we've tested up to k(k+1)/2 floors using k tosses with
     * first egg. Then we need at most k tosses with second egg to find T.
     *
     * To cover T floors: k(k+1)/2 ≥ T, so k ≈ √(2T)
     * Total tosses: k + k ≈ 2√(2T) ≈ 2.83√T (so c ≈ 2.83)
     *
     * Better: Use jumps that decrease to keep total constant
     * Start with jump of size n, then n-1, then n-2, ...
     * This ensures first_tosses + second_tosses = constant
     */
    public static int version4_findT(int n, int T) {
        System.out.println("\n=== VERSION 4: 2 eggs, ≤c√T tosses ===");
        System.out.println("Strategy: Decreasing jump sizes");

        int tosses = 0;
        int eggsUsed = 0;

        // Calculate initial jump size: we want jumps of n, n-1, n-2, ...
        // such that worst case is minimized
        // For optimal: initial jump ≈ √(2T), but we don't know T
        // So we use √(2n) as approximation
        int jump = (int) Math.ceil(Math.sqrt(2.0 * n));

        System.out.println("Starting with jump size: " + jump);
        System.out.println("\nPhase 1: Decreasing jumps");

        int floor = jump;
        int prevFloor = 0;

        while (floor <= n && jump > 0) {
            tosses++;
            boolean breaks = (floor >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + floor +
                    " (jump=" + jump + ") -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                break;
            }

            prevFloor = floor;
            jump--;
            floor += jump;
        }

        // Phase 2: Linear search with second egg
        System.out.println("\nPhase 2: Linear search in range [" +
                (prevFloor + 1) + ", " + Math.min(floor, n) + "]");

        for (int f = prevFloor + 1; f <= Math.min(floor, n); f++) {
            tosses++;
            boolean breaks = (f >= T);
            System.out.println("Toss " + tosses + ": Drop from floor " + f +
                    " -> " + (breaks ? "BREAKS" : "safe"));

            if (breaks) {
                eggsUsed++;
                System.out.println("Found T = " + f + " in " + tosses + " tosses, " +
                        eggsUsed + " eggs used");
                double ratio = tosses / Math.sqrt(T);
                System.out.println("Ratio tosses/√T = " + String.format("%.2f", ratio));
                return f;
            }
        }

        System.out.println("Found T = " + (floor + 1) + " in " + tosses + " tosses");
        return floor + 1;
    }

    // Main method with test cases
    public static void main(String[] args) {
        int n = 100;  // Building has 100 floors
        int T = 37;   // Egg breaks from floor 37 and above

        System.out.println("Building: " + n + " floors");
        System.out.println("Actual T: " + T + " (egg breaks from floor " + T + " and above)");
        System.out.println("=" .repeat(60));

        // Test each version
        version0_findT(n, T);
        version1_findT(n, T);
        version2_findT(n, T);
        version3_findT(n, T);
        version4_findT(n, T);

        // Additional test with different T
        System.out.println("\n\n" + "=".repeat(60));
        System.out.println("SECOND TEST");
        System.out.println("=".repeat(60));
        T = 89;
        System.out.println("Building: " + n + " floors");
        System.out.println("Actual T: " + T);

        version3_findT(n, T);
        version4_findT(n, T);
    }
}
