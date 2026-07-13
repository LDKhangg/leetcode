/*
 * @lc app=leetcode id=744 lang=java
 *
 * [744] Find Smallest Letter Greater Than Target
 */

// @lc code=start
class Solution {
    public char nextGreatestLetter(char[] letters, char target) {
        int l=0;
        int r=letters.length-1;
        while(l<=r){
            int m=l+(r-l)/2;
            if(letters[m]<=target){
                l=m+1;
            } else{
                r=m-1;
            }
        }
        return letters[l%letters.length];
    }
}
// @lc code=end

