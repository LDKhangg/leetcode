/*
 * @lc app=leetcode id=1893 lang=java
 *
 * [1893] Check if All the Integers in a Range Are Covered
 */

// @lc code=start
class Solution {
    public boolean isCovered(int[][] ranges, int left, int right) {
        int[] delta = new int[52];
        for(int[] range:ranges){
            delta[range[0]]+=1;
            delta[range[1]+1]-=1;
            }
        
        int coverage=0;
        for(int i =1;i<=51;i++){
            coverage+=delta[i];
            if(i>=left&&i<=right&&coverage==0){
                return false;
            }
        }
        return true;
    }
}
// @lc code=end

