/**
 * Race condition demo: unsynchronized increments lose updates, synchronized ones don't.
 * Run: javac CounterRace.java && java -cp . CounterRace
 */
public class CounterRace {
    static class Counter {
        int value;

        void incrementUnsafe() {
            value++; // read-modify-write: NOT atomic
        }

        synchronized void incrementSafe() {
            value++;
        }
    }

    static void runTrial(boolean safe, int threads, int perThread) throws InterruptedException {
        Counter c = new Counter();
        Thread[] ts = new Thread[threads];
        for (int t = 0; t < threads; t++) {
            ts[t] = new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    if (safe) {
                        c.incrementSafe();
                    } else {
                        c.incrementUnsafe();
                    }
                }
            });
            ts[t].start();
        }
        for (Thread th : ts) {
            th.join();
        }
        System.out.printf("%-5s expected=%d actual=%d %s%n",
                safe ? "safe" : "unsafe", threads * perThread, c.value,
                c.value == threads * perThread ? "OK" : "LOST UPDATES");
    }

    // TODO: picks — AtomicInteger version, synchronized block vs method, volatile visibility demo
    public static void main(String[] args) throws InterruptedException {
        runTrial(false, 8, 100_000);
        runTrial(true, 8, 100_000);
    }
}
