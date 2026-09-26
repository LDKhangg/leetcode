import java.util.concurrent.atomic.AtomicInteger;

/**
 * L1 — Race condition: mất update khi ++ không nguyên tử.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - value++ là 3 bước: READ -> MODIFY -> WRITE, KHÔNG nguyên tử (atomic).
 *   2 thread cùng READ giá trị cũ rồi cùng WRITE -> 1 update bị mất (lost update).
 * - synchronized đảm bảo mutual exclusion: cùng lúc chỉ 1 thread vào được,
 *   nên chuỗi read-modify-write thành nguyên tử -> không mất update.
 * - AtomicInteger dùng CAS (compare-and-set, không khóa) để làm ++ nguyên tử,
 *   nhanh hơn synchronized khi tranh chấp cao.
 * - volatile chỉ đảm bảo VISIBILITY (thấy giá trị mới), KHÔNG đảm bảo ATOMICITY
 *   (vẫn mất update vì read-modify-write tách rời). Đừng nhầm 2 khái niệm này!
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L1_RaceCounter.java && java -cp . L1_RaceCounter
 */
public class L1_RaceCounter {

    static int passed = 0, failed = 0;

    static void check(boolean cond, String name, String hint) {
        if (cond) {
            passed++;
            System.out.println("PASS " + name);
        } else {
            failed++;
            System.out.println("FAIL " + name + "  <-- " + hint);
        }
    }

    static class Counter {
        int value;

        void incrementUnsafe() {
            value++; // read-modify-write: NOT atomic
        }

        // TODO-1: method này đang THIẾU synchronized cố ý (xem đề bên dưới).
        synchronized void incrementSafe() {
            value++;
        }
    }

    static class VolatileCounter {
        volatile int value;

        void increment() {
            value++; // volatile vẫn KHÔNG nguyên tử!
        }
    }

    static int runTrial(Counter c, boolean safe, int threads, int perThread)
            throws InterruptedException {
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
        return c.value;
    }

