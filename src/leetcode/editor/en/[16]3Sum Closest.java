//Given an integer array nums of length n and an integer target, find three 
//integers at distinct indices in nums such that the sum is closest to target. 
//
// Return the sum of the three integers. 
//
// You may assume that each input would have exactly one solution. 
//
// 
// Example 1: 
//
// 
//Input: nums = [-1,2,1,-4], target = 1
//Output: 2
//Explanation: The sum that is closest to the target is 2. (-1 + 2 + 1 = 2).
// 
//
// Example 2: 
//
// 
//Input: nums = [0,0,0], target = 1
//Output: 0
//Explanation: The sum that is closest to the target is 0. (0 + 0 + 0 = 0).
// 
//
// 
// Constraints: 
//
// 
// 3 <= nums.length <= 500 
// -1000 <= nums[i] <= 1000 
// -10⁴ <= target <= 10⁴ 
// 
//
// Related Topics Array Two Pointers Sorting 👍 11720 👎 623


import java.util.Arrays;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int Sum = nums[0] + nums[1] + nums[2];
        for(int i = 0;i<nums.length;i++){
            int l=i+1,r=nums.length-1;
            while(l<r){
                int currentSum = nums[i]+nums[l]+nums[r];
                if (Math.abs(currentSum - target) < Math.abs(Sum - target)) {
                    Sum = currentSum;
                }
                if(currentSum == target){
                    return nums[i]+nums[l]+nums[r];
                }
                if(currentSum <= target){
                    l++;
                }
                else {
                    r--;
                }
            }
        }
        return Sum;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
