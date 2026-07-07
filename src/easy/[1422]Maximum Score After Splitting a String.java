/*
 * @lc app=leetcode id=1422 lang=java
 *
 * [1422] Maximum Score After Splitting a String
 */

// @lc code=start
class Solution {
    public int maxScore(String s) {
        int zerosLeft=0,onesRight=0;
        for(int i = 0;i<s.length();i++){
            if(s.charAt(i)=='1'){
                onesRight++;
            }
        }
        int max = 0;
        for(int i = 0;i<s.length()-1;i++){
            if(s.charAt(i)=='0'){
                zerosLeft++;
            }else{
                onesRight--;
            }
            max=Math.max(max,zerosLeft+onesRight);
        }
        return max;
    }
}
// @lc code=end

