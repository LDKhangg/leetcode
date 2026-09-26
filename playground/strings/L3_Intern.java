/**
 * L3 — intern() + switch trên String + text block + String.join.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - intern(): new String("hi").intern() trả về object CANONICAL trong pool,
 *   nên new String("hi").intern() == "hi" là TRUE.
 * - switch trên String (Java 7+): switch (level) { case "GOLD" -> ... } — so bằng
 *   equals bên trong, KHÔNG phải ==, nên an toàn với object mới.
 * - Text block """ (Java 15+): compiler tự stripIndent (xóa indent chung) — nội dung
 *   """\n    chào\n    bạn""" thực chất chỉ là "chào\nbạn".
 * - formatted(): "Hello, %s!".formatted(name) thay %s bằng name, gọn hơn String.format.
 * - String.join("/", parts) nối mảng KHÔNG thừa delimiter cuối — cách thủ công
 *   r += p + "/" luôn dư 1 cái "/" ở cuối.
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac playground/strings/L3_Intern.java && java -cp playground/strings L3_Intern
 */
public class L3_Intern {

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

    // Nhận xét theo hạng (thân đang SAI cố ý: level nào cũng "không rõ").
    static String remark(String level) {
        return "không rõ"; // TODO-2: viết switch theo level (GOLD/SILVER/BRONZE)
    }

    // Nối mảng bằng "/" (thân đang SAI cố ý: thừa "/" cuối + dùng += trong loop).
    static String slashJoin(String[] parts) {
        String r = "";
        for (String p : parts) r += p + "/";
        return r; // TODO-4: thừa "/" cuối — dùng String.join("/", parts)
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        String h = new String("hi").intern(); // intern() trả về chuỗi canonical trong pool
        check(h == "hi", "mẫu: intern() về pool nên ==", "");

        System.out.println("\n--- TODO-1: intern để so == ---");
        // ĐỀ: object new String("java") phải == được với literal "java".
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: chỉ cần thêm .intern() vào a: a.intern() == "java".
        // - Hint 2: intern() không sửa object cũ mà TRẢ VỀ object trong pool.
        // - LỖI THƯỜNG GẶP:
        //     a.intern(); check(a == "java") -> sai: quên dùng giá trị trả về
        //     dùng equals thay intern -> đúng kết quả nhưng không luyện được intern
        String a = new String("java");
        check(a == "java", "T1: new String(\"java\") == literal", "object mới khác pool — thêm .intern() vào a");

        System.out.println("\n--- TODO-2: switch trên String ---");
        // ĐỀ: GOLD -> "xuất sắc", SILVER -> "khá", BRONZE -> "trung bình", còn lại -> "không rõ".
        // KẾT QUẢ MONG ĐỢI: cả 2 check PASS.
        // - Hint 1: return switch (level) { case "GOLD" -> "xuất sắc"; ... default -> "không rõ"; };
        // - Hint 2: switch String so bằng equals nên new String("GOLD") vẫn match case "GOLD".
        // - LỖI THƯỜNG GẶP:
        //     switch (level) mà quên default -> level lạ trả null/rơi case, NPE ở caller
        //     dùng if (level == "GOLD") -> sai địa chỉ với object mới, phải equals/switch
        check("xuất sắc".equals(remark("GOLD")), "T2a: GOLD -> xuất sắc", "viết switch theo level");
        check("không rõ".equals(remark("KIM CƯƠNG")), "T2b: level lạ -> không rõ", "");

        System.out.println("\n--- TODO-3: formatted + text block ---");
        // ĐỀ (3a): thay %s bằng tên "An" -> "Hello, An!".
        // KẾT QUẢ MONG ĐỢI: T3a PASS.
        // - Hint 1: chỉ cần thêm .formatted(name) vào cuối literal.
        // - Hint 2: "Hello, %s!".formatted(name) gọn hơn String.format("Hello, %s!", name).
        // - LỖI THƯỜNG GẶP:
        //     quên .formatted -> %s còn nguyên, FAIL
        //     "Hello, " + name -> đúng nhưng bài này luyện formatted
        String name = "An";
        String g = "Hello, %s!"; // TODO-3a: thêm .formatted(name)
        check("Hello, An!".equals(g), "T3a: formatted thay %s bằng tên", "thêm .formatted(name) vào cuối literal");
        // ĐỀ (3b): text block bên dưới thực chất bằng chuỗi nào?
        // KẾT QUẢ MONG ĐỢI: T3b PASS.
        // - Hint 1: compiler tự stripIndent — 4 spaces đầu dòng bị xóa hết.
        // - Hint 2: đáp án là "chào\nbạn" (không indent), không phải bản còn spaces.
        // - LỖI THƯỜNG GẶP:
        //     nghĩ text block giữ nguyên indent -> so với bản còn spaces là FAIL
        //     quên \n giữa 2 dòng khi viết đáp án bằng chuỗi thường
        String block = """
                chào
                bạn""";
        check(block.equals("    chào\n    bạn"), "T3b: text block giữ nguyên indent", "text block tự stripIndent — đáp án là \"chào\\nbạn\""); // TODO-3b

        System.out.println("\n--- TODO-4: String.join vs nối tay ---");
        // ĐỀ: nối ["a","b","c"] thành "a/b/c" (không thừa delimiter).
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: return String.join("/", parts); — 1 dòng, không thừa "/" cuối.
        // - Hint 2: cách nối tay r += p + "/" luôn dư 1 cái "/" ở cuối + O(n^2) (xem lại L2).
        // - LỖI THƯỜNG GẶP:
        //     nối tay rồi quên cắt ký tự cuối -> "a/b/c/", FAIL
        //     String.join("/", "a,b,c") -> truyền 1 chuỗi thay vì mảng, không tách gì cả
        check("a/b/c".equals(slashJoin(new String[]{"a", "b", "c"})), "T4: nối thành a/b/c", "dùng String.join(\"/\", parts)");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. intern() giúp == nhưng có giá gì? (pool nằm lâu trong memory — lạm dụng với chuỗi random gây rò rỉ)");
        System.out.println(" 2. Vì sao switch(String) an toàn còn if (s == \"GOLD\") thì không? (switch dùng equals bên trong)");
        System.out.println(" 3. Text block stripIndent theo quy tắc nào? (xóa prefix trắng chung dài nhất của mọi dòng)");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: check(a.intern() == "java", ...);
     *  T2: return switch (level) { case "GOLD" -> "xuất sắc"; case "SILVER" -> "khá"; case "BRONZE" -> "trung bình"; default -> "không rõ"; };
     *  T3a: String g = "Hello, %s!".formatted(name);
     *  T3b: check(block.equals("chào\nbạn"), ...);
     *  T4: return String.join("/", parts);
     */
}
