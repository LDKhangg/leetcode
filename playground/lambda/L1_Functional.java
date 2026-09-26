import java.util.Arrays;
import java.util.Comparator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * L1 — Lambda + functional interface + method reference.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Functional interface = interface chỉ có ĐÚNG 1 method trừu tượng (SAM).
 *   Ví dụ: Predicate<T> có test(), Function<T,R> có apply(), Supplier<T> có get().
 * - Lambda = cách viết ngắn cho "new Interface { method... }".
 *   (String s) -> s.length()  tương đương  new Function<String,Integer> { apply(s){...} }
 * - Method reference (String::length) chỉ là viết tắt của lambda (s -> s.length()).
 * - Lambda chỉ capture được biến "effectively final" (không gán lại sau đó).
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L1_Functional.java && java -cp . L1_Functional
 */
public class L1_Functional {

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

    // Ví dụ mẫu: interface tự tạo, chạy luôn không cần sửa.
    @FunctionalInterface
    interface Checker {
        boolean test(String s);
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        Checker longName = (String s) -> { return s.length() > 3; };
        // Viết gọn dần: (s) -> s.length() > 3  |  rồi tới method-ref ở bài sau
        check(!longName.test("Bob"), "mẫu: Bob ngắn hơn 4 ký tự", "");
        check(longName.test("Alice"), "mẫu: Alice dài hơn 3 ký tự", "");

        System.out.println("\n--- TODO-1: Predicate + method reference ---");
        // ĐỀ: tạo Predicate<String> tên isBlank, trả true nếu chuỗi rỗng/trắng.
        // KẾT QUẢ MONG ĐỢI: isBlank.test(" ") == true, test("hi") == false.
        // - Hint 1: String đã có sẵn method isBlank(), chỉ cần trỏ tới nó.
        // - Hint 2: cú pháp là  String::isBlank   (không có ngoặc đơn!)
        // - LỖI THƯỜNG GẶP:
        //     String::isBlank()   -> sai, thừa () vì đây là "trỏ tới hàm", không phải "gọi hàm"
        //     s -> s.isBlank()    -> đúng nhưng dài, bài này muốn luyện method-ref
        Predicate<String> isBlank = s -> false; // TODO-1: sửa dòng này
        check(isBlank.test(" "), "T1: \" \" là blank", "gợi ý: String::isBlank");
        check(!isBlank.test("hi"), "T1: \"hi\" không blank", "");

        System.out.println("\n--- TODO-2: Function (biến đổi kiểu) ---");
        // ĐỀ: tạo Function<String,Integer> tên len, biến "hello" -> 5.
        // - Hint 1: String có method length(), trỏ tới nó bằng String::length.
        // - LỖI THƯỜNG GẶP:
        //     Function<String, String>  -> sai kiểu trả về, phải là <String, Integer>
        //     s -> s.length     -> sai, thiếu () vì length là method, không phải field
        Function<String, Integer> len = s -> -1; // TODO-2: sửa dòng này
        check(len.apply("hello") == 5, "T2: len(hello)==5", "gợi ý: String::length");

        System.out.println("\n--- TODO-3: Supplier (tính lười/lazy) ---");
        // ĐỀ: bọc hàm expensive() trong Supplier để nó CHƯA chạy ngay,
        // chỉ chạy khi gọi .get(). Đoạn check bên dưới kiểm tra điều này.
        // - Hint: () -> expensive()  (không tham số, trả về String)
        // - LỖI THƯỜNG GẶP:
        //     Supplier<String> lazy = expensive(); -> sai: gọi luôn, không bọc lambda
        //     () -> expensive      -> sai: thiếu () của lời gọi hàm
        Counter.calls = 0;
        Supplier<String> lazy = () -> "chưa bọc"; // TODO-3: sửa thành () -> expensive()
        check(Counter.calls == 0, "T3: trước get() chưa chạy expensive()", "phải bọc trong lambda, không gọi trực tiếp");
        String v = lazy.get();
        check(Counter.calls == 1 && "done".equals(v), "T3: sau get() chạy đúng 1 lần, trả về done", "");

        System.out.println("\n--- TODO-4: sắp xếp bằng Comparator ---");
        // ĐỀ: sắp xếp mảng tên theo độ dài tăng dần. Kết quả: [Jo, Bob, Alice, Christopher]
        // - Hint 1: Comparator.comparingInt(...) nhận 1 Function biến tên -> số.
        // - Hint 2: function đó chính là String::length ở TODO-2.
        // - LỖI THƯỜNG GẶP:
        //     sort(names, (a,b) -> a.length() - b.length()) -> chạy được nhưng dài, dễ tràn số
        //     comparingInt(s -> s.length()) -> đúng, nhưng bài này muốn luyện method-ref
        String[] names = {"Bob", "Alice", "Jo", "Christopher"};
        Arrays.sort(names, (a, b) -> 0); // TODO-4: thay comparator đúng vào đây
        check(Arrays.toString(names).equals("[Jo, Bob, Alice, Christopher]"),
                "T4: sort theo độ dài", "gợi ý: Comparator.comparingInt(String::length)");

        System.out.println("\n--- TODO-5: effectively final (bẫy phỏng vấn) ---");
        // ĐỀ: đọc đoạn code lỗi bên dưới (đang comment), đoán lỗi biên dịch là gì,
        // rồi bỏ comment 2 dòng để kiểm chứng. Sau đó comment lại để file chạy tiếp.
        // - KIẾN THỨC: lambda chỉ được "nhìn" biến ngoài nếu biến đó KHÔNG bị gán lại.
        // - LỖI SẼ THẤY: "variable used in lambda expression should be final or effectively final"
        // int n = 1;
        // Runnable r = () -> System.out.println(n);
        // n = 2; // <-- dòng này làm n không còn effectively-final -> lỗi ở dòng lambda
        System.out.println("(đọc code TODO-5 trong file, bỏ comment để thấy lỗi rồi comment lại)");
        check(true, "T5: đã đọc và thử uncomment (tự giác nhé)", "");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao @FunctionalInterface chỉ cho 1 method? (thử thêm method thứ 2 xem lỗi gì)");
        System.out.println(" 2. s -> s.length() và String::length khác gì nhau? (gợi ý: không khác, chỉ là viết tắt)");
        System.out.println(" 3. Lambda khác anonymous class ở `this` thế nào? (trong lambda, this = object ngoài)");
    }

    // Đếm số lần expensive() thực sự chạy (phục vụ TODO-3)
    static class Counter {
        static int calls = 0;
    }

    static String expensive() {
        Counter.calls++;
        System.out.println("  >> expensive() đang chạy... (lần " + Counter.calls + ")");
        return "done";
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: Predicate<String> isBlank = String::isBlank;
     *  T2: Function<String, Integer> len = String::length;
     *  T3: Supplier<String> lazy = () -> expensive();
     *  T4: Arrays.sort(names, Comparator.comparingInt(String::length));
     */
}
