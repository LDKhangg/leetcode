//Given a string containing digits from 2-9 inclusive, return all possible 
//letter combinations that the number could represent. Return the answer in any order. 
//
//
// A mapping of digits to letters (just like on the telephone buttons) is given 
//below. Note that 1 does not map to any letters. 
// 
// 
// Example 1: 
//
// 
//Input: digits = "23"
//Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
// 
//
// Example 2: 
//
// 
//Input: digits = "2"
//Output: ["a","b","c"]
// 
//
// 
// Constraints: 
//
// 
// 1 <= digits.length <= 4 
// digits[i] is a digit in the range ['2', '9']. 
// 
//
// Related Topics Hash Table String Backtracking 👍 21099 👎 1138


import java.util.ArrayList;
import java.util.List;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    List<String> res=new ArrayList<>();
    String[] phoneMap = {"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
    public List<String> letterCombinations(String digits) {
        if(digits==null || digits.isEmpty()){
            return res;
        }
        recursive(digits,0,new StringBuilder());
        return res;
    }
    private void recursive(String digits,int index,StringBuilder sb){
        if(index==digits.length()){
            res.add(sb.toString());
            return;
        }
        char digit = digits.charAt(index);
        String letter = phoneMap[digit-'0'];
        for(int i=0;i<letter.length();i++){
            sb.append(letter.charAt(i));
            recursive(digits,index+1,sb);
            sb.setLength(sb.length()-1);
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
