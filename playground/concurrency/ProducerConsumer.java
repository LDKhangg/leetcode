import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Classic producer-consumer over a bounded BlockingQueue.
 * Run: javac ProducerConsumer.java && java -cp . ProducerConsumer
 */
public class ProducerConsumer {
    private static final String POISON = "POISON";

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> q = new ArrayBlockingQueue<>(4);

        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 8; i++) {
                    String item = "task-" + i;
                    q.put(item); // blocks when full — natural backpressure
                    System.out.println("produced " + item);
                }
                q.put(POISON);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread consumer = new Thread(() -> {
            try {
                while (true) {
                    String item = q.take(); // blocks when empty
                    if (POISON.equals(item)) {
                        break;
                    }
                    System.out.println("  consumed " + item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        System.out.println("done, left in queue=" + q.size());
    }

    // TODO: wait()/notify() hand-rolled version, ExecutorService + CompletableFuture variant
}
