import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * L3 — ExecutorService + CompletableFuture fan-out/fan-in.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - ExecutorService = hồ thread (pool) tái sử dụng: submit() task, pool chia thread chạy.
 *   Dùng xong PHẢI shutdown() (hoặc try-with-resources), nếu không JVM không thoát.
 * - Future = "phiếu hẹn": f.get() BLOCK tới khi task xong rồi trả kết quả.
 * - CompletableFuture = Future nâng cấp: supplyAsync(...) chạy nền,
 *   .thenApply(...) nối bước tiếp theo KHÔNG block, .exceptionally(...) đỡ lỗi
 *   (task lỗi -> đổi thành giá trị fallback thay vì nổ exception).
 * - allOf(f1, f2, ...) = "đợi TẤT CẢ xong" (fan-in). Nhớ gọi .join() thì nó mới
 *   thực sự đợi; quên join -> đọc kết quả khi task chưa xong.
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS. Mỗi task chỉ sleep ~50ms,
 * tổng runtime < 10s.
 * Run: javac L3_ExecutorFuture.java && java -cp . L3_ExecutorFuture
 */
public class L3_ExecutorFuture {

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

    static String fetchPrice(String id) {
        try {
            Thread.sleep(50); // giữ nhỏ để tổng runtime < 10s (KHÔNG tăng quá 200ms)
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        if (id.equals("P5")) {
            throw new RuntimeException("P5 down!");
        }
        return id + "=100";
    }

    public static void main(String[] args) throws Exception {
        List<String> ids = IntStream.rangeClosed(1, 5).mapToObj(i -> "P" + i).collect(Collectors.toList());

        System.out.println("--- Bài 0 (mẫu, đọc hiểu: Future thuần đúng) ---");
        List<String> single = List.of("P1");
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            Future<String> f = pool.submit(() -> fetchPrice("P1"));
            String got = f.get(); // block tới khi xong
            System.out.println("  future got: " + got);
            check("P1=100".equals(got), "mẫu: Future thuần trả đúng", "");
        }
        System.out.println("  (pool trong try-with-resources tự shutdown.)");

        System.out.println("\n--- TODO-1: exceptionally đỡ task lỗi ---");
        // ĐỀ: pipeline dưới thiếu .exceptionally(...) nên P5 ném lỗi -> join() nổ,
        // failed[0] bị đặt true. Thêm fallback để P5 thành "P5=FALLBACK (checked)".
        // - Hint 1: chèn giữa supplyAsync(...) và thenApply(...):
        //     .exceptionally(ex -> id + "=FALLBACK")
        // - Hint 2: sau exceptionally, thenApply vẫn chạy tiếp trên giá trị fallback.
        // - LỖI THƯỜNG GẶP:
        //     try/catch quanh join() rồi bỏ qua -> che lỗi, mất 1 kết quả
        //     exceptionally trả null -> thenApply sau đó NullPointerException
        final boolean[] pipelineFailed = {false};
        List<CompletableFuture<String>> cf1 = ids.stream()
                .map(id -> CompletableFuture
                        .supplyAsync(() -> fetchPrice(id))
                        // TODO-1: thêm .exceptionally(ex -> id + "=FALLBACK") ở đây (đang thiếu cố ý)
                        .thenApply(s -> s + " (checked)"))
                .collect(Collectors.toList());
        List<String> got1 = new java.util.ArrayList<>();
        try {
            CompletableFuture.allOf(cf1.toArray(new CompletableFuture[0])).join();
            cf1.forEach(f -> got1.add(f.join()));
        } catch (Exception e) {
            pipelineFailed[0] = true; // P5 nổ -> rơi vào đây
            System.out.println("  pipeline nổ: " + e.getCause());
        }
        System.out.println("  got1=" + got1);
        check(!pipelineFailed[0], "T1: pipeline không nổ khi P5 lỗi",
                "gợi ý: thêm .exceptionally(ex -> id + \"=FALLBACK\")");
        check(got1.contains("P5=FALLBACK (checked)"), "T1: P5 có fallback",
                "gợi ý: exceptionally phải đặt trước thenApply");

        System.out.println("\n--- TODO-2: allOf fan-in phải join() ---");
        // ĐỀ: all dưới được tạo nhưng KHÔNG join() (didJoin=false) nên check allDone
        // FAIL. Gọi all.join() rồi đặt didJoin=true.
        // - Hint 1: thêm dòng  all.join();  ngay sau khi tạo all.
        // - Hint 2: allOf(...).join() = "đợi TẤT CẢ task xong" rồi mới đọc kết quả.
        // - LỖI THƯỜNG GẶP:
        //     đọc f.join() từng cái mà không allOf -> vẫn đúng nhưng mất ý fan-in, lỗi 1 cái khó gom
        //     gọi get() mà quên xử lý checked exception -> code rối
        List<CompletableFuture<String>> cf2 = ids.stream()
                .map(id -> CompletableFuture
                        .supplyAsync(() -> fetchPrice(id))
                        .exceptionally(ex -> id + "=FALLBACK")
                        .thenApply(s -> s + " (checked)"))
                .collect(Collectors.toList());
        CompletableFuture<Void> all = CompletableFuture.allOf(cf2.toArray(new CompletableFuture[0]));
        boolean didJoin = false; // TODO-2: gọi all.join() rồi đặt didJoin = true (đang thiếu cố ý)
        boolean allDone = all.isDone();
        List<String> got2 = cf2.stream().map(f -> f.getNow(null)).collect(Collectors.toList());
        System.out.println("  allDone=" + allDone + " got2=" + got2);
        check(didJoin && allDone, "T2: allOf đã join, tất cả xong",
                "gợi ý: thêm all.join() (fan-in phải đợi)");
        check(got2.stream().noneMatch(s -> s == null), "T2: không đọc kết quả khi task chưa xong",
                "gợi ý: join trước rồi mới getNow/join từng cái");

        System.out.println("\n--- TODO-3: shutdown pool ---");
        // ĐỀ: pool2 dưới quên shutdown nên isShutdown()==false -> FAIL.
        // Gọi pool2.shutdown() (hoặc dùng try-with-resources như Bài 0).
        // - Hint 1: thêm dòng  pool2.shutdown();  sau khi dùng xong.
        // - Hint 2: try-with-resources (try (var pool = ...)) tự gọi shutdown, khỏi quên.
        // - LỖI THƯỜNG GẶP:
        //     quên shutdown -> pool giữ thread non-daemon -> `java` treo không thoát
        //     shutdownNow() giữa chừng -> task đang chạy bị interrupt, mất kết quả
        ExecutorService pool2 = Executors.newFixedThreadPool(2);
        Future<String> f2 = pool2.submit(() -> fetchPrice("P1"));
        String got3 = f2.get();
        // TODO-3: thêm pool2.shutdown(); ở đây (đang thiếu cố ý)
        boolean shut = pool2.isShutdown();
        System.out.println("  got3=" + got3 + " isShutdown=" + shut);
        check("P1=100".equals(got3), "T3: task trên pool2 chạy đúng", "");
        check(shut, "T3: pool đã shutdown",
                "gợi ý: gọi pool2.shutdown() (hoặc try-with-resources)");
        pool2.shutdown(); // dọn dẹp để JVM thoát được dù bản lỗi quên shutdown

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Future.get() block thread nào? thenApply chạy trên thread nào (mặc định)?");
        System.out.println(" 2. exceptionally khác try/catch quanh join ở điểm gì? (1 chỗ đỡ cho cả pipeline)");
        System.out.println(" 3. Pool 2 vs 8 thread cho 5 task x 50ms: thời gian khác nhau bao nhiêu, vì sao?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: .supplyAsync(() -> fetchPrice(id)).exceptionally(ex -> id + "=FALLBACK").thenApply(...)
     *  T2: all.join(); didJoin = true;
     *  T3: pool2.shutdown();  (hoặc try (ExecutorService pool2 = ...) { ... })
     */
}
