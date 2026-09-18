import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Fixed-capacity array stack (LIFO). The structure behind Valid Parentheses (20), Min Stack (155).
 * Run: javac MyStack.java && java -cp . MyStack
 */
public class MyStack {
    private int[] data;
    private int top = -1;

    public MyStack(int capacity) {
        data = new int[capacity];
    }

    public void push(int v) {
        if (top + 1 == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[++top] = v;
    }

    public int pop() {
        if (top < 0) {
            throw new NoSuchElementException("empty");
        }
        return data[top--];
    }

    public int peek() {
        if (top < 0) {
            throw new NoSuchElementException("empty");
        }
        return data[top];
    }

    public boolean isEmpty() {
        return top < 0;
    }

    // TODO: MinStack with O(1) getMin (LeetCode 155) — pair each entry with running min
    public static void main(String[] args) {
        MyStack s = new MyStack(2);
        s.push(10);
        s.push(20);
        s.push(30); // forces growth
        System.out.println("peek=" + s.peek());
        while (!s.isEmpty()) {
            System.out.print(s.pop() + " ");
        }
        System.out.println();
    }
}
