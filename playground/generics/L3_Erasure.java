import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * L3 — Erasure (xóa kiểu) + heap pollution + bridge method.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Erasure: generics chỉ tồn tại lúc BIÊN DỊCH. Lúc runtime, Box<String> và
 *   Box<Integer> đều chỉ còn là Box thô — cùng 1 Class object.
 *   Hệ quả: boxStr.getClass() == boxInt.getClass() là TRUE.
 * - Heap pollution: lách qua raw type (List không tham số) để nhét sai kiểu vào
 *   List<String>, quả bom chỉ nổ lúc runtime dưới dạng ClassCastException.
 * - Bridge method: khi StrBox extends Box<String> và override set(String),
 *   compiler tự sinh thêm set(Object) cầu nối (isBridge()==true) để code cũ
 *   không-generic vẫn gọi được — bằng chứng erasure để lại lúc runtime.
 * - CẤM lúc biên dịch (hiểu để tránh, không cần chạy): new T[10], x instanceof T,
 *   overload f(List<String>) + f(List<Integer>) — cả 3 đều clash sau erasure.
 * ============================================================================
 * CÁCH LÀM: TODO runtime đang để SAI cố ý -> chạy sẽ thấy FAIL; mục (đọc-hiểu)
 * chỉ cần đọc comment rồi tự thử uncomment để thấy lỗi biên dịch.
 * Run: javac playground/generics/L3_Erasure.java && java -cp playground/generics L3_Erasure
 */
public class L3_Erasure {

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

    static class Box<T> {
        private T value;
        public void set(T v) { this.value = v; }
        public T get() { return value; }
    }

    // Override set(String) -> compiler sinh thêm bridge set(Object). Thấy ở TODO-3.
    static class StrBox extends Box<String> {
        @Override
        public void set(String s) { super.set(s); }
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        Box<String> sample = new Box<>();
        sample.set("mẫu");
        check("mẫu".equals(sample.get()), "mẫu: Box<String> get/set vẫn thường", "");

        System.out.println("\n--- TODO-1: erasure — khác tham số, cùng 1 Class ---");
        // ĐỀ: so sánh Class của Box<String> và Box<Integer>.
        // KẾT QUẢ MONG ĐỢI: cùng 1 Class object (erasure xóa <String>/<Integer> lúc runtime).
        // - Hint 1: chỉ cần đổi != thành ==.
        // - Hint 2: getClass() trả về Class<?>, không còn dấu vết của T.
        // - LỖI THƯỜNG GẶP:
        //     nghĩ Box<String> và Box<Integer> là 2 class khác nhau -> dùng != là sai
        //     so sánh bằng equals trên value thay vì so Class
        Box<String> bs = new Box<>();
        bs.set("a");
        Box<Integer> bi = new Box<>();
        bi.set(1);
        boolean same = bs.getClass() != bi.getClass(); // TODO-1: đổi != thành ==
        check(same, "T1: Box<String> và Box<Integer> cùng Class", "sửa != thành ==");

        System.out.println("\n--- TODO-2: heap pollution nổ lúc runtime ---");
        // ĐỀ: nhét Integer 42 vào List<String> qua cửa raw type, hứng ClassCastException.
        // KẾT QUẢ MONG ĐỢI: threw == true (bom nổ đúng lúc đọc ra, dòng ss.get(0)).
        // - Hint 1: đổi raw.add("ok") thành raw.add(42).
        // - Hint 2: compiler chèn lệnh ép kiểu (checkcast) invisible ở ss.get(0) — Integer
        //   không ép thành String được nên mới nổ. Đây là LÝ DO Java cấm raw type.
        // - LỖI THƯỜNG GẶP:
        //     catch (Exception e) rồi nuốt luôn -> check threw vẫn false, FAIL
        //     dùng List<Object> thay raw -> lỗi biên dịch, không gán được cho List<String>
        @SuppressWarnings({"rawtypes", "unchecked"})
        List<String> ss = new ArrayList<>();
        @SuppressWarnings({"rawtypes"})
        List raw = ss; // raw type: cửa ngõ ô nhiễm heap
        raw.add("ok"); // TODO-2: đổi "ok" thành 42 để gây ô nhiễm heap
        boolean threw = false;
        try {
            String s = ss.get(0);
            check(s.length() > 0, "T2a: đọc được phần tử", "");
        } catch (ClassCastException e) {
            threw = true;
        }
        check(threw, "T2b: ô nhiễm heap gây ClassCastException lúc runtime", "raw.add(42) nhét Integer vào List<String>");

