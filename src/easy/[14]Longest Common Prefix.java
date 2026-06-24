//Write a function to find the longest common prefix string amongst an array of 
//strings. 
//
// If there is no common prefix, return an empty string "". 
//
// 
// Example 1: 
//
// 
//Input: strs = ["flower","flow","flight"]
//Output: "fl"
// 
//
// Example 2: 
//
// 
//Input: strs = ["dog","racecar","car"]
//Output: ""
//Explanation: There is no common prefix among the input strings.
// 
//
// 
// Constraints: 
//
// 
// 1 <= strs.length <= 200 
// 0 <= strs[i].length <= 200 
// strs[i] consists of only lowercase English letters if it is non-empty. 
// 
//
// Related Topics Array String Trie 👍 21537 👎 4944


import java.util.Arrays;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public String longestCommonPrefix(String[] strs) {

        StringBuilder sb = new StringBuilder();
        Arrays.sort(strs, (a, b) -> a.length() - b.length());
        int i = 0;
        while(i<strs[0].length()){
            sb.append(strs[0].charAt(i));
            for (String str : strs) {
                if (str.charAt(i) != sb.charAt(i)) {
                    sb.setLength(i);
                    return sb.toString();
                }
            }
            i++;
        }
        return sb.toString();
    }
}
//leetcode submit region end(Prohibit modification and deletion)
