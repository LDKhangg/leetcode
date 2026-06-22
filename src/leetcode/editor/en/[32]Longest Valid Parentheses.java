//Given a string containing just the characters '(' and ')', return the length 
//of the longest valid (well-formed) parentheses substring. 
//
// 
// Example 1: 
//
// 
//Input: s = "(()"
//Output: 2
//Explanation: The longest valid parentheses substring is "()".
// 
//
// Example 2: 
//
// 
//Input: s = ")()())"
//Output: 4
//Explanation: The longest valid parentheses substring is "()()".
// 
//
// Example 3: 
//
// 
//Input: s = ""
//Output: 0
// 
//
// 
// Constraints: 
//
// 
// 0 <= s.length <= 3 * 10⁴ 
// s[i] is '(', or ')'. 
// 
//
// Related Topics String Dynamic Programming Stack 👍 13371 👎 468


import java.util.Stack;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    Stack<Integer> stack = new Stack<>();
    public int longestValidParentheses(String s) {
        if (s.length() == 0 || s.isEmpty())
            return 0;
        stack.push(-1);
        int max=0;
        for(int i = 0 ; i<s.length();i++){
            char c = s.charAt(i);
            if(c=='('){
                stack.push(i);
            } else {
                stack.pop();
                if(stack.isEmpty()){
                    stack.push(i);
                }
                else{
                    max=Math.max(max,i-stack.peek());
                }
            }
        }
        return max;
    }


    }

//leetcode submit region end(Prohibit modification and deletion)
