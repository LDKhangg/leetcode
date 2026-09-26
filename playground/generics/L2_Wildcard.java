import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * L2 — Wildcard + PECS (bẫy phỏng vấn kinh điển).
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - List<Integer> KHÔNG phải con của List<Number> (generics bất biến/invariant),
 *   nên cần wildcard: List<? extends Number> chấp nhận List<Integer>, List<Double>...
 * - PECS = "Producer Extends, Consumer Super":
 *   + Producer (chỉ ĐỌC ra): dùng ? extends T — đọc ra được kiểu T, nhưng CẤM add
 *     (vì không biết list thật đựng con nào của T).
 *   + Consumer (chỉ GHI vào): dùng ? super T — add được T (và con của T), nhưng đọc
 *     ra chỉ được kiểu Object (vì list thật có thể là List<Number> hay List<Object>).
 * - Copy kinh điển: <T> void copy(List<? extends T> src, List<? super T> dest)
 *   (src là producer -> extends, dest là consumer -> super).
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac playground/generics/L2_Wildcard.java && java -cp playground/generics L2_Wildcard
 */
public class L2_Wildcard {

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

    // PRODUCER: chỉ đọc từ list số -> dùng ? extends Number (thân đang SAI cố ý).
    static double sum(List<? extends Number> xs) {
        return 0; // TODO-1: cộng dồn n.doubleValue() vào total rồi trả về
    }

    // CONSUMER: ghi số nguyên vào list -> dùng ? super Integer (thân đang SAI cố ý: rỗng).
    static void addTwoInts(List<? super Integer> dest) {
        // TODO-2: thêm dest.add(1); dest.add(2);
    }

    // Copy src (producer) sang dest (consumer) — signature đã đúng, THÂN sai cố ý.
    static <T> void copy(List<? extends T> src, List<? super T> dest) {
        dest.clear(); // TODO-3: copy từng phần tử src sang dest thay vì clear
    }

    // Đọc phần tử đầu từ producer (thân đang SAI cố ý).
    static Number firstNum(List<? extends Number> xs) {
        return null; // TODO-4: sửa thành return xs.get(0);
    }

