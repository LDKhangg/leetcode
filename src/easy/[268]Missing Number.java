/*
 * @lc app=leetcode id=268 lang=java
 *
 * [268] Missing Number
 */

// @lc code=start
class Solution {
    public int missingNumber(int[] nums) {
        int n = nums.length;
        //solve 1:
        // Arrays.sort(nums);
        // if (nums[0] != 0) return 0;
        // for(int i = 0 ; i < n-1 ; i++){
        //     if(nums[i]+1!=nums[i+1])
        //     return nums[i]+1;
        // }
        // return n;
        //solve 2:
        // int sum=0;
        // for(int i = 0 ; i <n;i++){
        //     sum+=nums[i];
        // }
        // return (n*(n+1)/2)-sum;
        //solve 3:
        int result = n;
        for(int i = 0 ; i < n;i++){
            result^=i^nums[i];
        }
        return result;
    }
}
// @lc code=end

