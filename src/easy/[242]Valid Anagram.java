/*
 * @lc app=leetcode id=242 lang=java
 *
 * [242] Valid Anagram
 */

// @lc code=start
class Solution {
    public boolean isAnagram(String s, String t) {
        if(s==null || t== null){
            return false;
        }
        if(sortedString(s).equals(sortedString(t))){
            return true;
        }
        return false;    
    }


    private String sortedString(String s){
        char[] charArr = s.toCharArray();
        Arrays.sort(charArr);
        return new String(charArr);
    }
}
// @lc code=end

