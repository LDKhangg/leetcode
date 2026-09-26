/**
 * L2 — String += trong loop vs StringBuilder + đo thời gian.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - String BẤT BIẾN: mỗi lần s += "x" là tạo object mới + chép lại toàn bộ chuỗi cũ.
 *   Nối N lần trong loop -> chép 1+2+...+N ký tự = O(n^2).
 * - StringBuilder KHẢ BIẾN: append chỉ ghi tiếp vào buffer, tổng O(n).
 *   Với N = 20000, chênh lệch là hàng trăm lần — đo nanoTime là thấy ngay.
 * - Quy tắc: nối chuỗi trong loop (đặc biệt loop lớn) thì DÙNG StringBuilder;
 *   reverse/mask/join đều có cách gọn với builder hoặc API có sẵn.
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac playground/strings/L2_Builder.java && java -cp playground/strings L2_Builder
 */
public class L2_Builder {

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

    // Đảo ngược chuỗi (thân đang SAI cố ý: trả nguyên input).
    static String reverse(String s) {
        return s; // TODO-1: dùng new StringBuilder(s).reverse().toString()
    }

    // Che số điện thoại, chỉ chừa 4 số cuối (thân đang SAI cố ý: không che).
    static String mask(String phone) {
        return phone; // TODO-2: che hết trừ 4 ký tự cuối bằng '*'
    }

    // Lặp lại chuỗi s đúng n lần (thân đang SAI cố ý: trả 1 lần).
    static String repeat(String s, int n) {
        return s; // TODO-3: lặp n lần append vào StringBuilder (hoặc s.repeat(n))
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        StringBuilder sb0 = new StringBuilder();
        sb0.append("a").append("b"); // append nối chuỗi, không tạo object trung gian
        check("ab".equals(sb0.toString()), "mẫu: builder append + toString", "");

        System.out.println("\n--- TODO-1: đảo chuỗi ---");
        // ĐỀ: reverse("abc") phải ra "cba".
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: StringBuilder có sẵn method reverse().
        // - Hint 2: new StringBuilder(s).reverse().toString().
        // - LỖI THƯỜNG GẶP:
        //     tự viết vòng for swap char[] -> dài, dễ sai index giữa
        //     quên .toString() -> trả về StringBuilder, lỗi biên dịch khi gán cho String
        check("cba".equals(reverse("abc")), "T1: reverse(abc)==cba", "dùng new StringBuilder(s).reverse().toString()");

        System.out.println("\n--- TODO-2: che số điện thoại ---");
        // ĐỀ: mask("0123456789") phải ra "******6789" (che hết, chừa 4 số cuối).
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: append '*' (độ dài - 4) lần rồi append 4 ký tự cuối (substring).
        // - Hint 2: phone.substring(phone.length() - 4) lấy 4 số cuối.
        // - LỖI THƯỜNG GẶP:
        //     che nhầm cả 4 số cuối -> sai yêu cầu, FAIL
        //     dùng += trong loop con -> đúng kết quả nhưng mắc đúng lỗi bài này đang dạy
        check("******6789".equals(mask("0123456789")), "T2: mask chừa 4 số cuối", "append '*' rồi append substring 4 ký tự cuối");

        System.out.println("\n--- TODO-3: lặp chuỗi n lần ---");
        // ĐỀ: repeat("ab", 3) phải ra "ababab".
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: cách hiện đại là s.repeat(n) (Java 11+).
        // - Hint 2: cách luyện builder: loop n lần sb.append(s).
        // - LỖI THƯỜNG GẶP:
        //     return s; -> mới lặp 1 lần, FAIL
        //     s += s trong loop -> nhân đôi mỗi vòng (1,2,4,8...), sai với n lẻ
        check("ababab".equals(repeat("ab", 3)), "T3: repeat(ab,3)==ababab", "s.repeat(n) hoặc loop append n lần");

        System.out.println("\n--- TODO-4: đo += vs StringBuilder (N=20000) ---");
        // ĐỀ: chứng minh += trong loop CHẬM HƠN StringBuilder với N = 20000.
        // KẾT QUẢ MONG ĐỢI: T4a, T4b PASS (độ dài đúng) và builder nhanh hơn rõ rệt.
        // - Hint 1: += là O(n^2), builder là O(n) — với 20k vòng chênh lệch hàng trăm lần,
        //   kết quả ổn định trên mọi máy, không phải do may rủi.
        // - Hint 2: placeholder khẳng định NGƯỢC (tPlus < tBuilder) nên FAIL — đảo lại là xong.
        // - LỖI THƯỜNG GẶP:
        //     đo bằng currentTimeMillis() -> độ phân giải thô, nhiễu; nanoTime chuẩn hơn
        //     N quá nhỏ (vd 100) -> chênh lệch chìm trong nhiễu đo
        int N = 20000;
        long t0 = System.nanoTime();
        String s = "";
        for (int i = 0; i < N; i++) s += "x";
        long tPlus = System.nanoTime() - t0;
        check(s.length() == N, "T4a: chuỗi += dài đúng " + N, "");
        long t1 = System.nanoTime();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < N; i++) sb.append('x');
        String r = sb.toString();
        long tBuilder = System.nanoTime() - t1;
        check(r.length() == N, "T4b: builder dài đúng " + N, "");
        System.out.printf("  += : %,d ns | builder: %,d ns%n", tPlus, tBuilder);
        check(tPlus < tBuilder, "T4c: += nhanh hơn StringBuilder", "+= trong loop là O(n^2) — thực tế builder nhanh hơn, đổi thành tBuilder < tPlus"); // TODO-4

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao s += \"x\" trong loop là O(n^2)? (mỗi vòng chép lại cả chuỗi cũ dài dần)");
        System.out.println(" 2. Ngoài loop, khi nào vẫn nên dùng + thay vì builder? (gợi ý: nối vài chuỗi lẻ, compiler tự tối ưu)");
        System.out.println(" 3. StringBuilder không thread-safe — bản thread-safe tên gì, và khi nào cần nó?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: return new StringBuilder(s).reverse().toString();
     *  T2: StringBuilder sb = new StringBuilder();
     *      for (int i = 0; i < phone.length() - 4; i++) sb.append('*');
     *      sb.append(phone.substring(phone.length() - 4));
     *      return sb.toString();
     *  T3: return s.repeat(n);
     *  T4: check(tBuilder < tPlus, ...);
     */
}
