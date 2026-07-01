/*
 * @lc app=leetcode id=121 lang=java
 *
 * [121] Best Time to Buy and Sell Stock
 */

// @lc code=start
class Solution {
    public int maxProfit(int[] prices) {
        int minPrice=prices[0];
        int maxProfit=0;
        for(int i = 1;i<prices.length;i++){
            maxProfit=Math.max(maxProfit,prices[i]-minPrice);
            minPrice=Math.min(minPrice,prices[i]);
        }
        return maxProfit;
    }
}
// @lc code=end

