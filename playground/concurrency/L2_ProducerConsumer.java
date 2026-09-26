import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * L2 — Producer-Consumer trên BlockingQueue + poison pill.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - BlockingQueue chặn (block) tự nhiên: put() chờ khi đầy, take() chờ khi rỗng.
 *   Queue đầy -> producer tự chậm lại = backpressure, không cần code tay.
 * - Poison pill = 1 phần tử đặc biệt ("POISON") producer gửi cuối cùng để báo
 *   consumer "hết việc, thoát vòng lặp". QUÊN gửi poison -> consumer chờ mãi.
 * - poll(timeout) trả về null khi hết giờ thay vì treo; take() treo tới khi có hàng.
 *   Bài này dùng poll(2s) để bản lỗi FAIL nhanh thay vì treo máy.
 * - 1 producer + 1 consumer + queue FIFO -> thứ tự nhận = thứ tự gửi, không mất tin.
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L2_ProducerConsumer.java && java -cp . L2_ProducerConsumer
 */
public class L2_ProducerConsumer {

    static final String POISON = "POISON";
    static final int CAPACITY = 4;

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

    /** Producer mẫu (đúng): gửi N message rồi gửi POISON. */
    static Thread producer(BlockingQueue<String> q, int n, boolean sendPoison) {
        return new Thread(() -> {
            try {
                for (int i = 1; i <= n; i++) {
                    q.put("task-" + i);
                }
                if (sendPoison) {
                    q.put(POISON);
                }
                // TODO-1: bản lỗi truyền sendPoison=false nên thiếu POISON (xem đề bên dưới).
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu: pipeline đúng) ---");
        BlockingQueue<String> q0 = new ArrayBlockingQueue<>(CAPACITY);
        List<String> consumed0 = new ArrayList<>();
        Thread p0 = producer(q0, 5, true);
        Thread c0 = new Thread(() -> {
            try {
                while (true) {
                    String item = q0.take();
                    if (POISON.equals(item)) {
                        break;
                    }
                    consumed0.add(item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        p0.start();
        c0.start();
        p0.join();
        c0.join();
        System.out.println("  mẫu consumed=" + consumed0 + " left=" + q0.size());
        check(consumed0.size() == 5, "mẫu: nhận đủ 5 message", "");

        System.out.println("\n--- TODO-1: producer QUÊN poison pill ---");
        // ĐỀ: producer dưới cố ý truyền sendPoison=false nên consumer poll(2s)
        // sẽ nhận null (timeout). Sửa false -> true để consumer gặp POISON.
        // - Hint 1: chỉ cần đổi `false` thành `true` ở dòng producer(q1, 5, ...).
        // - Hint 2: consumer dùng poll(2, SECONDS) để bản lỗi FAIL sau 2s thay vì treo.
        //   KHÔNG đổi poll thành take() ở bản lỗi — take() sẽ treo máy khi thiếu poison!
        // - LỖI THƯỜNG GẶP:
        //     quên put POISON khi producer xong sớm / bị exception -> consumer treo
        //     so sánh poison bằng == thay vì equals -> không nhận ra pill
        BlockingQueue<String> q1 = new ArrayBlockingQueue<>(CAPACITY);
        Thread p1 = producer(q1, 5, false); // TODO-1: sửa false -> true
        final String[] terminal1 = {null};
        Thread c1 = new Thread(() -> {
            try {
                while (true) {
                    String item = q1.poll(2, TimeUnit.SECONDS); // KHÔNG đổi thành take()
                    if (item == null) {
                        terminal1[0] = null; // timeout = lỗi thiếu poison
                        break;
                    }
                    if (POISON.equals(item)) {
                        terminal1[0] = item;
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        p1.start();
        c1.start();
        p1.join();
        c1.join(5000);
        check(POISON.equals(terminal1[0]), "T1: consumer gặp POISON (không timeout)",
                "gợi ý: producer quên put POISON (đổi false thành true)");

        System.out.println("\n--- TODO-2: không mất message, đúng thứ tự ---");
        // ĐỀ: consumer dưới đang "quên" add vào consumed2 (list mãi rỗng).
        // Thêm dòng add để nhận đủ 8 message theo đúng thứ tự gửi.
        // - Hint 1: trong vòng lặp, sau khi loại POISON, gọi consumed2.add(item).
        // - Hint 2: 1 producer + 1 consumer + FIFO = thứ tự được giữ nguyên.
        // - LỖI THƯỜNG GẶP:
        //     add cả POISON vào list -> list thừa 1 phần tử, equals() sai
        //     dùng poll mà quên check null -> NullPointerException khi producer lỗi
        BlockingQueue<String> q2 = new ArrayBlockingQueue<>(CAPACITY);
        List<String> consumed2 = new ArrayList<>();
        Thread p2 = producer(q2, 8, true);
        Thread c2 = new Thread(() -> {
            try {
                while (true) {
                    String item = q2.poll(2, TimeUnit.SECONDS);
                    if (item == null || POISON.equals(item)) {
                        break;
                    }
                    // TODO-2: thêm consumed2.add(item); ở đây (đang thiếu cố ý)
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        p2.start();
        c2.start();
        p2.join();
        c2.join(5000);
        List<String> expected2 = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            expected2.add("task-" + i);
        }
        System.out.println("  consumed2=" + consumed2);
        check(consumed2.equals(expected2), "T2: đủ 8 message, đúng thứ tự",
                "gợi ý: quên consumed2.add(item) trong consumer");

        System.out.println("\n--- TODO-3: capacity / backpressure ---");
        // ĐỀ: queue bounded CAPACITY=4 nên size KHÔNG BAO GIỜ vượt 4.
        // Biến claimedMax dưới đang ghi SAI (99). Sửa nó thành giá trị quan sát
        // thực tế (q3.size() sau khi chạy đúng, luôn <= CAPACITY).
        // - Hint 1: sửa thành `int claimedMax = q3.size();` (sau join, queue đã rỗng -> 0).
        // - Hint 2: backpressure = put() tự block khi đầy, nên size không bao giờ > capacity.
        // - LỖI THƯỜNG GẶP:
        //     nghĩ queue "phình" vô hạn -> sai, ArrayBlockingQueue chặn cứng ở capacity
        //     dùng queue không giới hạn (LinkedBlockingQueue mặc định) rồi OOM khi producer nhanh
        BlockingQueue<String> q3 = new ArrayBlockingQueue<>(CAPACITY);
        List<String> consumed3 = new ArrayList<>();
        Thread p3 = producer(q3, 8, true);
        Thread c3 = new Thread(() -> {
            try {
                while (true) {
                    String item = q3.poll(2, TimeUnit.SECONDS);
                    if (item == null || POISON.equals(item)) {
                        break;
                    }
                    consumed3.add(item);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        p3.start();
        c3.start();
        p3.join();
        c3.join(5000);
        int claimedMax = 99; // TODO-3: sửa thành q3.size()
        System.out.println("  left in queue=" + q3.size() + " claimedMax=" + claimedMax);
        check(claimedMax <= CAPACITY, "T3: size quan sát được <= capacity (backpressure)",
                "gợi ý: claimedMax = q3.size() (<= 4 mới đúng)");
        check(q3.isEmpty() && consumed3.size() == 8, "T3: drain hết, không kẹt message", "");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao thiếu poison pill thì consumer treo với take() nhưng FAIL nhanh với poll(2s)?");
        System.out.println(" 2. 2 consumer cùng take() thì thứ tự còn đảm bảo không? Vì sao?");
        System.out.println(" 3. Queue đầy thì producer bị gì (block ở put)? Đó chính là backpressure nghĩa là gì?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: Thread p1 = producer(q1, 5, true);
     *  T2: thêm dòng  consumed2.add(item);  trong vòng lặp consumer
     *  T3: int claimedMax = q3.size();
     */
}
