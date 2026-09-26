/**
 * L1 — OOP: record, sealed, pattern matching (Java 21).
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - record = class bất biến, tự sinh equals/hashCode/toString + accessor.
 *   Ví dụ: record Circle(double r) {}  ->  new Circle(1.0).r() == 1.0
 *   (accessor là c.r(), KHÔNG phải c.r hay c.getR()).
 * - sealed interface + permits = liệt kê sẵn ai được implement.
 *   Switch trên sealed KHÔNG cần default nếu đã đủ nhánh (compiler kiểm tra).
 * - instanceof pattern: if (o instanceof Circle c) -> dùng luôn biến c, khỏi cast.
 * - switch pattern: case Circle c -> ...  (rẽ nhánh theo KIỂU, không phải giá trị).
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L1_OopRecord.java && java -cp . L1_OopRecord
 */
public class L1_OopRecord {

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

    // Sealed: chỉ 2 loại shape được phép.
    sealed interface Shape permits Circle, Rect {}

    record Circle(double r) implements Shape {}
    record Rect(double w, double h) implements Shape {}

    // Diện tích bằng switch pattern matching — mỗi nhánh là 1 TODO riêng.
    static double area(Shape s) {
        return switch (s) {
            case Circle c -> c.r() * c.r(); // TODO-1: thiếu PI, sửa thành Math.PI * c.r() * c.r()
            case Rect r -> r.w() + r.h(); // TODO-2: + là sai, sửa thành r.w() * r.h()
        };
    }

    static String describe(Object o) {
        return "unknown"; // TODO-3: viết instanceof pattern, xem ĐỀ trong main
    }

    static double perimeter(Rect r) {
        return r.w() * r.h(); // TODO-4: đây là công thức diện tích, sửa thành 2 * (w + h)
    }

