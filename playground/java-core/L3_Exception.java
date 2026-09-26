import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

/**
 * L3 — Exception + try-with-resources + JVM/GC notes.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Checked (IOException): compiler BẮT phải xử lý (try/catch hoặc throws).
 *   Unchecked (IllegalArgumentException, NullPointerException): không bắt buộc,
 *   thường dùng để báo lỗi lập trình / argument sai (fail-fast).
 * - try-with-resources: tự đóng resource (stream, file...) dù có exception.
 *   Ví dụ: try (var lines = Files.lines(p)) { ... }
 * - finally LUÔN chạy (kể cả khi try ném) -> nơi dọn dẹp (xóa file tạm...).
 * - Catch con TRƯỚC cha: catch (NoSuchFileException e) phải đứng trước
 *   catch (IOException e), vì NoSuchFileException là con của IOException.
 *   (Đảo thứ tự là LỖI BIÊN DỊCH — nên bài này luyện bằng check() runtime.)
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L3_Exception.java && java -cp . L3_Exception
 */
public class L3_Exception {

    // Đếm điểm
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

    // Hàm đọc dòng đầu file — ném checked IOException cho caller quyết định.
    static String firstLine(Path p) throws IOException {
        // try-with-resources: tự đóng stream dù có exception.
        try (var lines = Files.lines(p)) {
            return lines.findFirst().orElse("(empty)");
        }
    }

    // Xóa file tạm, KHÔNG ném exception ra ngoài — đang SAI cố ý.
    static boolean cleanupTemp(Path p) {
        return false; // TODO-1: dùng Files.deleteIfExists trong try/catch riêng
    }

    // Phân loại IOException — đang SAI cố ý (mọi lỗi đều báo chung chung).
    static String kindOf(IOException e) {
        return "io-chung"; // TODO-2: NoSuchFileException phải trả "khong-thay-file"
    }

    // Căn bậc 2 với chính sách fail-fast — đang SAI cố ý.
    static double safeSqrt(double x) {
        return Math.sqrt(x); // TODO-3: x < 0 phải ném IllegalArgumentException
    }

