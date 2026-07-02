/*
 * @lc app=leetcode id=414 lang=java
 *
 * [414] Third Maximum Number
 */

// @lc code=start
class Solution {
    public int thirdMax(int[] nums) {
        TreeSet<Integer> set = new TreeSet<>();
        for(int num:nums){
            set.add(num);
            if(set.size()>3){
                set.remove(set.first());
            }
        }

        return set.size()==3?set.first():set.last();
    }
}
// @lc code=end

