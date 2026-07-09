/*
 * @lc app=leetcode id=232 lang=java
 *
 * [232] Implement Queue using Stacks
 */

// @lc code=start
class MyQueue {
    Stack<Integer> input;
    Stack<Integer> output;
    public MyQueue() {
        input=new Stack<>();
        output=new Stack<>();
    }
    
    public void push(int x) {
        input.push(x);
    }
    
    public int pop() {
        moveInputToOutput();
        return output.pop();
    }
    
    public int peek() {
        moveInputToOutput();
        return output.peek();
    }
    
    public boolean empty() {
        return input.isEmpty()&&output.isEmpty();
    }

    private void moveInputToOutput(){
        while(!input.isEmpty()){
            output.push(input.pop());
        }
    }
}

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */
// @lc code=end

