/*
 * @lc app=leetcode id=167 lang=java
 *
 * [167] Two Sum II - Input Array Is Sorted
 */

// @lc code=start
class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int l = 0, r = numbers.length - 1;
        while (l < r) {
            int sum = numbers[l] + numbers[r];
            if (sum == target) {
                return new int[] { l + 1, r + 1 };
            } else if (sum > target) {
                r--;
            } else {
                l++;
            }

        }

        return new int[] { -1, -1 };
    }
}
// @lc code=end
