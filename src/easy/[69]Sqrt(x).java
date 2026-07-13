/*
 * @lc app=leetcode id=69 lang=java
 *
 * [69] Sqrt(x)
 */

// @lc code=start
class Solution {
    public int mySqrt(int x) {
       int l = 0;
       int r = x;

       while(l<=r){
        int m = l+(r-l)/2;
        long p=(long) m*m;
        if(p==x){
            return m;
        } else if(p<x){
            l=m+1;
        } else{
            r=m-1;
        }
       }
       return r;
    }
}
// @lc code=end

