/**
 * L1 — == vs equals + string pool + null-safe.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - == so sánh ĐỊA CHỈ object; equals() so sánh NỘI DUNG chuỗi. So nội dung thì DÙNG equals.
 * - Literal pool: "java" == "java" là TRUE (cùng 1 object trong pool), nhưng
 *   new String("java") == "java" là FALSE (object mới ngoài pool).
 * - Null-safe: x.equals("hi") nổ NullPointerException khi x null; đảo lại
 *   "hi".equals(x) thì an toàn (trả false).
 * - equalsIgnoreCase: so không phân biệt hoa/thường; isBlank(): rỗng HOẶC chỉ chứa
 *   khoảng trắng (isEmpty() chỉ true với "").
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac playground/strings/L1_Equality.java && java -cp playground/strings L1_Equality
 */
public class L1_Equality {

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

    // So x với "hi", CHƯA null-safe (sửa ở TODO-3).
    static boolean safeEquals(String x) {
        return x.equals("hi"); // TODO-3: đảo thành "hi".equals(x) để null-safe
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        String a = "java";
        String b = "java"; // cùng literal -> cùng object trong pool
        check(a == b, "mẫu: literal cùng pool nên ==", "");

        System.out.println("\n--- TODO-1: new String vs literal ---");
        // ĐỀ: so sánh NỘI DUNG s1 (new String) với s2 (literal).
        // KẾT QUẢ MONG ĐỢI: hai chuỗi bằng nhau về nội dung -> check PASS.
        // - Hint 1: s1 == s2 là false (khác object) — phải dùng s1.equals(s2).
        // - Hint 2: đây là câu PV top-1 về String, thuộc lòng: "== địa chỉ, equals nội dung".
        // - LỖI THƯỜNG GẶP:
        //     giữ nguyên == -> luôn false với new String, FAIL
        //     s1.equals(s2) == true -> thừa, equals đã trả boolean
        String s1 = new String("abc");
        String s2 = "abc";
        check(s1 == s2, "T1: new String(\"abc\") equals literal", "phải dùng s1.equals(s2), == so sánh địa chỉ");

        System.out.println("\n--- TODO-2: bỏ qua hoa/thường ---");
        // ĐỀ: "Hello" và "hello" coi như bằng nhau (không phân biệt hoa thường).
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: dùng "Hello".equalsIgnoreCase("hello").
        // - Hint 2: equals thường phân biệt hoa/thường nên trả false.
        // - LỖI THƯỜNG GẶP:
        //     toLowerCase rồi equals -> chạy được nhưng dài, lại tạo chuỗi rác
        //     == -> vừa sai địa chỉ vừa sai hoa/thường, sai kép
        check("Hello".equals("hello"), "T2: \"Hello\" bằng \"hello\" (không phân biệt hoa thường)", "dùng equalsIgnoreCase");

        System.out.println("\n--- TODO-3: null-safe equals ---");
        // ĐỀ: safeEquals(null) không được ném, trả false; safeEquals("hi") trả true.
        // KẾT QUẢ MONG ĐỢI: cả 2 check PASS, không có NullPointerException.
        // - Hint 1: đảo thành "hi".equals(x) — literal không bao giờ null nên an toàn.
        // - Hint 2: khung try/catch trong main đã biến NPE thành FAIL gọn, cứ chạy sẽ thấy.
        // - LỖI THƯỜNG GẶP:
        //     x.equals("hi") -> NPE khi x null (đây là lỗi production kinh điển)
        //     if (x == null) return false; -> đúng nhưng dài hơn đảo chuỗi
        boolean ok;
        try {
            ok = safeEquals(null);
        } catch (NullPointerException e) {
            ok = true; // ném NPE nghĩa là chưa null-safe -> coi như sai
        }
        check(!ok, "T3a: safeEquals(null) không ném, trả false", "đảo thành \"hi\".equals(x)");
        check(safeEquals("hi"), "T3b: safeEquals(\"hi\")==true", "");

        System.out.println("\n--- TODO-4: substring tạo object mới ---");
        // ĐỀ: "ab".substring(1) có nội dung là "b".
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: substring() luôn tạo object mới ngoài pool -> == với literal là false.
        // - Hint 2: sửa thành sub.equals("b").
        // - LỖI THƯỜNG GẶP:
        //     nghĩ substring trả về literal trong pool -> dùng == là sai
        //     "b" == sub -> đảo vế không cứu được, == vẫn so địa chỉ
        String sub = "ab".substring(1);
        check(sub == "b", "T4: substring(1) của \"ab\" là \"b\"", "substring() tạo object mới, == luôn false — dùng equals");

        System.out.println("\n--- TODO-5: isEmpty vs isBlank ---");
        // ĐỀ: chuỗi 3 spaces "   " có coi là "trống" theo nghĩa người dùng không? Có.
        // KẾT QUẢ MONG ĐỢI: check PASS.
        // - Hint 1: "   ".isEmpty() là false (độ dài 3) — phải dùng isBlank().
        // - Hint 2: isBlank() true với "" và mọi chuỗi chỉ chứa whitespace.
        // - LỖI THƯỜNG GẶP:
        //     dùng isEmpty() để validate input form -> lọt toàn spaces
        //     trim().isEmpty() -> đúng nhưng cũ, isBlank() sinh ra để thay nó
        String blank = "   ";
        check(blank.isEmpty(), "T5: \"   \" là chuỗi rỗng", "isEmpty() chỉ true với \"\" — chuỗi trắng dùng isBlank()");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao \"a\" == \"a\" true nhưng new String(\"a\") == \"a\" false? (vẽ pool ra giấy)");
        System.out.println(" 2. \"hi\".equals(x) an toàn khi x null — vậy x.equals(\"hi\") bao giờ NPE? Vì sao đảo vế lại hết?");
        System.out.println(" 3. Khi nào dùng == với String là ĐÚNG? (gợi ý: so enum, hoặc so đã intern() cố ý)");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: check(s1.equals(s2), ...);
     *  T2: check("Hello".equalsIgnoreCase("hello"), ...);
     *  T3: return "hi".equals(x);
     *  T4: check(sub.equals("b"), ...);
     *  T5: check(blank.isBlank(), ...);
     */
}
