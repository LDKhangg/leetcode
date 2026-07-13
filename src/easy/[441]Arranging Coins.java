/*
 * @lc app=leetcode id=441 lang=java
 *
 * [441] Arranging Coins
 */

// @lc code=start
class Solution {
    public int arrangeCoins(int n) {
        int l=1;
        int r=n;
        while(l<=r){
            int m = l+(r-l)/2;
            long coins = (long) m*(m+1)/2;
            if(coins==n){
                return m;
            }
            else if(coins<n){
                l=m+1;
            }
            else{
                r=m-1;
            }
        }
        return r;        
    }
}
// @lc code=end

