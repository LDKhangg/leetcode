/**
 * L1 — Generic class Box<T> + generic method + bounded type.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Generic class: Box<T> là "cái hộp đựng kiểu T", T do người dùng chọn lúc new.
 *   Box<String> chỉ đựng String, Box<Integer> chỉ đựng Integer — sai kiểu báo NGAY khi biên dịch.
 * - Diamond <>: new Box<>(...) để compiler tự suy T từ vế trái, khỏi viết lại kiểu 2 lần.
 * - Generic method: <T> T firstOf(T[] a) — chữ <T> trước kiểu trả về nghĩa là
 *   "T là tham số kiểu của riêng method này", suy ra từ argument truyền vào.
 * - Bounded: <T extends Number> nghĩa là T chỉ được là Number hoặc con của nó
 *   (Integer, Double...), nhờ đó mới gọi được n.doubleValue().
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac playground/generics/L1_Box.java && java -cp playground/generics L1_Box
 */
public class L1_Box {

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

    // Hộp generic đựng đúng 1 giá trị kiểu T.
    static class Box<T> {
        private T value;
        void set(T v) { this.value = v; }
        T get() { return value; }
    }

    // Trả về phần tử đầu tiên của mảng (thân đang SAI cố ý).
    static <T> T firstOf(T[] arr) {
        return null; // TODO-2: sửa thành return arr[0];
    }

    // Trả về phần tử lớn hơn (thân đang SAI cố ý: luôn trả về a).
    static <T extends Comparable<T>> T max(T a, T b) {
        return a; // TODO-3: so sánh a với b rồi mới trả về
    }

    // Bóc giá trị double từ bất kỳ Number nào (thân đang SAI cố ý).
    static <T extends Number> double toDouble(T n) {
        return 0; // TODO-4: sửa thành return n.doubleValue();
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        Box<Integer> sample = new Box<>(); // <> là diamond: compiler tự hiểu <Integer>
        sample.set(7);
        check(sample.get() == 7, "mẫu: Box<Integer> get/set + diamond <>", "");

        System.out.println("\n--- TODO-1: Box<String> cơ bản ---");
        // ĐỀ: hộp b phải đựng chuỗi "hello".
        // KẾT QUẢ MONG ĐỢI: b.get() equals "hello".
        // - Hint 1: chỉ cần sửa giá trị truyền vào set().
        // - Hint 2: Box<String>.set chỉ nhận String — thử set(123) xem lỗi biên dịch gì.
        // - LỖI THƯỜNG GẶP:
        //     Box<String> b = new Box<>(); b.set(123); -> không biên dịch (Integer không phải String)
        //     quên set() -> get() trả null, check FAIL vì null không equals "hello"
        Box<String> b = new Box<>();
        b.set("sai"); // TODO-1: sửa "sai" thành "hello"
        check("hello".equals(b.get()), "T1: Box<String> đựng \"hello\"", "set đúng giá trị đề yêu cầu");

        System.out.println("\n--- TODO-2: generic method firstOf ---");
        // ĐỀ: firstOf trả về phần tử ĐẦU TIÊN của mảng, với mọi kiểu T.
        // KẾT QUẢ MONG ĐỢI: firstOf({"a","b"}) equals "a".
        // - Hint 1: T được suy từ argument — truyền String[] thì T = String.
        // - Hint 2: chỉ cần return arr[0], không cần biết T là gì.
        // - LỖI THƯỜNG GẶP:
        //     quên <T> trước kiểu trả về -> T không được khai báo, lỗi biên dịch
        //     return arr[1] -> sai index, lén lút FAIL ở test khác
        String[] words = {"a", "b"};
        check("a".equals(firstOf(words)), "T2: firstOf([a,b])==a", "return arr[0] thay vì null");

        System.out.println("\n--- TODO-3: bounded Comparable + max ---");
        // ĐỀ: max(a, b) trả về phần tử LỚN HƠN (thân mẫu luôn trả a nên sai nửa trường hợp).
        // KẾT QUẢ MONG ĐỢI: max(3,7)==7 và max(9,2)==9.
        // - Hint 1: vì T extends Comparable<T> nên gọi được a.compareTo(b) (>= 0 nghĩa là a >= b).
        // - Hint 2: cú pháp: return a.compareTo(b) >= 0 ? a : b;
        // - LỖI THƯỜNG GẶP:
        //     dùng a > b -> không biên dịch, T không phải kiểu số nguyên thủy
        //     return b -> đảo ngược, FAIL test còn lại
        check(max(3, 7) == 7, "T3a: max(3,7)==7", "so sánh bằng compareTo rồi trả về phần tử lớn hơn");
        check(max(9, 2) == 9, "T3b: max(9,2)==9", "");

        System.out.println("\n--- TODO-4: bounded Number ---");
        // ĐỀ: toDouble bóc giá trị double từ bất kỳ Number nào (Integer, Double, Long...).
        // KẾT QUẢ MONG ĐỢI: toDouble(5)==5.0 và toDouble(2.5)==2.5.
        // - Hint 1: Number đã có sẵn method doubleValue(), chỉ cần gọi nó.
        // - Hint 2: chính nhờ <T extends Number> nên compiler mới cho gọi n.doubleValue().
        // - LỖI THƯỜNG GẶP:
        //     (double) n -> không biên dịch (không cast trực tiếp từ T được)
        //     Double.parseDouble(n.toString()) -> chạy được nhưng vòng vo, mất nghĩa của bounded
        check(toDouble(5) == 5.0, "T4a: toDouble(5)==5.0", "return n.doubleValue()");
        check(toDouble(2.5) == 2.5, "T4b: toDouble(2.5)==2.5", "");

        System.out.println("\n--- TODO-5: diamond + kiểm tra kiểu lúc runtime ---");
        // ĐỀ: hộp Box<Number> phải đựng số thực 3.14 (kiểu Double).
        // KẾT QUẢ MONG ĐỢI: bn.get() instanceof Double == true.
        // - Hint 1: chỉ cần đổi giá trị set vào từ 42 (Integer) thành 3.14 (Double).
        // - Hint 2: <> (diamond) không khóa kiểu con — Box<Number> đựng được cả Integer
        //   lẫn Double, nên instanceof mới phân biệt được lúc runtime.
        // - LỖI THƯỜNG GẶP:
        //     Box<Double> bn = new Box<>(); bn.set(42); -> không biên dịch (42 là Integer)
        //     check(bn.get() instanceof Number) -> luôn true, không chứng minh được gì
        Box<Number> bn = new Box<>();
        bn.set(42); // TODO-5: sửa 42 thành 3.14
        check(bn.get() instanceof Double, "T5: bn.get() là Double", "set(3.14) thay vì set(42)");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao Box<String>.set(123) bị chặn lúc biên dịch, còn Box (raw type) thì không?");
        System.out.println(" 2. Generic method <T> T firstOf khác gì method thường T firstOf? (thử bỏ <T> xem lỗi gì)");
        System.out.println(" 3. <T extends Number> cho phép gọi method nào mà <T> thường không cho? Vì sao?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: b.set("hello");
     *  T2: return arr[0];
     *  T3: return a.compareTo(b) >= 0 ? a : b;
     *  T4: return n.doubleValue();
     *  T5: bn.set(3.14);
     */
}