    // Đọc phần tử cuối từ consumer (thân đang SAI cố ý).
    static Integer lastInt(List<? super Integer> xs) {
        return 0; // TODO-5: sửa thành return (Integer) xs.get(xs.size() - 1);
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        List<Integer> ints = new ArrayList<>(Arrays.asList(1, 2, 3));
        Number n0 = ints.get(0); // đọc từ List<Integer> qua lăng kính Number: luôn OK
        check(n0.intValue() == 1, "mẫu: đọc Number từ List<Integer>", "");

        System.out.println("\n--- TODO-1: producer ? extends (chỉ đọc, cấm add) ---");
        // ĐỀ: tính tổng các số trong list (Integer, Double... gì cũng được).
        // KẾT QUẢ MONG ĐỢI: sum([1.5, 2.5]) == 4.0.
        // - Hint 1: đọc ra được Number nên gọi n.doubleValue() rồi cộng dồn.
        // - Hint 2: KHÔNG được xs.add(...) — với ? extends, add gì cũng lỗi biên dịch.
        // - LỖI THƯỜNG GẶP:
        //     xs.add(1) -> không biên dịch (producer cấm ghi, đây chính là bẫy PV)
        //     cộng n.intValue() -> mất phần thập phân, FAIL với số thực
        check(sum(Arrays.asList(1.5, 2.5)) == 4.0, "T1: sum([1.5,2.5])==4.0", "cộng dồn n.doubleValue()");

        System.out.println("\n--- TODO-2: consumer ? super (ghi Integer vào) ---");
        // ĐỀ: thêm 2 số nguyên 1 và 2 vào dest.
        // KẾT QUẢ MONG ĐỢI: dest equals [1, 2].
        // - Hint 1: với ? super Integer, dest.add(1) HỢP LỆ (1 là Integer).
        // - Hint 2: thêm đúng 2 lệnh add, đúng thứ tự 1 rồi 2.
        // - LỖI THƯỜNG GẶP:
        //     dest.add(3.14) -> không biên dịch (Double không phải Integer hay con của nó)
        //     quên add -> list rỗng, FAIL
        List<Integer> dest2 = new ArrayList<>();
        addTwoInts(dest2);
        check(dest2.equals(Arrays.asList(1, 2)), "T2: dest có đúng [1, 2]", "dest.add(1); dest.add(2);");

        System.out.println("\n--- TODO-3: copy(src extends, dest super) ---");
        // ĐỀ: chép toàn bộ src sang dest (signature PECS đã đúng, chỉ sửa THÂN hàm).
        // KẾT QUẢ MONG ĐỢI: dest thành [1, 2, 3] (đè lên nội dung cũ).
        // - Hint 1: dest.clear() trước rồi for (T e : src) dest.add(e);
        //   đọc từ extends ra T, ghi T vào super: cả 2 đều hợp lệ.
        // - Hint 2: KHÔNG clear dest nếu sau đó không chép lại (mất dữ liệu, FAIL).
        // - LỖI THƯỜNG GẶP:
        //     for (T e : dest) src.add(e); -> không biên dịch (đảo chiều: ghi vào extends)
        //     dest = src; -> chỉ trỏ lại biến cục bộ, caller không thấy gì
        List<Integer> src = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Number> dest3 = new ArrayList<>(Arrays.asList(9));
        copy(src, dest3);
        check(dest3.equals(Arrays.asList(1, 2, 3)), "T3: dest thành [1, 2, 3]", "dest.clear() rồi for (T e : src) dest.add(e);");

        System.out.println("\n--- TODO-4: đọc từ producer ra kiểu cha ---");
        // ĐỀ: trả về phần tử ĐẦU TIÊN (đọc từ ? extends ra Number luôn an toàn).
        // KẾT QUẢ MONG ĐỢI: firstNum([7, 8]) là 7.
        // - Hint 1: chỉ cần return xs.get(0); — get từ extends trả về Number, khớp kiểu trả về.
        // - LỖI THƯỜNG GẶP:
        //     return (Integer) xs.get(0); -> ép kiểu thừa, vỡ với List<Double>
        //     return null; -> NullPointerException ở caller hoặc FAIL check null
        Number r4 = firstNum(Arrays.asList(7, 8));
        check(r4 != null && r4.intValue() == 7, "T4: phần tử đầu là 7", "return xs.get(0);");

        System.out.println("\n--- TODO-5: đọc từ consumer phải cast ---");
        // ĐỀ: trả về phần tử CUỐI, ép về Integer.
        // KẾT QUẢ MONG ĐỢI: lastInt([5, 6, 7]) == 7.
        // - Hint 1: đọc từ ? super Integer chỉ được Object, nên phải cast: (Integer) xs.get(...).
        // - Hint 2: index cuối là xs.size() - 1.
        // - LỖI THƯỜNG GẶP:
        //     return xs.get(...); -> không biên dịch (Object không tự thành Integer)
        //     return 0; -> giá trị cứng, FAIL với mọi input khác 0
        check(lastInt(new ArrayList<>(Arrays.asList(5, 6, 7))) == 7,
                "T5: phần tử cuối là 7", "cast Object về Integer khi đọc từ ? super");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao List<Integer> không gán được cho List<Number>, nhưng List<Integer> lại truyền được cho List<? extends Number>?");
        System.out.println(" 2. Vì sao ? extends cấm add còn ? super cấm đọc ra kiểu cụ thể? (vẽ 2 list thật có thể đứng sau mỗi wildcard)");
        System.out.println(" 3. Trong copy(src, dest), đảo signature thành (? super, ? extends) thì dòng nào trong thân hàm vỡ đầu tiên?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: double total = 0; for (Number n : xs) total += n.doubleValue(); return total;
     *  T2: dest.add(1); dest.add(2);
     *  T3: dest.clear(); for (T e : src) dest.add(e);
     *  T4: return xs.get(0);
     *  T5: return (Integer) xs.get(xs.size() - 1);
     */
}
