/*
 * @lc app=leetcode id=219 lang=java
 *
 * [219] Contains Duplicate II
 */

// @lc code=start
class Solution {
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i = 0 ; i < nums.length;i++){
            if(!map.containsKey(nums[i])){
                map.put(nums[i],i);
            } else{
                if(i-map.get(nums[i])>k){
                    map.put(nums[i],i);
                }else{
                    return true;
                }
            }
        }
        return false;
    }
}
// @lc code=end

