package com.princeton.partone.module3.analysisofalgorithm;

public class BitonicSearch {

    /**
     * STANDARD VERSION: ~3lg(n) compares
     *
     * Strategy:
     * 1. Find the peak (bitonic point) - ~lg(n) compares
     * 2. Binary search in ascending part - ~lg(n) compares
     * 3. Binary search in descending part - ~lg(n) compares
     * Total: ~3lg(n) compares
     */
    public static int searchStandard(int[] arr, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }

        // Step 1: Find the peak index
        int peak = findPeak(arr);

        // Step 2: Search in ascending part [0...peak]
        int result = binarySearchAscending(arr, key, 0, peak);
        if (result != -1) {
            return result;
        }

        // Step 3: Search in descending part [peak+1...n-1]
        return binarySearchDescending(arr, key, peak + 1, arr.length - 1);
    }

    /**
     * Find the peak (maximum element) in bitonic array
     * Time: O(lg n)
     */
    private static int findPeak(int[] arr) {
        int left = 0;
        int right = arr.length - 1;

        while (left < right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] < arr[mid + 1]) {
                // We're in ascending part, peak is to the right
                left = mid + 1;
            } else {
                // We're in descending part or at peak, peak is at or left of mid
                right = mid;
            }
        }

        return left;
    }

    /**
     * Binary search in ascending order
     */
    private static int binarySearchAscending(int[] arr, int key, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == key) {
                return mid;
            } else if (arr[mid] < key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    /**
     * Binary search in descending order
     */
    private static int binarySearchDescending(int[] arr, int key, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            if (arr[mid] == key) {
                return mid;
            } else if (arr[mid] > key) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }

        return -1;
    }

    /**
     * SIGNING BONUS: ~2lg(n) compares
     *
     * Strategy: Combine peak finding with search in one pass
     * At each step, we eliminate half the array by comparing with neighbors
     * and determining which half cannot contain the key.
     *
     * Key insight: At any point, we can determine:
     * 1. If we're in ascending/descending part based on neighbors
     * 2. Whether the key could be in the left or right half
     * 3. This allows us to search while finding the structure
     */
    public static int searchOptimized(int[] arr, int key) {
        if (arr == null || arr.length == 0) {
            return -1;
        }

        return bitonicSearch(arr, key, 0, arr.length - 1);
    }

    private static int bitonicSearch(int[] arr, int key, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;

            // Check if we found the key
            if (arr[mid] == key) {
                return mid;
            }

            // Determine if we're in ascending or descending part
            boolean isAscending = (mid == 0 || arr[mid] > arr[mid - 1]);
            boolean isDescending = (mid == arr.length - 1 || arr[mid] > arr[mid + 1]);

            // At the peak
            if (isAscending && isDescending) {
                // Key not found (we checked arr[mid] already)
                return -1;
            }

            // In ascending part
            if (isAscending) {
                if (key < arr[mid]) {
                    // Key must be in left half (ascending part)
                    right = mid - 1;
                } else {
                    // Key could be in right half (ascending or descending)
                    left = mid + 1;
                }
            }
            // In descending part
            else {
                if (key < arr[mid]) {
                    // Key could be in right half (descending part)
                    left = mid + 1;
                } else {
                    // Key must be in left half (could be ascending or peak)
                    right = mid - 1;
                }
            }
        }

        return -1;
    }

    /**
     * Alternative optimized version with explicit 2lg(n) bound
     * Uses modified binary search that checks both sides when necessary
     */
    public static int searchOptimized2(int[] arr, int key) {
        return searchBitonic(arr, key, 0, arr.length - 1);
    }

    private static int searchBitonic(int[] arr, int key, int left, int right) {
        if (left > right) {
            return -1;
        }

        int mid = left + (right - left) / 2;

        if (arr[mid] == key) {
            return mid;
        }

        // Check if mid is peak
        boolean isPeak = (mid == 0 || arr[mid] > arr[mid - 1]) &&
                (mid == arr.length - 1 || arr[mid] > arr[mid + 1]);

        if (isPeak) {
            // Not found at peak, key doesn't exist
            return -1;
        }

        // Ascending part
        if (mid < arr.length - 1 && arr[mid] < arr[mid + 1]) {
            if (key > arr[mid]) {
                return searchBitonic(arr, key, mid + 1, right);
            } else {
                return searchBitonic(arr, key, left, mid - 1);
            }
        }
        // Descending part
        else {
            if (key > arr[mid]) {
                return searchBitonic(arr, key, left, mid - 1);
            } else {
                return searchBitonic(arr, key, mid + 1, right);
            }
        }
    }

    // Test methods
    public static void main(String[] args) {
        // Bitonic array: [1, 3, 5, 7, 9, 8, 6, 4, 2]
        int[] arr = {1, 3, 5, 7, 9, 8, 6, 4, 2};

        System.out.println("Bitonic Array: ");
        for (int val : arr) {
            System.out.print(val + " ");
        }
        System.out.println("\nPeak is at index: " + findPeak(arr) + " (value: " + arr[findPeak(arr)] + ")");
        System.out.println();

        // Test various keys
        int[] testKeys = {1, 5, 9, 6, 2, 10, 0};

        System.out.println("Standard Version (~3lg n):");
        for (int key : testKeys) {
            int index = searchStandard(arr, key);
            System.out.println("Search for " + key + ": " +
                    (index != -1 ? "Found at index " + index : "Not found"));
        }

        System.out.println("\nOptimized Version (~2lg n):");
        for (int key : testKeys) {
            int index = searchOptimized(arr, key);
            System.out.println("Search for " + key + ": " +
                    (index != -1 ? "Found at index " + index : "Not found"));
        }

        // Another test case
        System.out.println("\n--- Test Case 2 ---");
        int[] arr2 = {1, 2, 3, 4, 5, 4, 3, 2, 1};
        System.out.println("Bitonic Array: ");
        for (int val : arr2) {
            System.out.print(val + " ");
        }
        System.out.println("\nPeak is at index: " + findPeak(arr2) + " (value: " + arr2[findPeak(arr2)] + ")");
        System.out.println("Search for 4 (optimized): " + searchOptimized(arr2, 4));
        System.out.println("Search for 1 (optimized): " + searchOptimized(arr2, 1));
    }
}