    // Đọc dòng đầu, có giá trị dự phòng — đang SAI cố ý.
    static String firstLineOrDefault(Path p, String fallback) {
        return fallback; // TODO-4: try firstLine, bắt NoSuchFileException riêng trước IOException chung
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Mẫu: bắt checked exception, giữ nguyên nhân (cause). Chạy luôn, không sửa.
        Path tmp = null;
        try {
            tmp = Files.createTempFile("demo", ".txt");
            Files.writeString(tmp, "hello\nworld\n");
            check(firstLine(tmp).equals("hello"), "mẫu: đọc dòng đầu file", "");
        } catch (IOException e) {
            check(false, "mẫu: đọc dòng đầu file", "không ngờ tới: " + e);
        }

        System.out.println("\n--- TODO-1: dọn file tạm trong finally ---");
        // ĐỀ: viết cleanupTemp(Path) dùng Files.deleteIfExists trong try/catch riêng,
        // rồi gọi nó trong khối finally bên dưới.
        // KẾT QUẢ MONG ĐỢI: sau finally, file tạm không còn tồn tại.
        // - Hint 1: try { return Files.deleteIfExists(p); } catch (IOException e) { return false; }
        // - Hint 2: dọn trong finally (không phải sau catch) vì finally chạy CẢ khi try ném.
        // - LỖI THƯỜNG GẶP:
        //     Files.delete(p)            -> sai, file vắng là ném ngay; deleteIfExists hiền hơn
        //     để catch (Exception e) {}  -> sai, nuốt hết lỗi mà không báo gì (ở đây return false là đủ)
        //     quên gọi trong finally      -> cleanupTemp đúng mà tmp vẫn còn vì không ai gọi
        try {
            if (tmp != null && Files.exists(tmp)) {
                System.out.println("  tmp đang tồn tại: " + tmp);
            }
        } catch (Exception e) {
            System.out.println("  (lỗi không ngờ: " + e + ")");
        } finally {
            if (tmp != null) {
                cleanupTemp(tmp); // phải gọi ở đây — finally luôn chạy kể cả khi try ném
            }
        }
        try {
            check(tmp != null && !Files.exists(tmp), "T1: file tạm đã được dọn trong finally",
                    "gợi ý: cleanupTemp dùng Files.deleteIfExists + gọi trong finally");
        } catch (Exception e) {
            check(false, "T1: file tạm đã được dọn trong finally", "lỗi không ngờ: " + e);
        }

        System.out.println("\n--- TODO-2: bắt riêng NoSuchFileException ---");
        // ĐỀ: sửa kindOf(IOException) để phân loại: NoSuchFileException -> "khong-thay-file",
        // IOException khác -> "io-chung".
        // KẾT QUẢ MONG ĐỢI: 2 dòng check bên dưới đều PASS.
        // (Vì đảo thứ tự catch là lỗi BIÊN DỊCH nên bài này luyện bằng instanceof ở runtime
        //  — cùng một nguyên tắc: kiểm tra kiểu CON trước kiểu CHA.)
        // - Hint 1: if (e instanceof NoSuchFileException) return "khong-thay-file";
        // - Hint 2: trong catch thật, thứ tự phải là: catch (NoSuchFileException) TRƯỚC catch (IOException).
        // - LỖI THƯỜNG GẶP:
        //     catch (IOException) trước -> compiler báo "exception has already been caught"
        //     e.getClass() == ...       -> dài, instanceof là đủ và còn đúng với class con cháu
        check(kindOf(new NoSuchFileException("/tmp/vang.txt")).equals("khong-thay-file"),
                "T2: NoSuchFileException được bắt riêng", "gợi ý: instanceof NoSuchFileException");
        check(kindOf(new IOException("đứt mạng")).equals("io-chung"),
                "T2: IOException khác vẫn báo chung", "");

        System.out.println("\n--- TODO-3: unchecked + fail-fast ---");
        // ĐỀ: safeSqrt(x) với x < 0 phải NÉM IllegalArgumentException (fail-fast),
        // x >= 0 trả Math.sqrt(x) bình thường.
        // KẾT QUẢ MONG ĐỢI: gọi safeSqrt(-1) là nổ exception; safeSqrt(9) == 3.0.
        // - Hint 1: if (x < 0) throw new IllegalArgumentException("x=" + x);
        // - Hint 2: fail-fast = báo lỗi NGAY chỗ sai, thay vì trả NaN để lỗi lan xa mới phát hiện.
        // - LỖI THƯỜNG GẶP:
        //     return Double.NaN;   -> sai chính sách bài này (tolerant), caller khó phát hiện
        //     return -1;           -> sai nặng: -1 cũng là số hợp lệ, caller tưởng kết quả thật
        boolean thrown = false;
        try {
            safeSqrt(-1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "T3: safeSqrt(-1) ném IllegalArgumentException",
                "gợi ý: if (x < 0) throw new IllegalArgumentException(...)");
        check(safeSqrt(9) == 3.0, "T3: safeSqrt(9)==3.0", "");

        System.out.println("\n--- TODO-4: fallback có phân loại ---");
        // ĐỀ: viết firstLineOrDefault(Path, fallback): file có nội dung -> trả nội dung;
        // file vắng (NoSuchFileException) -> trả "not-found"; lỗi IO khác -> trả fallback.
        // KẾT QUẢ MONG ĐỢI: 3 dòng check bên dưới đều PASS.
        // - Hint 1: try { return firstLine(p); }
        //           catch (NoSuchFileException e) { return "not-found"; }
        //           catch (IOException e) { return fallback; }
        // - Hint 2: catch con (NoSuchFileException) BẮT BUỘC đứng trước catch cha (IOException).
        // - LỖI THƯỜNG GẶP:
        //     đảo 2 catch           -> lỗi biên dịch: "already been caught"
        //     catch (Exception e)   -> sai, nuốt cả lỗi không liên quan (RuntimeException...)
        //     luôn return fallback  -> sai, che mất phân loại "not-found" (placeholder hiện tại)
        Path tmp4 = null;
        try {
            tmp4 = Files.createTempFile("t4", ".txt");
            Files.writeString(tmp4, "nd-t4\n");
            check(firstLineOrDefault(tmp4, "FB").equals("nd-t4"),
                    "T4: file tồn tại -> trả nội dung", "gợi ý: try { return firstLine(p); } ...");
            check(firstLineOrDefault(Path.of("/tmp/khong-ton-tai-xyz-123.txt"), "FB").equals("not-found"),
                    "T4: file vắng -> not-found", "gợi ý: catch (NoSuchFileException) riêng, đứng trước");
        } catch (IOException e) {
            check(false, "T4: setup file tạm", "lỗi không ngờ: " + e);
        } finally {
            if (tmp4 != null) {
                try {
                    Files.deleteIfExists(tmp4);
                } catch (IOException ignored) {
                }
            }
        }

        System.out.println("\n--- TODO-5: JVM/GC tự kiểm (thí nghiệm) ---");
        // ĐỀ: chạy file này 2 lần với heap khác nhau, quan sát maxMem đổi, rồi đặt daThuXmx = true:
        //   java -Xmx64m -cp . L3_Exception  |  java -Xmx512m -cp . L3_Exception
        // KẾT QUẢ MONG ĐỢI: maxMem in ra khác nhau rõ rệt giữa 2 lần chạy.
        // - Hint 1: -Xmx là heap TỐI ĐA JVM được dùng; maxMemory() phản ánh đúng con số đó.
        // - Hint 2: Young/Old gen + Minor/Major GC: object mới ở Young (GC nhanh/rẻ),
        //   sống lâu được đẩy sang Old (GC đắt) — -Xmx to thì Old rộng, đỡ Major GC.
        // - LỖI THƯỜNG GẶP:
        //     nhầm -Xmx với -Xms (Xms là heap KHỞI ĐẦU, Xmx là TRẦN)
        //     nghĩ heap càng to càng nhanh -> sai, GC mỗi lần quét lâu hơn
        System.out.println("  maxMem=" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "MB (chạy lại với -Xmx khác để so)");
        boolean daThuXmx = false; // TODO-5: đặt true sau khi đã chạy 2 lần -Xmx64m và -Xmx512m
        check(daThuXmx, "T5: đã thử -Xmx64m vs -Xmx512m (tự giác nhé)",
                "chạy 2 lệnh java -Xmx... rồi đặt true");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. checked vs unchecked: khi nào throws, khi nào throw unchecked? (gợi ý: lỗi phục hồi được vs lỗi lập trình)");
        System.out.println(" 2. Vì sao dọn dẹp đặt trong finally mà không phải sau catch? (thử ném exception trong try rồi xem dòng nào chạy)");
        System.out.println(" 3. Object sống ở heap, reference ở stack — vì sao? (-Xmx giới hạn cái nào?)");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: try { return Files.deleteIfExists(p); } catch (IOException e) { return false; }
     *  T2: if (e instanceof NoSuchFileException) return "khong-thay-file";
     *      return "io-chung";
     *  T3: if (x < 0) throw new IllegalArgumentException("x must be >= 0, got " + x);
     *      return Math.sqrt(x);
     *  T4: try { return firstLine(p); }
     *      catch (NoSuchFileException e) { return "not-found"; }
     *      catch (IOException e) { return fallback; }
     *  T5: java -Xmx64m -cp . L3_Exception  rồi  java -Xmx512m -cp . L3_Exception,
     *      quan sát maxMem, đặt daThuXmx = true;
     */
}
