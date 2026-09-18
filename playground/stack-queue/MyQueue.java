import java.util.NoSuchElementException;

/**
 * Circular-buffer queue (FIFO). The structure behind BFS levels and sliding windows.
 * Run: javac MyQueue.java && java -cp . MyQueue
 */
public class MyQueue {
    private final int[] data;
    private int head;
    private int size;

    public MyQueue(int capacity) {
        data = new int[capacity];
    }

    public void offer(int v) {
        if (size == data.length) {
            throw new IllegalStateException("full");
        }
        data[(head + size) % data.length] = v;
        size++;
    }

    public int poll() {
        if (size == 0) {
            throw new NoSuchElementException("empty");
        }
        int out = data[head];
        head = (head + 1) % data.length;
        size--;
        return out;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // TODO: queue via two stacks (LeetCode 232), deque with head/tail pointers
    public static void main(String[] args) {
        MyQueue q = new MyQueue(3);
        q.offer(1);
        q.offer(2);
        q.offer(3);
        System.out.println("poll=" + q.poll());
        q.offer(4); // wraps around the ring
        while (!q.isEmpty()) {
            System.out.print(q.poll() + " ");
        }
        System.out.println();
    }
}
