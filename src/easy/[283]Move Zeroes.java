/*
 * @lc app=leetcode id=283 lang=java
 *
 * [283] Move Zeroes
 */

// @lc code=start
class Solution {
    public void moveZeroes(int[] nums) {
        int n = nums.length;
        int slow=0;
        for(int fast = 0 ; fast < n;fast++){
            if(nums[fast]!=0){
                int temp = nums[slow];
                nums[slow]=nums[fast];
                nums[fast] = temp;
                slow++;
            }
        }
    }
}
// @lc code=end

