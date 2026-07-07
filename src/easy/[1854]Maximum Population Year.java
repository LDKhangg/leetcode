/*
 * @lc app=leetcode id=1854 lang=java
 *
 * [1854] Maximum Population Year
 */

// @lc code=start
class Solution {
    public int maximumPopulation(int[][] logs) {
        int[] delta = new int[2051];
        for(int[] log :logs){
            int birth=log[0];
            int death=log[1];
            delta[birth]+=1;
            delta[death]-=1;
        }
        
        int currentPop=0;
        int maxPop=0;
        int bestYear=0;
        for(int year=1950;year<=2050;year++){
            currentPop+=delta[year];
            if(currentPop>maxPop){
                maxPop=currentPop;
                bestYear=year;
            }
        }
        return bestYear;
    }
}
// @lc code=end

