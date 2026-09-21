/*
 * @lc app=leetcode id=150 lang=java
 *
 * [150] Evaluate Reverse Polish Notation
 */

// @lc code=start

import java.util.ArrayDeque;
import java.util.Deque;

class Solution {
    public int evalRPN(String[] tokens) {
        // Method 1: use collections
        // Deque<Integer> stk = new ArrayDeque<>();
        MyCustomStack stack = new MyCustomStack();
        for (String token : tokens) {
            switch (token) {
                case "+":
                    // stk.addLast(stk.removeLast() + stk.removeLast());
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "*":
                    // stk.addLast(stk.removeLast() * stk.removeLast());
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "-":
                    // int a = stk.removeLast();
                    // int b = stk.removeLast();
                    int b = stack.pop();
                    int a = stack.pop();
                    stack.push(a - b);
                    break;
                case "/":
                    // int x = stk.removeLast();
                    // int y = stk.removeLast();
                    int x = stack.pop();
                    int y = stack.pop();
                    stack.push(y / x);
                    break;
                default:
                    stack.push(Integer.parseInt(token));
                    break;
            }
        }
        return stack.peek();
    }

    private static class MyCustomStack {
        int[] stack;
        private int head;

        public MyCustomStack() {
            head = -1;
            stack = new int[10000];
        }

        public void push(int val) {
            head++;
            stack[head] = val;
        }

        public int pop() {
            int res = stack[head];
            stack[head] = 0;
            head--;
            return res;
        }

        public int peek() {
            return stack[head];
        }
    }
}
// @lc code=end
