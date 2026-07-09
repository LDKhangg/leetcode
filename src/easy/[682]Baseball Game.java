/*
 * @lc app=leetcode id=682 lang=java
 *
 * [682] Baseball Game
 */

// @lc code=start
class Solution {
    public int calPoints(String[] operations) {
        Stack<Integer> stk=new Stack<>();
        for(String op:operations){
            if(op.equals("C")) stk.pop();
            else if(op.equals("D")) stk.push(stk.peek()*2);
            else if(op.equals("+")) {
                int top = stk.pop();
                int sum = top+stk.peek();
                stk.push(top);
                stk.push(sum);
            } else{
                stk.push(Integer.parseInt(op));
            }
        }
        int total =0;
        while(!stk.isEmpty()) total+=stk.pop();

        return total;
    }
}
// @lc code=end

