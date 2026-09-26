import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * L2 — Stream: map / filter / reduce, lazy evaluation.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút):
 * - Stream có 2 loại thao tác:
 *     intermediate (lười, chưa chạy): filter, map, sorted, peek, limit
 *     terminal (kích hoạt, chạy thật): sum, toList, forEach, max, count
 * - Không có terminal thì intermediate KHÔNG chạy gì cả (lazy).
 * - Pipeline là 1 chiều, dùng 1 lần: gọi 2 terminal trên cùng stream -> lỗi.
 * ============================================================================
 * CÁCH LÀM: các TODO đang để SAI cố ý -> chạy thấy FAIL, sửa dần tới PASS.
 * Run: javac L2_Streams.java && java -cp . L2_Streams
 */
public class L2_Streams {

    record Order(String id, int amount, boolean paid) {}

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

    public static void main(String[] args) {
        List<Order> orders = List.of(
                new Order("A", 100, true),
                new Order("B", 200, false),
                new Order("C", 300, true),
                new Order("D", 50, true));

        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Đọc to thành tiếng: "lấy stream orders, GIỮ lại paid, LẤY amount, TÍNH tổng"
        // filter(Order::paid)  ==  filter(o -> o.paid())
        // mapToInt(Order::amount) == mapToInt(o -> o.amount())
        int total = orders.stream()
                .filter(Order::paid) // intermediate: giữ paid=true  (A, C, D)
                .mapToInt(Order::amount) // intermediate: 100, 300, 50
                .sum(); // terminal: 450
        check(total == 450, "mẫu: tổng paid == 450", "");

        System.out.println("\n--- TODO-1: filter + map + sorted + toList ---");
        // ĐỀ: lấy id của các order THỎA CẢ 2 điều kiện (paid && amount>=100),
        // rồi sắp xếp A-Z. Kết quả đúng: [A, C]  (D bị loại vì 50<100, B vì chưa paid)
        // - Hint 1: filter trước (điều kiện kép dùng &&), rồi map(Order::id), rồi sorted(), rồi toList().
        // - Hint 2: khung đúng là:
        //     orders.stream().filter(o -> ...).map(...).sorted().toList()
        // - LỖI THƯỜNG GẶP:
        //     quên 1 trong 2 điều kiện -> ra [A, B, C] hoặc [A, C, D]
        //     map trước filter -> vẫn chạy nhưng tư duy ngược (lọc trước, biến đổi sau)
        //     thiếu .toList() -> mới chỉ là Stream, chưa phải List (lỗi kiểu)
        List<String> ids = List.of("SAI"); // TODO-1: thay bằng pipeline thật
        check(ids.equals(List.of("A", "C")), "T1: ids == [A, C]", "filter kép rồi map id rồi sorted");

        System.out.println("\n--- TODO-2: max + Optional ---");
        // ĐỀ: tìm order ĐÃ PAID có amount lớn nhất. Kết quả: C (300).
        // - Hint 1: filter paid trước, rồi .max(so sánh theo amount).
        // - Hint 2: max cần 1 Comparator: Comparator.comparingInt(Order::amount)
        // - Hint 3: kết quả của max là Optional<Order> (vì list lọc xong có thể rỗng!).
        //   Lấy id bằng: biggest.map(Order::id).orElse("none")
        // - LỖI THƯỜNG GẶP:
        //     biggest.get() khi rỗng -> NoSuchElementException, nên dùng orElse/map
        //     quên filter paid -> ra B (200) hoặc C tùy data, sai đề
        Optional<Order> biggest = Optional.empty(); // TODO-2: thay bằng pipeline thật
        String biggestId = biggest.map(Order::id).orElse("none");
        check("C".equals(biggestId), "T2: biggest paid là C", "filter paid + max theo amount");

        System.out.println("\n--- TODO-3: lazy + short-circuit (quan sát, không chấm điểm) ---");
        // ĐỀ: chạy đoạn dưới, đếm xem "filter ..." in ra mấy lần. Giải thích vì sao.
        // - KIẾN THỨC: limit(1) là short-circuit: đủ 1 phần tử thì DỪNG, không duyệt tiếp.
        //   Nên chỉ thấy "filter A" 1 lần rồi "got A", dù list có 4 phần tử.
        // - THỬ THÊM: bỏ .limit(1) chạy lại -> sẽ thấy filter chạy cho cả 4 phần tử.
        // - THỬ THÊM: thêm .peek(System.out::println) giữa filter và limit để thấy dòng chảy.
        orders.stream()
                .filter(o -> {
                    System.out.println("  filter " + o.id());
                    return o.paid();
                })
                .limit(1)
                .forEach(o -> System.out.println("  got " + o.id()));
        System.out.println("  Hỏi: vì sao chỉ filter 1 lần? (đáp án: limit short-circuit + lazy)");
        check(true, "T3: đã quan sát lazy (tự giác)", "");

        System.out.println("\n--- TODO-4: bẫy stream dùng 2 lần ---");
        // ĐỀ: bỏ comment 3 dòng dưới, chạy, đọc tên exception. Rồi comment lại.
        // - KIẾN THỨC: stream như vòi nước 1 chiều — đã xả (terminal) thì không dùng lại.
        // - LỖI SẼ THẤY: IllegalStateException: stream has already been operated upon or closed
        // - CÁCH ĐÚNG: mỗi lần dùng gọi lại orders.stream() mới.
        // var s = orders.stream();
        // s.count();
        // s.count(); // <-- lỗi ở đây
        System.out.println("(bỏ comment 3 dòng TODO-4 trong file để thấy lỗi, rồi comment lại)");
        check(true, "T4: đã thử reuse-stream (tự giác)", "");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> đọc hint từng TODO rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY:");
        System.out.println(" 1. sorted() là stateful (phải thấy hết mới sắp được), filter/map là stateless. Vì sao?");
        System.out.println(" 2. peek() để debug, không nên dùng làm logic chính. Vì sao?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: List<String> ids = orders.stream()
     *          .filter(o -> o.paid() && o.amount() >= 100)
     *          .map(Order::id).sorted().toList();
     *  T2: Optional<Order> biggest = orders.stream()
     *          .filter(Order::paid)
     *          .max(Comparator.comparingInt(Order::amount));
     */
}
