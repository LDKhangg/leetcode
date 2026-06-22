//Given n pairs of parentheses, write a function to generate all combinations 
//of well-formed parentheses. 
//
// 
// Example 1: 
// Input: n = 3
//Output: ["((()))","(()())","(())()","()(())","()()()"]
// 
// Example 2: 
// Input: n = 1
//Output: ["()"]
// 
// 
// Constraints: 
//
// 
// 1 <= n <= 8 
// 
//
// Related Topics String Dynamic Programming Backtracking 👍 23383 👎 1088


import java.util.ArrayList;
import java.util.List;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    List<String> res = new ArrayList<>();
    public List<String> generateParenthesis(int n) {
        dfs(n,0,new StringBuilder());
        return res;
    }
    private void dfs(int n,int index,StringBuilder sb){
        if(index==0 &&n==0){
            res.add(sb.toString());
            return;
        }
        if(n >0){
            sb.append('(');
            dfs(n-1,index+1,sb);
            sb.setLength(sb.length()-1);
        }
        if(index>0 ){
            sb.append(')');
            dfs(n,index-1,sb);
            sb.setLength(sb.length()-1);
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
