import java.util.ArrayList;
import java.util.List;

/**
 * L2 — Mini test-framework: test() tay + runner đếm pass/fail.
 * Hiểu framework từ bên trong trước khi dùng JUnit thật.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Test runner = vòng lặp gọi từng test, bắt lỗi, đếm PASS/FAIL (JUnit làm y vậy).
 * - eq(expected, actual): ném AssertionError nếu khác nhau -> runner bắt -> FAIL.
 * - Lifecycle: setup (new MyStack) -> act (push/pop) -> assert (eq). Mỗi test PHẢI
 *   tự tạo stack mới trong thân test (test isolation): dùng chung stack sẽ làm
 *   test trước làm bẩn test sau -> flaky (lúc xanh lúc đỏ).
 * - MyStack.pop() trên stack rỗng ném IllegalStateException (không phải return -1).
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L2_MiniTest.java && java -cp . L2_MiniTest
 */
public class L2_MiniTest {

    // ---- code cần test: stack đơn giản ----
    static class MyStack {
        private final List<Integer> data = new ArrayList<>();

        void push(int x) { data.add(x); }

        int pop() {
            if (data.isEmpty()) throw new IllegalStateException("empty");
            return data.remove(data.size() - 1);
        }

        int size() { return data.size(); }
        boolean isEmpty() { return data.isEmpty(); }
    }

    // ---- mini-framework ----
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

    static void test(String name, String hint, Runnable body) {
        try {
            body.run(); // mỗi test tự tạo stack mới -> isolation
            passed++;
            System.out.println("PASS " + name);
        } catch (AssertionError | Exception e) {
            failed++;
            System.out.println("FAIL " + name + "  <-- " + hint + " (" + e.getMessage() + ")");
        }
    }

    static void eq(Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("expected=" + expected + " actual=" + actual);
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Mẫu: push 1 phần tử -> size == 1. Chạy luôn, không cần sửa.
        // Chú ý: stack được new NGAY TRONG test -> test sau không bị bẩn.
        test("mẫu: push tăng size", "", () -> {
            MyStack s = new MyStack();
            s.push(1);
            eq(1, s.size());
        });

        System.out.println("\n--- TODO-1: push-pop LIFO ---");
        // ĐỀ: push 1 rồi 2, pop ra phải được 2 trước 1 sau (LIFO).
        // KẾT QUẢ MONG ĐỢI: first == 2, second == 1.
        // - Hint 1: first = s.pop() (lần pop đầu), second = s.pop().
        // - Hint 2: đừng gọi s.size() thay cho pop — size không lấy phần tử ra.
        // - LỖI THƯỜNG GẶP:
        //     đoán first == 1 (FIFO của queue) -> sai, stack là LIFO
        //     quên push 2 lần mà chỉ push 1 -> pop lần 2 ném IllegalStateException
        test("T1: push-pop LIFO", "gợi ý: first = s.pop(), second = s.pop()", () -> {
            MyStack s = new MyStack();
            s.push(1);
            s.push(2);
            int first = -1; // TODO-1: sửa thành s.pop()
            int second = -1; // TODO-1: sửa thành s.pop()
            eq(2, first);
            eq(1, second);
        });

        System.out.println("\n--- TODO-2: pop rỗng ném lỗi ---");
        // ĐỀ: pop trên stack rỗng PHẢI ném IllegalStateException; đặt threw = true khi bắt được.
        // KẾT QUẢ MONG ĐỢI: threw == true.
        // - Hint 1: khung try { s.pop(); } catch (IllegalStateException e) { threw = true; }.
        // - Hint 2: biến threw phải là mảng boolean[1] hoặc AtomicBoolean vì lambda cần effectively-final;
        //   ở đây dùng mảng 1 phần tử để gán được từ trong lambda... (bài này code sẵn khung, chỉ sửa giá trị).
        // - LỖI THƯỜNG GẶP:
        //     catch (Exception e)               -> quá rộng, che lỗi sai loại
        //     return -1 thay vì ném lỗi         -> sai contract của pop(), test phải bắt throw
        //     tạo stack rồi push trước khi pop  -> stack không còn rỗng, không ném nữa
        test("T2: pop rỗng ném lỗi", "gợi ý: try { s.pop(); } catch (IllegalStateException e) { threw[0] = true; }", () -> {
            MyStack s = new MyStack();
            boolean[] threw = {false}; // TODO-2: thêm try/catch để set threw[0] = true
            try {
                s.pop();
            } catch (IllegalStateException e) {
                threw[0] = false; // TODO-2: sửa thành threw[0] = true;
            }
            eq(true, threw[0]);
        });

        System.out.println("\n--- TODO-3: isEmpty lifecycle ---");
        // ĐỀ: stack mới -> isEmpty true; sau push -> false; sau pop hết -> true lại.
        // KẾT QUẢ MONG ĐỢI: e0 == true, e1 == false, e2 == true.
        // - Hint 1: e0 = s.isEmpty() ngay sau new; e1 = s.isEmpty() sau push.
        // - Hint 2: nhớ pop ra rồi mới đo e2.
        // - LỖI THƯỜNG GẶP:
        //     dùng s.size() == 0 thay isEmpty() -> đúng nhưng bài này luyện đúng API isEmpty
        //     quên pop trước khi đo e2 -> e2 vẫn false
        test("T3: isEmpty lifecycle", "gợi ý: e0 = s.isEmpty(); push; e1 = s.isEmpty(); pop; e2 = s.isEmpty()", () -> {
            MyStack s = new MyStack();
            boolean e0 = false; // TODO-3: sửa thành s.isEmpty()
            s.push(9);
            boolean e1 = true; // TODO-3: sửa thành s.isEmpty()
            s.pop();
            boolean e2 = false; // TODO-3: sửa thành s.isEmpty()
            eq(true, e0);
            eq(false, e1);
            eq(true, e2);
        });

        System.out.println("\n--- TODO-4: isolation ---");
        // ĐỀ: 2 stack độc lập — push sang a không ảnh hưởng b.
        // KẾT QUẢ MONG ĐỢI: b.size() == 0 sau khi a.push(1).
        // - Hint 1: bSize = b.size() (đo trên b, không phải a).
        // - Hint 2: nếu dùng static/shared list trong MyStack thì test này đỏ -> đó là lý do
        //   mỗi test phải new riêng (isolation).
        // - LỖI THƯỜNG GẶP:
        //     đo a.size() rồi so với 0            -> sai đối tượng, a.size() == 1
        //     dùng chung 1 stack cho 2 biến a/b   -> mất isolation, test vô nghĩa
        test("T4: isolation", "gợi ý: bSize = b.size()", () -> {
            MyStack a = new MyStack();
            MyStack b = new MyStack();
            a.push(1);
            int bSize = -1; // TODO-4: sửa thành b.size()
            eq(0, bSize);
        });

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. setup/teardown để làm gì? Vì sao mỗi test phải new stack riêng?");
        System.out.println(" 2. Test flaky thường do đâu? (gợi ý: state dùng chung, random, thời gian, mạng)");
        System.out.println(" 3. eq() ném AssertionError để làm gì? Runner bắt nó ở đâu?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: int first = s.pop(); int second = s.pop();
     *  T2: trong catch: threw[0] = true; (giữ try { s.pop(); })
     *  T3: boolean e0 = s.isEmpty(); boolean e1 = s.isEmpty(); boolean e2 = s.isEmpty();
     *  T4: int bSize = b.size();
     */
}
