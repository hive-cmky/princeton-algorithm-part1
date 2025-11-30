package com.princeton.partone.module3.analysisofalgorithm;

import java.util.*;

public class ThreeSum {

    /**
     * Find all unique triplets in the array that sum to zero.
     * Time complexity: O(n²)
     * Space complexity: O(1) excluding output
     *
     * @param nums input array of integers
     * @return list of all unique triplets that sum to zero
     */
    public static List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();

        if (nums == null || nums.length < 3) {
            return result;
        }

        // Sort the array - O(n²) or better as given
        Arrays.sort(nums);

        // Iterate through array, fixing first element
        for (int i = 0; i < nums.length - 2; i++) {
            // Skip duplicate values for first element
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            // Two-pointer approach for remaining elements
            int left = i + 1;
            int right = nums.length - 1;
            int target = -nums[i];

            while (left < right) {
                int sum = nums[left] + nums[right];

                if (sum == target) {
                    // Found a triplet
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    // Skip duplicates for second element
                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }

                    // Skip duplicates for third element
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }

                    left++;
                    right--;

                } else if (sum < target) {
                    // Sum too small, move left pointer right
                    left++;
                } else {
                    // Sum too large, move right pointer left
                    right--;
                }
            }
        }

        return result;
    }

    /**
     * Alternative: Find if any triplet sums to a specific target value
     */
    public static List<List<Integer>> threeSumTarget(int[] nums, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();

        if (nums == null || nums.length < 3) {
            return result;
        }

        Arrays.sort(nums);

        for (int i = 0; i < nums.length - 2; i++) {
            if (i > 0 && nums[i] == nums[i - 1]) {
                continue;
            }

            int left = i + 1;
            int right = nums.length - 1;
            int target = targetSum - nums[i];

            while (left < right) {
                int sum = nums[left] + nums[right];

                if (sum == target) {
                    result.add(Arrays.asList(nums[i], nums[left], nums[right]));

                    while (left < right && nums[left] == nums[left + 1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right - 1]) {
                        right--;
                    }

                    left++;
                    right--;

                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }

    // Test the implementation
    public static void main(String[] args) {
        // Test case 1: Standard example
        int[] nums1 = {-1, 0, 1, 2, -1, -4};
        System.out.println("Input: " + Arrays.toString(nums1));
        System.out.println("Triplets summing to 0: " + threeSum(nums1));
        System.out.println();

        // Test case 2: No solution
        int[] nums2 = {0, 1, 1};
        System.out.println("Input: " + Arrays.toString(nums2));
        System.out.println("Triplets summing to 0: " + threeSum(nums2));
        System.out.println();

        // Test case 3: All zeros
        int[] nums3 = {0, 0, 0};
        System.out.println("Input: " + Arrays.toString(nums3));
        System.out.println("Triplets summing to 0: " + threeSum(nums3));
        System.out.println();

        // Test case 4: Custom target
        int[] nums4 = {-1, 0, 1, 2, -1, -4};
        System.out.println("Input: " + Arrays.toString(nums4));
        System.out.println("Triplets summing to 3: " + threeSumTarget(nums4, 3));
    }
}
