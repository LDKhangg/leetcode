/*
 * @lc app=leetcode id=169 lang=java
 *
 * [169] Majority Element
 */

// @lc code=start
class Solution {
    public int majorityElement(int[] nums) {
        int n= nums.length;
        int candidates=0;
        int count =0;
        for(int i = 0;i<n;i++){
            if(count==0){
                candidates=nums[i];
                count++;
            } else{
                if(candidates != nums[i]){
                    count--;
                }
                else{count++;}
            }   
        }
        return candidates;
    }
}
// @lc code=end

