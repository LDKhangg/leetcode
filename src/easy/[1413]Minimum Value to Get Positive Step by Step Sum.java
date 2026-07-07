/*
 * @lc app=leetcode id=1413 lang=java
 *
 * [1413] Minimum Value to Get Positive Step by Step Sum
 */

// @lc code=start
class Solution {
    public int minStartValue(int[] nums) {
        int n = nums.length;
        int prefixSum=0,min=0;
        for (int num : nums){
            prefixSum+=num;
            min=Math.min(prefixSum,min);
        }
        return 1-min;
    }
}
// @lc code=end