    static Circle mustBePositive(double r) {
        return new Circle(r); // TODO-5: ném IllegalArgumentException khi r < 0, xem ĐỀ trong main
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // record tự sinh equals/hashCode/toString — đoạn này chạy luôn, không cần sửa.
        var c0 = new Circle(1.0);
        check(c0.equals(new Circle(1.0)), "mẫu: record equals theo giá trị", "");
        check(c0.hashCode() == new Circle(1.0).hashCode(), "mẫu: equal -> hashCode bằng nhau", "");
        check(c0.toString().contains("1.0"), "mẫu: toString in ra field", "");

        System.out.println("\n--- TODO-1: nhánh Circle của area() ---");
        // ĐỀ: sửa nhánh Circle trong area() thành PI*r*r.
        // KẾT QUẢ MONG ĐỢI: area(new Circle(1.0)) ≈ 3.14159 (sai số < 0.001).
        // - Hint 1: Math.PI * c.r() * c.r()
        // - Hint 2: accessor của record là c.r(), KHÔNG phải c.r hay c.getR().
        // - LỖI THƯỜNG GẶP:
        //     c.r * c.r        -> sai, r là method, phải có ()
        //     3.14 * r * r     -> sai, không có biến r trong scope, phải là c.r()
        check(Math.abs(area(new Circle(1.0)) - Math.PI) < 0.001,
                "T1: area(Circle r=1) ≈ PI", "gợi ý: Math.PI * c.r() * c.r()");

        System.out.println("\n--- TODO-2: nhánh Rect của area() ---");
        // ĐỀ: sửa nhánh Rect trong area() thành w*h.
        // KẾT QUẢ MONG ĐỢI: area(new Rect(2.0, 3.0)) == 6.0.
        // - Hint 1: r.w() * r.h()
        // - Hint 2: so sánh với nhánh Circle đã sửa ở TODO-1 (cùng 1 switch).
        // - LỖI THƯỜNG GẶP:
        //     r.w() + r.h()    -> sai, đó là nửa chu vi, không phải diện tích
        //     w * h            -> sai, phải qua accessor r.w(), r.h()
        check(area(new Rect(2.0, 3.0)) == 6.0,
                "T2: area(Rect 2x3)==6.0", "gợi ý: r.w() * r.h()");

        System.out.println("\n--- TODO-3: instanceof pattern ---");
        // ĐỀ: viết lại describe(Object o): nếu là Circle trả "circle r=<r>",
        // nếu là String trả "string len=<độ dài>", còn lại "unknown".
        // KẾT QUẢ MONG ĐỢI: xem 3 dòng check bên dưới.
        // - Hint 1: if (o instanceof Circle c) return "circle r=" + c.r();
        // - Hint 2: pattern variable (c, s) dùng trực tiếp, khỏi cast.
        // - LỖI THƯỜNG GẶP:
        //     (Circle) o       -> cũ, không cần khi đã có pattern variable
        //     o instanceof Circle -> sai, thiếu tên biến pattern (phải có "c")
        //     quên nhánh String -> check string vẫn FAIL
        check(describe(new Circle(1.0)).equals("circle r=1.0"),
                "T3: describe(circle)", "gợi ý: if (o instanceof Circle c) ...");
        check(describe("hi").equals("string len=2"),
                "T3: describe(string)", "gợi ý: if (o instanceof String s) ...");
        check(describe(42).equals("unknown"), "T3: describe(42)==unknown", "");

        System.out.println("\n--- TODO-4: chu vi hình chữ nhật ---");
        // ĐỀ: sửa perimeter(Rect) thành chu vi 2*(w+h).
        // KẾT QUẢ MONG ĐỢI: perimeter(new Rect(2.0, 3.0)) == 10.0.
        // - Hint 1: 2 * (r.w() + r.h())
        // - Hint 2: placeholder hiện tại là công thức DIỆN TÍCH (w*h) — vẫn ra số,
        //   nhưng sai ngữ nghĩa, check sẽ bắt được.
        // - LỖI THƯỜNG GẶP:
        //     2 * r.w() + r.h()  -> sai thứ tự nhân/cộng, thiếu ngoặc
        check(perimeter(new Rect(2.0, 3.0)) == 10.0,
                "T4: perimeter(Rect 2x3)==10.0", "gợi ý: 2 * (r.w() + r.h())");

        System.out.println("\n--- TODO-5: record + fail-fast ---");
        // ĐỀ: mustBePositive(-1) phải ném IllegalArgumentException (fail-fast),
        // giá trị hợp lệ vẫn tạo Circle bình thường.
        // KẾT QUẢ MONG ĐỢI: 2 dòng check bên dưới đều PASS.
        // - Hint 1: if (r < 0) throw new IllegalArgumentException("r=" + r);
        // - Hint 2: record KHÔNG tự validate — muốn ràng buộc phải tự kiểm tra.
        // - LỖI THƯỜNG GẶP:
        //     return null;      -> sai, caller sẽ NullPointerException ở chỗ khác
        //     Math.abs(r)       -> sai, âm thầm sửa dữ liệu thay vì báo lỗi
        boolean thrown = false;
        try {
            mustBePositive(-1);
        } catch (IllegalArgumentException e) {
            thrown = true;
        }
        check(thrown, "T5: mustBePositive(-1) ném IllegalArgumentException",
                "gợi ý: if (r < 0) throw new IllegalArgumentException(...)");
        check(mustBePositive(2.0).r() == 2.0, "T5: giá trị hợp lệ tạo bình thường", "");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Khi nào dùng record, khi nào dùng class? (gợi ý: dữ liệu bất biến vs object có hành vi/state đổi)");
        System.out.println(" 2. sealed giúp gì cho switch? (thử thêm 1 shape thứ 3 không khai báo trong permits xem lỗi gì)");
        System.out.println(" 3. instanceof pattern khác cast thường ở điểm nào? (gợi ý: an toàn + gọn, biến chỉ visible khi đã chắc chắn kiểu)");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: case Circle c -> Math.PI * c.r() * c.r();
     *  T2: case Rect r -> r.w() * r.h();
     *  T3: if (o instanceof Circle c) return "circle r=" + c.r();
     *      if (o instanceof String s) return "string len=" + s.length();
     *      return "unknown";
     *  T4: return 2 * (r.w() + r.h());
     *  T5: if (r < 0) throw new IllegalArgumentException("r must be >= 0, got " + r);
     *      return new Circle(r);
     */
}
