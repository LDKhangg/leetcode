import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * L3 — ExecutorService + CompletableFuture fan-out/fan-in.
 *
 * Mục tiêu phỏng vấn: thread pool, Future vs CompletableFuture,
 * non-blocking compose (thenApply/thenCombine/allOf), xử lý exception.
 *
 * Cách luyện:
 *  1. Chạy demo, đọc hiểu 2 cách: Future thuần vs CompletableFuture.
 *  2. TODO-1: thêm .exceptionally(...) cho 1 task lỗi (đổi id=5 thành lỗi).
 *  3. TODO-2: đổi pool size (2 vs 8), đo thời gian, giải thích.
 *  4. Nâng cao: thử Executors.newVirtualThreadPerTaskExecutor() (Java 21).
 *
 * Run: javac ExecutorFuture.java && java -cp . ExecutorFuture
 */
public class ExecutorFuture {

    static String fetchPrice(String id, long delayMs) {
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // TODO-1: uncomment để giả lập lỗi, rồi xử lý bằng exceptionally:
        // if (id.equals("P5")) throw new RuntimeException("P5 down!");
        return id + "=100";
    }

    static void demoFuture(ExecutorService pool, List<String> ids) throws Exception {
        long t0 = System.currentTimeMillis();
        List<Future<String>> futures = ids.stream()
                .map(id -> pool.submit(() -> fetchPrice(id, 200)))
                .collect(Collectors.toList());
        for (Future<String> f : futures) {
            System.out.println("  future got: " + f.get());
        }
        System.out.println("  Future thuần mất " + (System.currentTimeMillis() - t0) + "ms");
    }

    static void demoCompletable(List<String> ids) {
        long t0 = System.currentTimeMillis();
        List<CompletableFuture<String>> futures = ids.stream()
                .map(id -> CompletableFuture
                        .supplyAsync(() -> fetchPrice(id, 200))
                        // TODO-1: thêm .exceptionally(ex -> id + "=FALLBACK") ở đây
                        .thenApply(s -> s + " (checked)"))
                .collect(Collectors.toList());
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        futures.forEach(f -> System.out.println("  cf got: " + f.join()));
        System.out.println("  CompletableFuture mất " + (System.currentTimeMillis() - t0) + "ms");
    }

    public static void main(String[] args) throws Exception {
        List<String> ids = IntStream.rangeClosed(1, 5).mapToObj(i -> "P" + i).collect(Collectors.toList());

        // TODO-2: đổi 4 thành 2 rồi 8, so thời gian demoFuture.
        try (ExecutorService pool = Executors.newFixedThreadPool(4)) {
            System.out.println("[Future thuần]");
            demoFuture(pool, ids);
        }

        System.out.println("[CompletableFuture]");
        demoCompletable(ids);

        System.out.println("\nNâng cao (Java 21): thử thay pool bằng");
        System.out.println("  Executors.newVirtualThreadPerTaskExecutor()");
        System.out.println("Câu hỏi: Future.get() block thread nào? thenApply chạy trên thread nào?");
    }
}
