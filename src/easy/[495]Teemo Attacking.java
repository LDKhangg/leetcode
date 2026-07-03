/*
 * @lc app=leetcode id=495 lang=java
 *
 * [495] Teemo Attacking
 */

// @lc code=start
class Solution {
    public int findPoisonedDuration(int[] timeSeries, int duration) {
        int total = 0;
        for(int i =0;i<timeSeries.length-1;i++){
            int gap=timeSeries[i+1]-timeSeries[i];
            total+=+Math.min(duration,gap);
        }
        return total+duration;
    }
}
// @lc code=end

