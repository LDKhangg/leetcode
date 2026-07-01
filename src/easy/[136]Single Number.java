/*
 * @lc app=leetcode id=136 lang=java
 *
 * [136] Single Number
 */

// @lc code=start
class Solution {
    public int singleNumber(int[] nums) {
        // if(nums.length<2) return nums[0];

        // Map<Integer,Integer> map = new HashMap<>();
        // for(int i = 0;i<nums.length;i++){
        //     map.put(nums[i],map.getOrDefault(nums[i],0)+1);
        // }
        // for(Map.Entry<Integer,Integer> entry:map.entrySet()){
        //     if(entry.getValue()==1){
        //         return entry.getKey();
        //     }
        // }
        // return 0;
        int result=0;
        for(int i = 0;i<nums.length;i++){
            result^=nums[i];
        }
        return result;
    }
}
// @lc code=end

