import java.util.HashMap;
import java.util.Map;

/**
 * L3 — Test collection + mock tay (fake dependency).
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - DI (dependency injection): UserService KHÔNG tự new Mailer mà NHẬN mailer qua
 *   constructor. Nhờ vậy test có thể nhét FakeMailer vào để kiểm tra "đã gửi gì".
 * - Fake = bản thay thế chạy được thật (ghi lại sent/lastTo/lastBody để assert).
 *   Stub = trả giá trị cứng. Mock = còn kiểm tra "được gọi mấy lần, với tham số nào".
 *   File này dùng FakeMailer (đơn giản nhất) + assert tay trên fake đó.
 * - HashMap boundary hay bị hỏi: put trùng key thì value mới thắng, size KHÔNG tăng;
 *   HashMap cho null key (khác Hashtable/ConcurrentHashMap sẽ ném NullPointerException).
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L3_MockTest.java && java -cp . L3_MockTest
 */
public class L3_MockTest {

    // ---- dependency thật: gửi mail ----
    interface Mailer {
        void send(String to, String body);
    }

    // Fake thủ công: ghi lại đã gửi gì, để assert sau.
    static class FakeMailer implements Mailer {
        int sent = 0;
        String lastTo, lastBody;

        @Override
        public void send(String to, String body) {
            sent++;
            lastTo = to;
            lastBody = body;
        }
    }

    // ---- code cần test: đăng ký user rồi gửi mail chào ----
    static class UserService {
        private final Mailer mailer;
        private final Map<String, String> users = new HashMap<>();

        UserService(Mailer mailer) { this.mailer = mailer; } // DI qua constructor

        void register(String email, String name) {
            if (users.containsKey(email)) throw new IllegalStateException("exists");
            users.put(email, name);
            mailer.send(email, "Hi " + name);
        }

        String find(String email) { return users.get(email); }
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

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Mẫu: register 1 user -> fake ghi đúng 1 mail, đúng người nhận + nội dung.
        // Chạy luôn, không cần sửa. Chú ý svc NHẬN fake qua constructor (DI).
        {
            FakeMailer fake = new FakeMailer();
            UserService svc = new UserService(fake);
            svc.register("a@x.com", "An");
            check(fake.sent == 1, "mẫu: gửi 1 mail", "");
            check("a@x.com".equals(fake.lastTo), "mẫu: đúng người nhận", "");
            check("Hi An".equals(fake.lastBody), "mẫu: đúng nội dung", "");
        }

        System.out.println("\n--- TODO-1: đăng ký trùng phải ném lỗi + không gửi thêm mail ---");
        // ĐỀ: register cùng email 2 lần -> lần 2 ném IllegalStateException, fake.sent vẫn == 1.
        // KẾT QUẢ MONG ĐỢI: threw == true, sentCount == 1.
        // - Hint 1: bọc lần register thứ 2 trong try/catch, set threw = true trong catch.
        // - Hint 2: sentCount = fake.sent (đo SAU khi đã thử register lần 2).
        // - LỖI THƯỜNG GẶP:
        //     catch (Exception e)          -> quá rộng, che lỗi sai loại
        //     đo fake.sent trước lần 2    -> luôn == 1, test vô nghĩa
        //     register email khác nhau    -> không trùng, không ném lỗi
        {
            FakeMailer fake = new FakeMailer();
            UserService svc = new UserService(fake);
            svc.register("b@x.com", "Bo");
            boolean threw = false; // TODO-1: try register trùng rồi set threw = true trong catch
            try {
                svc.register("b@x.com", "Bo2");
            } catch (IllegalStateException e) {
                threw = false; // TODO-1: sửa thành threw = true;
            }
            int sentCount = -1; // TODO-1: sửa thành fake.sent
            check(threw, "T1: trùng phải ném lỗi", "gợi ý: catch (IllegalStateException e) { threw = true; }");
            check(sentCount == 1, "T1: trùng không gửi thêm mail", "gợi ý: sentCount = fake.sent");
        }

        System.out.println("\n--- TODO-2: HashMap overwrite ---");
        // ĐỀ: put cùng key 2 lần -> value mới thắng, size vẫn 1.
        // KẾT QUẢ MONG ĐỢI: val == 2, sz == 1.
        // - Hint 1: val = m.get("k"), sz = m.size() (đo sau cả 2 lần put).
        // - Hint 2: overwrite KHÔNG tạo entry mới nên size giữ nguyên.
        // - LỖI THƯỜNG GẶP:
        //     đoán size == 2            -> sai, trùng key không tăng size
        //     đoán val == 1 (giữ cũ)    -> sai, put sau ghi đè put trước
        {
            Map<String, Integer> m = new HashMap<>();
            m.put("k", 1);
            m.put("k", 2);
            int val = -1; // TODO-2: sửa thành m.get("k")
            int sz = -1; // TODO-2: sửa thành m.size()
            check(val == 2, "T2: overwrite value mới thắng", "gợi ý: val = m.get(\"k\")");
            check(sz == 1, "T2: overwrite giữ size=1", "gợi ý: sz = m.size()");
        }

        System.out.println("\n--- TODO-3: HashMap null key ---");
        // ĐỀ: HashMap cho phép null key; put(null, 9) rồi get(null) phải ra 9.
        // KẾT QUẢ MONG ĐỢI: gotNull == 9.
        // - Hint 1: gotNull = m.get(null) (sau khi đã put(null, 9)).
        // - Hint 2: chỉ HashMap/HashSet cho null; Hashtable và ConcurrentHashMap sẽ ném NPE.
        // - LỖI THƯỜNG GẶP:
        //     m.get("null") (chuỗi) thay vì m.get(null) -> sai, null khác "null"
        //     dùng ConcurrentHashMap rồi bảo "sao NPE"   -> đúng, nó cấm null key
        {
            Map<String, Integer> m = new HashMap<>();
            m.put(null, 9);
            Integer gotNull = -99; // TODO-3: sửa thành m.get(null)
            check(gotNull != null && gotNull == 9, "T3: HashMap cho null key", "gợi ý: gotNull = m.get(null)");
        }

        System.out.println("\n--- TODO-4: find user không tồn tại -> null ---");
        // ĐỀ: find email chưa đăng ký phải trả null.
        // KẾT QUẢ MONG ĐỢI: found == null.
        // - Hint 1: found = svc.find("no@x.com") (email chưa từng register).
        // - Hint 2: Map.get trả null khi vắng — đó chính là contract cần test.
        // - LỖI THƯỜNG GẶP:
        //     đoán "" (chuỗi rỗng) thay vì null     -> sai contract của Map.get
        //     register email đó trước rồi mới find  -> không còn vắng nữa
        {
            UserService svc = new UserService(new FakeMailer());
            String found = "sai cố ý"; // TODO-4: sửa thành svc.find("no@x.com")
            check(found == null, "T4: find vắng -> null", "gợi ý: found = svc.find(\"no@x.com\")");
        }

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. mock vs stub vs fake khác nhau thế nào? FakeMailer ở trên thuộc loại nào?");
        System.out.println(" 2. Vì sao DI (truyền Mailer qua constructor) giúp test dễ? (thử new Mailer thật bên trong xem sao)");
        System.out.println(" 3. HashMap cho null key nhưng ConcurrentHashMap thì không — vì sao boundary này đáng test?");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: trong catch: threw = true;  và  int sentCount = fake.sent;
     *  T2: int val = m.get("k"); int sz = m.size();
     *  T3: Integer gotNull = m.get(null);
     *  T4: String found = svc.find("no@x.com");
     */
}
