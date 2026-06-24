//Given an input string s and a pattern p, implement regular expression 
//matching with support for '.' and '*' where: 
//
// 
// '.' Matches any single character. 
// '*' Matches zero or more of the preceding element. 
// 
//
// Return a boolean indicating whether the matching covers the entire input 
//string (not partial). 
//
// 
// Example 1: 
//
// 
//Input: s = "aa", p = "a"
//Output: false
//Explanation: "a" does not match the entire string "aa".
// 
//
// Example 2: 
//
// 
//Input: s = "aa", p = "a*"
//Output: true
//Explanation: '*' means zero or more of the preceding element, 'a'. Therefore, 
//by repeating 'a' once, it becomes "aa".
// 
//
// Example 3: 
//
// 
//Input: s = "ab", p = ".*"
//Output: true
//Explanation: ".*" means "zero or more (*) of any character (.)".
// 
//
// 
// Constraints: 
//
// 
// 1 <= s.length <= 20 
// 1 <= p.length <= 20 
// s contains only lowercase English letters. 
// p contains only lowercase English letters, '.', and '*'. 
// It is guaranteed for each appearance of the character '*', there will be a 
//previous valid character to match. 
// 
//
// Related Topics String Dynamic Programming Recursion 👍 13389 👎 2408


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public boolean isMatch(String s, String p) {
        Boolean[][] memo = new Boolean[s.length()+1][p.length()];
        return recursive(s,p,0,0,memo);
    }
    private boolean recursive(String s,String p,int sIndex,int pIndex,Boolean[][] memo){
        if(sIndex==s.length() && pIndex== p.length())
            return true;

        if(pIndex>=p.length())
            return false;
        if(memo[sIndex][pIndex]!=null)
            return memo[sIndex][pIndex];
        final boolean matched;
        boolean characterMatch = sIndex<s.length()&&(s.charAt(sIndex)==p.charAt(pIndex)||
                p.charAt(pIndex)=='.');
        boolean nextOneStar= pIndex+1<p.length()&& p.charAt(pIndex+1)=='*';
        if(characterMatch){
            if(nextOneStar){
                matched=recursive(s,p,sIndex,pIndex+2,memo) || recursive(s,p,sIndex+1,pIndex,memo);
            } else {
                matched=recursive(s,p,sIndex+1,pIndex+1,memo);
            }
        } else {
            if(nextOneStar){
                matched=recursive(s,p,sIndex,pIndex+2,memo);
            }
            else{
                matched=false;
            }
        }
        memo[sIndex][pIndex]=matched;
        return matched;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
