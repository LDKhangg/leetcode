import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * L2 — equals/hashCode + Collections (bẫy phỏng vấn kinh điển).
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút trước khi làm):
 * - Contract: a.equals(b) == true  =>  a.hashCode() == b.hashCode().
 *   (Chiều ngược KHÔNG bắt buộc: trùng hash chưa chắc đã equal.)
 * - HashMap tìm key theo hashCode TRƯỚC, equals SAU.
 *   Vỡ hashCode -> get() trả null dù equals vẫn true (xem demo BadKey).
 * - Key phải IMMUTABLE: mutate field tham gia hashCode -> key "rơi sai bucket",
 *   mất trong map dù vẫn cầm đúng reference (xem TODO-2).
 * - == so sánh reference, equals() so sánh giá trị. Với key trong Map luôn dùng equals().
 * ============================================================================
 * CÁCH LÀM: mỗi TODO bên dưới đang để SAI cố ý -> chạy sẽ thấy FAIL.
 * Sửa từng chỗ TODO cho tới khi tất cả PASS.
 * Run: javac L2_EqualsHash.java && java -cp . L2_EqualsHash
 */
public class L2_EqualsHash {

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

    // Key ĐÚNG: immutable + equals/hashCode đầy đủ.
    static final class GoodKey {
        final String id;

