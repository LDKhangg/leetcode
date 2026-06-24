//Given an array of integers nums sorted in non-decreasing order, find the 
//starting and ending position of a given target value. 
//
// If target is not found in the array, return [-1, -1]. 
//
// You must write an algorithm with O(log n) runtime complexity. 
//
// 
// Example 1: 
// Input: nums = [5,7,7,8,8,10], target = 8
//Output: [3,4]
// 
// Example 2: 
// Input: nums = [5,7,7,8,8,10], target = 6
//Output: [-1,-1]
// 
// Example 3: 
// Input: nums = [], target = 0
//Output: [-1,-1]
// 
// 
// Constraints: 
//
// 
// 0 <= nums.length <= 10⁵ 
// -10⁹ <= nums[i] <= 10⁹ 
// nums is a non-decreasing array. 
// -10⁹ <= target <= 10⁹ 
// 
//
// Related Topics Array Binary Search 👍 23464 👎 654


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int[] searchRange(int[] nums, int target) {
        int first = findTarget(nums, target, true);
        int last = findTarget(nums, target, false);
        return new int[] { first, last };
    }
    private int findTarget(int[] nums,int target,boolean isFirst){
        int l=0, r=nums.length-1,res=-1;
        while(l<=r){
            int m = (l+r)/2;
            if(nums[m]==target) {
                res = m;
                if (isFirst)
                    r = m - 1;
                    else
                    l = m + 1;
            } else if(nums[m]>=target) r=m-1;
            else l=m+1;
        }
        return res;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