    static int runAtomicTrial(AtomicInteger c, boolean useAtomicOp, int threads, int perThread)
            throws InterruptedException {
        Thread[] ts = new Thread[threads];
        for (int t = 0; t < threads; t++) {
            ts[t] = new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    if (useAtomicOp) {
                        c.incrementAndGet(); // nguyên tử, đúng
                    } else {
                        c.incrementAndGet(); // TODO-2: BUG cố ý — đọc mà không tăng
                    }
                }
            });
            ts[t].start();
        }
        for (Thread th : ts) {
            th.join();
        }
        return c.get();
    }

    static int runVolatileTrial(int threads, int perThread) throws InterruptedException {
        VolatileCounter c = new VolatileCounter();
        Thread[] ts = new Thread[threads];
        for (int t = 0; t < threads; t++) {
            ts[t] = new Thread(() -> {
                for (int i = 0; i < perThread; i++) {
                    c.increment();
                }
            });
            ts[t].start();
        }
        for (Thread th : ts) {
            th.join();
        }
        return c.value;
    }

    public static void main(String[] args) throws InterruptedException {
        final int THREADS = 8;
        final int PER_THREAD = 100_000;
        final int EXPECTED = THREADS * PER_THREAD;

        System.out.println("--- Bài 0 (mẫu, đọc hiểu: unsafe mất update) ---");
        Counter demo = new Counter();
        int unsafeActual = runTrial(demo, false, THREADS, PER_THREAD);
        System.out.printf("  unsafe: expected=%d actual=%d %s%n",
                EXPECTED, unsafeActual,
                unsafeActual == EXPECTED ? "OK (hiếm)" : "LOST UPDATES (dự kiến)");
        System.out.println("  (Bài 0 chỉ demo để nhìn thấy LOST UPDATES, không check PASS/FAIL.)");

        System.out.println("\n--- TODO-1: synchronized cho incrementSafe ---");
        // ĐỀ: incrementSafe() hiện THIẾU synchronized nên vẫn mất update.
        // KẾT QUẢ MONG ĐỢI: actual == expected (800_000).
        // - Hint 1: thêm từ khóa synchronized vào khai báo method:
        //     synchronized void incrementSafe() { value++; }
        // - Hint 2: synchronized trên method instance = khóa trên `this`,
        //   mọi thread dùng chung object Counter sẽ xếp hàng.
        // - LỖI THƯỜNG GẶP:
        //     synchronized(value) -> sai, value là int, không khóa được; phải khóa trên object
        //     chỉ synchronized chỗ gọi (caller) mà quên method -> vẫn sót đường khác gọi vào
        Counter c1 = new Counter();
        int actual1 = runTrial(c1, true, THREADS, PER_THREAD);
        System.out.printf("  safe: expected=%d actual=%d%n", EXPECTED, actual1);
        check(actual1 == EXPECTED, "T1: synchronized không mất update",
                "gợi ý: thêm synchronized cho incrementSafe()");

        System.out.println("\n--- TODO-2: AtomicInteger thay khóa ---");
        // ĐỀ: runAtomicTrial(c, false, ...) đang dùng c.get() (chỉ đọc, không tăng)
        // nên counter mãi = 0. Sửa lời gọi sao cho mỗi vòng lặp tăng nguyên tử 1 đơn vị.
        // - Hint 1: dùng c.incrementAndGet() thay vì c.get().
        // - Hint 2: sửa tham số useAtomicOp thành true HOẶC sửa nhánh else trong runAtomicTrial.
        //   Cách đơn giản nhất: đổi `false` thành `true` ở dòng gọi bên dưới.
        // - LỖI THƯỜNG GẶP:
        //     c.get() + 1 -> sai, tính xong không ghi lại, vẫn mất update
        //     c.set(c.get() + 1) -> sai, read-modify-write tách rời, vẫn race
        AtomicInteger atomic = new AtomicInteger(0);
        int actual2 = runAtomicTrial(atomic, false, THREADS, PER_THREAD); // TODO-2: sửa false -> true
        System.out.printf("  atomic: expected=%d actual=%d%n", EXPECTED, actual2);
        check(actual2 == EXPECTED, "T2: AtomicInteger tăng nguyên tử đủ số",
                "gợi ý: dùng incrementAndGet() (đổi false thành true)");

        System.out.println("\n--- TODO-3: volatile có fix được race không? ---");
        // ĐỀ: nhiều bạn nghĩ "thêm volatile là hết race". Đoạn dưới đo thực tế:
        // VolatileCounter.value là volatile nhưng increment() vẫn là value++ (3 bước).
        // - Hint 1: volatile chỉ fix VISIBILITY, không fix ATOMICITY -> vẫn LOST UPDATES.
        // - Hint 2: đáp án đúng là volatileFixesRace = false.
        // - LỖI THƯỜNG GẶP:
        //     "volatile làm ++ thành nguyên tử" -> SAI, ++ vẫn tách rời
        //     "dùng volatile thay synchronized cho counter" -> SAI, phải dùng AtomicInteger/khóa
        boolean volatileFixesRace = false; // TODO-3: sửa thành false
        int volatileActual = runVolatileTrial(THREADS, PER_THREAD);
        System.out.printf("  volatile: expected=%d actual=%d %s%n",
                EXPECTED, volatileActual,
                volatileActual == EXPECTED ? "OK (hiếm)" : "LOST UPDATES (dự kiến)");
        check(!volatileFixesRace, "T3: hiểu volatile KHÔNG fix được lost-update",
                "gợi ý: volatile chỉ là visibility, ++ vẫn cần atomicity (đặt = false)");
        check(volatileActual != EXPECTED || !volatileFixesRace, "T3: volatile demo vẫn mất update (atomicity)",
                "nếu hãn hữu bằng nhau thì vẫn PASS nhờ nửa sau của ||, chạy lại sẽ thấy LOST");

        System.out.println("\n--- TODO-4: synchronized block (khóa hẹp) ---");
        // ĐỀ: biến lockedOps đang tăng KHÔNG khóa nên mất update. Bọc nó trong
        // synchronized block khóa trên đúng object để đủ số.
        // - Hint 1: synchronized (lock) { lockedOps[0]++; }
        // - Hint 2: mọi thread phải khóa CÙNG 1 object `lock`, không được new Object() mỗi lần.
        // - LỖI THƯỜNG GẶP:
        //     synchronized (new Object()) -> mỗi thread khóa object khác nhau = không khóa gì cả
        //     quên bọc cả read lẫn write -> chỉ bọc write vẫn race
        final Object lock = new Object();
        final int[] lockedOps = {0};
        Thread[] ts = new Thread[THREADS];
        for (int t = 0; t < THREADS; t++) {
            ts[t] = new Thread(() -> {
                for (int i = 0; i < PER_THREAD; i++) {
                    synchronized (lock){lockedOps[0]++;} // TODO-4: bọc dòng này trong synchronized (lock) { ... }
                }
            });
            ts[t].start();
        }
        for (Thread th : ts) {
            th.join();
        }
        System.out.printf("  block: expected=%d actual=%d%n", EXPECTED, lockedOps[0]);
        check(lockedOps[0] == EXPECTED, "T4: synchronized block đủ số",
                "gợi ý: synchronized (lock) { lockedOps[0]++; }");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao 8 thread x 100k hầu như luôn mất update, còn 2 thread x 100 lần thì hên xui?");
        System.out.println(" 2. synchronized method vs synchronized block khác gì? Khi nào dùng block?");
        System.out.println(" 3. AtomicInteger nhanh hơn synchronized khi nào, chậm hơn khi nào?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: synchronized void incrementSafe() { value++; }
     *  T2: runAtomicTrial(atomic, true, THREADS, PER_THREAD);
     *      (hoặc trong nhánh else dùng c.incrementAndGet();)
     *  T3: boolean volatileFixesRace = false;
     *  T4: synchronized (lock) { lockedOps[0]++; }
     */
}