        GoodKey(String id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof GoodKey g)) return false;
            return Objects.equals(id, g.id);
        }

        @Override
        public int hashCode() { return Objects.hash(id); }
    }

    // Key SAI kinh điển: có equals nhưng THIẾU hashCode -> get() luôn null.
    // (Giữ nguyên để demo, KHÔNG sửa class này — bài sửa nằm ở NewKey bên dưới.)
    static final class BadKey {
        final String id;

        BadKey(String id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof BadKey b)) return false;
            return Objects.equals(id, b.id);
        }
    }

    // Key mutable SAI: hashCode phụ thuộc field đổi được -> mutate xong mất trong map.
    static final class MutableKey {
        String id;

        MutableKey(String id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MutableKey k)) return false;
            return Objects.equals(id, k.id);
        }

        @Override
        public int hashCode() { return Objects.hash(id); }
    }

    // Key mới: đã có equals đúng, hashCode đang SAI cố ý.
    static final class NewKey {
        final String id;

        NewKey(String id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof NewKey k)) return false;
            return Objects.equals(id, k.id);
        }

        @Override
        public int hashCode() { return System.identityHashCode(this); } // TODO-1: sửa dòng này
    }

    // So sánh 2 GoodKey theo giá trị — đang SAI cố ý.
    static boolean sameId(GoodKey a, GoodKey b) {
        return a == b; // TODO-3: sửa thành so sánh giá trị
    }

    public static void main(String[] args) {
        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // GoodKey: get bằng key MỚI nhưng equal -> vẫn thành công. Chạy luôn, không sửa.
        Map<GoodKey, String> m0 = new HashMap<>();
        m0.put(new GoodKey("k1"), "v1");
        check("v1".equals(m0.get(new GoodKey("k1"))), "mẫu: GoodKey get bằng key equal", "");
        check(new GoodKey("k1").hashCode() == new GoodKey("k1").hashCode(),
                "mẫu: equal -> hashCode bằng nhau", "");

        System.out.println("\n--- demo BadKey (đọc hiểu, không sửa) ---");
        // BadKey: equals true nhưng hash khác -> get null. Đây chính là cái bẫy.
        Map<BadKey, String> mBad = new HashMap<>();
        mBad.put(new BadKey("k1"), "v1");
        System.out.println("  bad equals=" + new BadKey("k1").equals(new BadKey("k1")) + " (true)");
        System.out.println("  bad get=" + mBad.get(new BadKey("k1")) + " (null — bẫy!)");
        check(mBad.get(new BadKey("k1")) == null, "demo: BadKey thiếu hashCode -> get null", "");

        System.out.println("\n--- TODO-1: viết hashCode đúng cho NewKey ---");
        // ĐỀ: sửa NewKey.hashCode() để Map tìm được key equal.
        // KẾT QUẢ MONG ĐỢI: put 1 key, get bằng key mới nhưng equal -> ra "v1".
        // - Hint 1: return Objects.hash(id);  (giống hệt GoodKey)
        // - Hint 2: hashCode phải dùng ĐÚNG field đã dùng trong equals (ở đây là id).
        // - LỖI THƯỜNG GẶP:
        //     System.identityHashCode(this) -> sai, mỗi object 1 hash -> equal mà khác bucket
        //     return 42;  -> biên dịch + chạy "đúng" nhưng mọi key dồn 1 bucket (O(n), vẫn FAIL review)
        //     quên @Override -> vô tình overload, HashMap không gọi hàm của bạn
        Map<NewKey, String> m1 = new HashMap<>();
        m1.put(new NewKey("k1"), "v1");
        check("v1".equals(m1.get(new NewKey("k1"))), "T1: NewKey get bằng key equal",
                "gợi ý: return Objects.hash(id)");

        System.out.println("\n--- TODO-2: thí nghiệm key mutable (đoán trước, chạy kiểm chứng) ---");
        // ĐỀ: đọc 2 dòng in bên dưới, ĐOÁN rồi điền đáp án vào biến doanMatKey:
        // sau khi mutate, get(mk) (đúng object cũ) có trả null không?
        // KẾT QUẢ MONG ĐỢI: đoán đúng hành vi thật của JVM.
        // - Hint 1: hashCode = Objects.hash(id); id đổi "k" -> "CHANGED" thì hash đổi.
        // - Hint 2: HashMap đã cất entry vào bucket của hash CŨ, get() tìm ở bucket của hash MỚI.
        // - LỖI THƯỜNG GẶP:
        //     "cùng object thì phải tìm được" -> sai, HashMap không quét toàn bộ, nó nhảy theo hash
        //     đoán bừa mà không chạy -> bài này bắt chạy để thấy tận mắt
        MutableKey mk = new MutableKey("k");
        Map<MutableKey, String> m3 = new HashMap<>();
        m3.put(mk, "v");
        mk.id = "CHANGED";
        System.out.println("  mutable get(same obj)=" + m3.get(mk));
        System.out.println("  mutable get(orig key)=" + m3.get(new MutableKey("k")));
        boolean doanMatKey = false; // TODO-2: quan sát 2 dòng trên rồi sửa thành true nếu get(mk) == null
        check(doanMatKey == (m3.get(mk) == null), "T2: đoán đúng số phận của key bị mutate",
                "gợi ý: hash đổi -> rơi sai bucket -> get null -> đáp án là true");
        check(m3.get(new MutableKey("k")) == null, "T2: key mới với id cũ cũng không cứu được", "");

        System.out.println("\n--- TODO-3: == vs equals ---");
        // ĐỀ: sửa sameId() để so sánh 2 GoodKey theo GIÁ TRỊ.
        // KẾT QUẢ MONG ĐỢI: sameId(new GoodKey("k"), new GoodKey("k")) == true.
        // - Hint 1: return Objects.equals(a.id, b.id);  (hoặc a.equals(b))
        // - Hint 2: == chỉ true khi cùng 1 object — 2 key mới khác object luôn false.
        // - LỖI THƯỜNG GẶP:
        //     a == b            -> sai, so sánh reference
        //     a.id == b.id      -> sai kiểu khác: String cũng phải so bằng equals
        check(sameId(new GoodKey("k"), new GoodKey("k")), "T3: 2 key equal theo giá trị",
                "gợi ý: Objects.equals(a.id, b.id)");
        check(!sameId(new GoodKey("k"), new GoodKey("z")), "T3: khác id -> false", "");

        System.out.println("\n--- TODO-4: sắp xếp bằng Comparator ---");
        // ĐỀ: sắp xếp mảng tên theo độ dài tăng dần. Kết quả: [Jo, Bob, Alice, Christopher]
        // - Hint 1: Comparator.comparingInt(...) nhận 1 Function biến tên -> số.
        // - Hint 2: function đó chính là String::length.
        // - LỖI THƯỜNG GẶP:
        //     (a, b) -> 0  -> sai, comparator luôn 0 = "mọi phần tử bằng nhau", mảng đứng yên
        //     (a, b) -> a.length() - b.length()  -> chạy được nhưng dài, dễ tràn số với kiểu lớn
        String[] names = {"Bob", "Alice", "Jo", "Christopher"};
        Arrays.sort(names, (a, b) -> 0); // TODO-4: thay comparator đúng vào đây
        check(Arrays.toString(names).equals("[Jo, Bob, Alice, Christopher]"),
                "T4: sort theo độ dài", "gợi ý: Comparator.comparingInt(String::length)");

        System.out.println("\n--- TODO-5: fail-fast (thí nghiệm, đọc kỹ) ---");
        // ĐỀ: bỏ comment 2 dòng bên dưới, chạy để THẤY ConcurrentModificationException,
        // rồi comment lại (để file chạy tiếp) và đặt daThuFailFast = true.
        // KẾT QUẢ MONG ĐỢI: đã tận mắt thấy exception ít nhất 1 lần.
        // - Hint 1: for-each dùng iterator ngầm; add() trong lúc lặp -> iterator "fail-fast".
        // - Hint 2: muốn thêm/xóa khi lặp phải dùng iterator.remove() hoặc lặp trên bản copy.
        // - LỖI THƯỜNG GẶP:
        //     nghĩ "thêm phần tử thì vòng lặp chạy tiếp bình thường" -> sai, fail-fast ném ngay
        //     bỏ comment rồi quên comment lại -> file không chạy tiếp được
        // var list = new java.util.ArrayList<>(java.util.List.of(1, 2, 3));
        // for (int x : list) list.add(x); // <-- bỏ comment dòng này để thấy exception
        System.out.println("(đọc code TODO-5 trong file, bỏ comment để thấy lỗi rồi comment lại)");
        boolean daThuFailFast = false; // TODO-5: đặt true sau khi đã thấy exception
        check(daThuFailFast, "T5: đã thử fail-fast (tự giác nhé)", "bỏ comment 2 dòng trên, chạy, rồi đặt true");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> sửa từng TODO theo hint rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY (che đáp án, tự trả lời):");
        System.out.println(" 1. Vì sao equal thì hash phải bằng, nhưng trùng hash chưa chắc equal? (gợi ý: bucket + equals xác minh lại)");
        System.out.println(" 2. Vì sao key HashMap nên immutable? (thử kể lại thí nghiệm TODO-2 bằng lời của mình)");
        System.out.println(" 3. Fail-fast iterator bảo vệ điều gì? (gợi ý: phát hiện mutate trong lúc lặp thay vì lặp sai lặng lẽ)");
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: @Override public int hashCode() { return Objects.hash(id); }
     *  T2: boolean doanMatKey = true;
     *  T3: return Objects.equals(a.id, b.id);   (hoặc return a.equals(b);)
     *  T4: Arrays.sort(names, Comparator.comparingInt(String::length));
     *  T5: bỏ comment 2 dòng, chạy thấy ConcurrentModificationException,
     *      comment lại, đặt daThuFailFast = true;
     */
}
