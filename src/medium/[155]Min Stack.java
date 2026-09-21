/*
 * @lc app=leetcode id=155 lang=java
 *
 * [155] Min Stack
 */

// @lc code=start

import java.util.ArrayDeque;
import java.util.Deque;

class MinStack {
    // Cách dùng collections:
    // Deque<Integer> stk1;
    // Deque<Integer> stk2;

    // public MinStack() {
    // stk1 = new ArrayDeque<>();
    // stk2 = new ArrayDeque<>();
    // }

    // public void push(int value) {
    // if (!stk2.isEmpty()) {
    // if (stk2.getLast() >= value) {
    // stk2.addLast(value);
    // }
    // } else {
    // stk2.addLast(value);
    // }
    // stk1.addLast(value);
    // }

    // public void pop() {
    // int value = stk1.removeLast();
    // if (value == stk2.getLast()) {
    // stk2.removeLast();
    // }
    // }

    // public int top() {
    // return stk1.getLast();
    // }

    // public int getMin() {
    // return stk2.getLast();
    // }

    int[] stack;
    int[] minStack;
    private int head;

    public MinStack() {
        stack = new int[30000];
        minStack = new int[30000];
        head = -1;
    }

    public void push(int value) {
        head++;
        stack[head] = value;
        if (head == 0) {
            minStack[head] = value;
        } else {
            minStack[head] = Math.min(minStack[head - 1], value);
        }
    }

    public int top() {
        return stack[head];
    }

    public int getMin() {
        return minStack[head];
    }

    public void pop() {
        stack[head] = 0;
        minStack[head] = 0;
        head--;
    }

}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(value);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.getMin();
 */
// @lc code=end
