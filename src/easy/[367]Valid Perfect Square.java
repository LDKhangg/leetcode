/*
 * @lc app=leetcode id=367 lang=java
 *
 * [367] Valid Perfect Square
 */

// @lc code=start
class Solution {
    public boolean isPerfectSquare(int num) {
      int l=1;
      int r= num;
      while(l<=r){
        int m = l+(r-l)/2;
        long p =(long) m*m;
        if(p==num){
            return true;
        } else if(p<num){
            l=m+1;
        } else{
            r=m-1;
        }
        }
        return false;
      }  
}
// @lc code=end

