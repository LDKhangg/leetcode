import java.util.LinkedList;
import java.util.Queue;

/**
 * L2 — Hand-rolled bounded queue bằng wait()/notifyAll().
 * Xong bài này hãy so sánh với ProducerConsumer.java (BlockingQueue).
 *
 * Mục tiêu phỏng vấn: monitor lock, wait/notify, spurious wakeup (while, không phải if),
 * vì sao notifyAll thay vì notify.
 *
 * Cách luyện:
 *  1. Chạy bản mẫu đã đúng, đọc hiểu put/take.
 *  2. Làm TODO-1: đổi `while` thành `if`, chạy stress xem có lỗi không, giải thích.
 *  3. Làm TODO-2: đổi `notifyAll()` thành `notify()`, đoán khi nào kẹt với nhiều consumer.
 *
 * Run: javac WaitNotifyQueue.java && java -cp . WaitNotifyQueue
 */
public class WaitNotifyQueue<T> {
    private final Queue<T> q = new LinkedList<>();
    private final int capacity;

    public WaitNotifyQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T item) throws InterruptedException {
        while (q.size() == capacity) { // TODO-1: thử đổi thành `if` rồi stress-test
            wait();
        }
        q.add(item);
        notifyAll(); // TODO-2: thử đổi thành notify(), đoán khi nào kẹt
    }

    public synchronized T take() throws InterruptedException {
        while (q.isEmpty()) { // TODO-1: tương tự ở đây
            wait();
        }
        T item = q.poll();
        notifyAll();
        return item;
    }

    // ---- demo ----
    public static void main(String[] args) throws InterruptedException {
        WaitNotifyQueue<String> q = new WaitNotifyQueue<>(3);

        Thread p1 = new Thread(() -> produce(q, "P1", 1, 5));
        Thread p2 = new Thread(() -> produce(q, "P2", 6, 10));
        Thread c1 = new Thread(() -> consume(q, "C1", 5));
        Thread c2 = new Thread(() -> consume(q, "C2", 5));

        p1.start(); p2.start(); c1.start(); c2.start();
        p1.join(); p2.join(); c1.join(); c2.join();
        System.out.println("done. Hỏi: vì sao phải dùng while + notifyAll? (xem TODO)");
    }

    static void produce(WaitNotifyQueue<String> q, String name, int from, int to) {
        try {
            for (int i = from; i <= to; i++) {
                q.put("task-" + i);
                System.out.println(name + " produced task-" + i);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    static void consume(WaitNotifyQueue<String> q, String name, int count) {
        try {
            for (int i = 0; i < count; i++) {
                String item = q.take();
                System.out.println("  " + name + " consumed " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
