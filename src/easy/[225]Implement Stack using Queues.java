/*
 * @lc app=leetcode id=225 lang=java
 *
 * [225] Implement Stack using Queues
 */

// @lc code=start
class MyStack {

    private Deque<Integer> stk;

    public MyStack() {
        stk = new LinkedList<>();
    }
    
    public void push(int x) {
        stk.offerLast(x);
    }
    
    public int pop() {
        return stk.pollLast();
    }
    
    public int top() {
        return stk.peekLast();
    }
    
    public boolean empty() {
        return stk.isEmpty();
    }
}

/**
 * Your MyStack object will be instantiated and called as such:
 * MyStack obj = new MyStack();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.top();
 * boolean param_4 = obj.empty();
 */
// @lc code=end

