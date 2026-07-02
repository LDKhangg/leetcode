/*
 * @lc app=leetcode id=485 lang=java
 *
 * [485] Max Consecutive Ones
 */

// @lc code=start
class Solution {
    public int findMaxConsecutiveOnes(int[] nums) {
        // Stack<Integer> stk = new Stack<>();
        // stk.push(-1);
        // int max=0;
        // for(int i = 0 ; i < nums.length;i++){
        //     if(nums[i]==0){
        //         if(stk.isEmpty())
        //             stk.push(i);
        //         else {
        //             max=Math.max(max,i-1-stk.peek());
        //             stk.pop();
        //             stk.push(i);
        //         }
        //     }
        // }
        // if(!stk.isEmpty()&&nums[nums.length-1]!=0){
        //         max=Math.max(max,nums.length-1-stk.peek());
        //     }
        // return max;
        int maxCount = 0;
        int count = 0;
        for (int num : nums) {
            if (num == 1) {
                count++;
                maxCount = Math.max(maxCount, count);
            } else {
                count = 0;
            }
        }
        return maxCount;
    
    }
}
// @lc code=end

