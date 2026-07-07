/*
 * @lc app=leetcode id=1480 lang=java
 *
 * [1480] Running Sum of 1d Array
 */

// @lc code=start
class Solution {
    public int[] runningSum(int[] nums) {
        int[] prefixSum = new int[nums.length];
        prefixSum[0]=nums[0];
        for(int i = 0;i<nums.length-1;i++){
            prefixSum[i+1]=prefixSum[i]+nums[i+1];
        }
        return prefixSum;
    }
}
// @lc code=end

