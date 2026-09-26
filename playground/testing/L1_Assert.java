import java.util.List;

/**
 * L1 — Test tư duy bằng assert thủ công (không cần JUnit).
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - AAA = Arrange (chuẩn bị dữ liệu) -> Act (gọi hàm cần test) -> Assert (so sánh).
 * - assert của Java MẶC ĐỊNH TẮT, chỉ chạy khi thêm cờ -ea (enable assertions).
 *   Quên -ea là assert bị bỏ qua im lặng -> test xanh giả. Vì vậy file này dùng
 *   thêm hàm check() tay (luôn chạy, không cần -ea) để không bị lừa.
 * - average(xs) = tổng / số phần tử (làm tròn xuống), ném IllegalArgumentException
 *   nếu list rỗng. Edge cases hay bị hỏi: 1 phần tử, số âm, list rỗng, tràn int.
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L1_Assert.java && java -ea -cp . L1_Assert
 */
public class L1_Assert {

    // Hàm cần test: trung bình cộng, làm tròn xuống. Ném lỗi nếu list rỗng.
    static int average(List<Integer> xs) {
        if (xs.isEmpty()) throw new IllegalArgumentException("empty");
        int sum = 0;
        for (int x : xs) sum += x;
        return sum / xs.size();
    }

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

    static void checkThrows(Runnable r, String name, String hint) {
        try {
            r.run();
            failed++;
            System.out.println("FAIL " + name + "  <-- " + hint + " (chạy xong mà không ném lỗi!)");
        } catch (IllegalArgumentException e) {
            passed++;
            System.out.println("PASS " + name);
        } catch (Exception e) {
            failed++;
            System.out.println("FAIL " + name + "  <-- ném sai loại: " + e + " (" + hint + ")");
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Arrange-Act-Assert mẫu: avg(1,2,3) = 6/3 = 2. Chạy luôn, không cần sửa.
        check(average(List.of(1, 2, 3)) == 2, "mẫu: avg(1,2,3)==2", "");
        assert average(List.of(2, 4)) == 3 : "assert mẫu avg(2,4)==3 (chỉ chạy khi có -ea)";

        System.out.println("\n--- TODO-1: 1 phần tử ---");
        // ĐỀ: tính avg([5]), gán vào got1.
        // KẾT QUẢ MONG ĐỢI: got1 == 5.
        // - Hint 1: gọi average(List.of(5)).
        // - Hint 2: List.of(5) là list 1 phần tử, không cần new ArrayList.
        // - LỖI THƯỜNG GẶP:
        //     average(5)            -> sai, average nhận List, không nhận int
        //     average(List.of())    -> sai, list rỗng sẽ ném lỗi, không trả 5
        int got1 = -1; // TODO-1: sửa thành average(List.of(5))
        check(got1 == 5, "T1: avg([5])==5", "gợi ý: average(List.of(5))");

        System.out.println("\n--- TODO-2: số âm ---");
        // ĐỀ: tính avg([-2, 2]), gán vào got2.
        // KẾT QUẢ MONG ĐỢI: got2 == 0 (tổng 0 / 2).
        // - Hint 1: average(List.of(-2, 2)).
        // - Hint 2: phép chia int làm tròn về 0, (-2+2)/2 = 0 chuẩn.
        // - LỖI THƯỜNG GẶP:
        //     List.of("-2", "2")   -> sai kiểu, phải là Integer không phải String
        //     đoán == -1 hay == 1  -> sai, tính lại: tổng = 0
        int got2 = 999; // TODO-2: sửa thành average(List.of(-2, 2))
        check(got2 == 0, "T2: avg([-2,2])==0", "gợi ý: average(List.of(-2, 2))");

        System.out.println("\n--- TODO-3: list rỗng phải ném lỗi ---");
        // ĐỀ: đặt threw3 = true nếu average(List.of()) ném IllegalArgumentException.
        // KẾT QUẢ MONG ĐỢI: threw3 == true (checkThrows bên dưới PASS).
        // - Hint 1: bọc trong try { average(List.of()); } catch (IllegalArgumentException e) { ... }.
        // - Hint 2: xem lại checkThrows ở Bài 0 — copy khung try/catch đó.
        // - LỖI THƯỜNG GẶP:
        //     catch (Exception e)   -> bắt quá rộng, che mất lỗi sai loại
        //     quên gọi average() trong try -> khối try không ném gì -> FAIL "không ném lỗi"
        boolean threw3 = false; // TODO-3: try average(List.of()) rồi set threw3 = true trong catch
        check(threw3, "T3a: tự bắt lỗi rỗng (threw3==true)", "gợi ý: try { average(List.of()); } catch (IllegalArgumentException e) { threw3 = true; }");
        checkThrows(() -> average(List.of(1)), "T3b: checkThrows xác nhận ném lỗi", "gợi ý: phải truyền list RỖNG List.of() thì mới ném lỗi"); // TODO-3: sửa List.of(1) thành List.of()

        System.out.println("\n--- TODO-4: edge avg(2,4) + assert -ea ---");
        // ĐỀ: tính avg([2, 4]) gán vào got4, rồi assert thuần cũng phải đúng.
        // KẾT QUẢ MONG ĐỢI: got4 == 3, và dòng assert không nổ khi chạy với -ea.
        // - Hint 1: average(List.of(2, 4)) = 6/2 = 3.
        // - Hint 2: chạy ĐÚNG lệnh có -ea: java -ea -cp . L1_Assert (quên -ea là assert bị skip).
        // - LỖI THƯỜNG GẶP:
        //     đoán == 2 (làm tròn xuống?) -> sai, 6/2 = 3 chẵn, không có phần dư
        //     chạy java mà thiếu -ea     -> assert bị bỏ qua, tưởng PASS nhưng chưa test gì
        int got4 = 0; // TODO-4: sửa thành average(List.of(2, 4))
        check(got4 == 3, "T4: avg([2,4])==3", "gợi ý: average(List.of(2, 4))");
        assert got4 == 3 : "assert avg(2,4)==3 (nhớ chạy với -ea)";

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. AAA là gì? Viết 1 test cho average theo đúng 3 bước đó.");
        System.out.println(" 2. Quên -ea thì điều gì xảy ra với dòng assert? Vì sao check() tay an toàn hơn?");
        System.out.println(" 3. average(List.of(MAX_VALUE, MAX_VALUE)) trả về gì? Vì sao tràn int là edge case đáng test?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: int got1 = average(List.of(5));
     *  T2: int got2 = average(List.of(-2, 2));
     *  T3: try { average(List.of()); } catch (IllegalArgumentException e) { threw3 = true; }
     *      T3b: checkThrows(() -> average(List.of()), ...); // sửa List.of(1) thành List.of()
     *  T4: int got4 = average(List.of(2, 4));
     */
}
