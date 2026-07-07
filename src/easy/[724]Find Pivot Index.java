/*
 * @lc app=leetcode id=724 lang=java
 *
 * [724] Find Pivot Index
 */

// @lc code=start
class Solution {
    public int pivotIndex(int[] nums) {
        int n=nums.length;
        // int sum=0;
        // for(int num:nums){
        //     sum+=num;
        // }
        // int leftSum=0;
        // for(int i = 0 ; i< n;i++){
        //     int rightSum=sum-nums[i]-leftSum;
        //     if (leftSum == rightSum) {
        //         return i;
        //     }

        //     leftSum += nums[i];
        // }

        int[] prefix = new int[n+1];
        for(int i =0;i<n;i++){
            prefix[i+1]=prefix[i]+nums[i];
        }
        for(int i = 0;i<n;i++){
            int lSum=prefix[i];
            int rSum=prefix[n]-prefix[i+1];
            if(lSum==rSum){
                return i;
            }
        }

        return -1;
    }
}
// @lc code=end