        System.out.println("\n--- TODO-3: bridge method (dấu vết erasure) ---");
        // ĐỀ: chứng minh StrBox có method cầu nối do compiler tự sinh.
        // KẾT QUẢ MONG ĐỢI: hasBridge == true.
        // - Hint 1: duyệt StrBox.class.getDeclaredMethods(), gặp m.isBridge() thì gán true.
        // - Hint 2: bridge trông như set(Object) gọi sang set(String) — javap -c StrBox sẽ thấy.
        // - LỖI THƯỜNG GẶP:
        //     dùng getMethods() của Box cha -> không thấy bridge (nó nằm ở StrBox)
        //     so sánh tên method bằng chuỗi "set" -> cả method thật lẫn bridge đều tên set
        boolean hasBridge = false; // TODO-3: duyệt getDeclaredMethods(), m.isBridge() thì gán true
        check(hasBridge, "T3: StrBox có bridge method do erasure", "for (Method m : StrBox.class.getDeclaredMethods()) if (m.isBridge()) hasBridge = true;");

        System.out.println("\n--- TODO-4 (đọc-hiểu): overload clash sau erasure ---");
        // ĐỀ: đọc 2 dòng overload bên dưới (đang comment), hiểu vì sao KHÔNG biên dịch được,
        // rồi thử bỏ comment để thấy lỗi "same erasure", xong comment lại để file chạy tiếp.
        // - KIẾN THỨC: sau erasure cả 2 đều thành f(List) -> trùng signature -> clash.
        // void f(List<String> a) {}
        // void f(List<Integer> a) {}
        System.out.println("(đọc code TODO-4 trong file, bỏ comment để thấy lỗi erasure rồi comment lại)");
        check(true, "T4: đã đọc và thử uncomment overload clash (tự giác nhé)", "");

        System.out.println("\n--- TODO-5 (đọc-hiểu + runtime): cấm new T[] / instanceof T ---");
        // ĐỀ (đọc-hiểu): 2 dòng bên dưới KHÔNG biên dịch được — hiểu vì sao rồi thử uncomment kiểm chứng:
        //   - if (o instanceof T) ... -> lỗi: T không còn tồn tại lúc runtime để kiểm tra
        //   - T[] arr = new T[10];    -> lỗi: generic array creation (mảng cần biết kiểu thật lúc runtime)
        // Object o = "hi";
        // if (o instanceof T) {}
        // PHẦN RUNTIME (làm thật): workaround chuẩn là Object[] + cast (hoặc List<T>).
        // KẾT QUẢ MONG ĐỢI: got equals "hi".
        // - Hint: chỉ cần sửa "sai" thành "hi" — dòng cast (String) tmp[0] chính là workaround.
        // - LỖI THƯỜNG GẶP:
        //     (T) tmp[0] trong static method -> T không tồn tại, lỗi biên dịch
        //     new String[10] rồi gán cho T[] -> không biên dịch nếu T chưa cố định là String
        Object[] tmp = new Object[1]; // workaround: không new T[10] được thì dùng Object[] + cast
        tmp[0] = "hi";
        String got = (String) tmp[0];
        check(got.equals("sai"), "T5: workaround đọc lại \"hi\"", "đáp án là \"hi\", không phải \"sai\""); // TODO-5: sửa "sai" thành "hi"

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao new T[10] bị cấm nhưng new Object[10] + cast lại được? (mảng reified, generics thì không)");
        System.out.println(" 2. Heap pollution ở TODO-2: vì sao lỗi chỉ nổ ở ss.get(0) mà không nổ ngay ở raw.add(42)?");
        System.out.println(" 3. Bridge method chứng tỏ điều gì về tương thích ngược của Java generics?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: boolean same = bs.getClass() == bi.getClass();
     *  T2: raw.add(42);
     *  T3: boolean hasBridge = false; for (Method m : StrBox.class.getDeclaredMethods()) if (m.isBridge()) hasBridge = true;
     *  T4: (đọc-hiểu, không có code sửa — lỗi "same erasure" là đáp án)
     *  T5: check(got.equals("hi"), ...);  // phần comment instanceof T / new T[] cứ để nguyên
     */
}
