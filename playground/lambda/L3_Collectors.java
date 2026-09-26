import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * L3 — Collectors + Optional + parallel stream.
 * ============================================================================
 * KIẾN THỨC NỀN (đọc 1 phút):
 * - collect() là terminal gom Stream thành cái khác: List, Map, chuỗi, số...
 * - groupingBy: chia thành NHIỀU nhóm theo key (trả Map<key, List>).
 * - partitioningBy: chia đúng 2 NHÓM true/false (trả Map<Boolean, List>).
 * - Optional: hộp "có thể rỗng", thay cho return null. Đừng gọi get() mù quáng.
 * - parallelStream: chia việc cho nhiều thread. Nhanh với việc NẶNG, ĐỘC LẬP;
 *   chậm/sai với việc NHẸ, CÓ THỨ TỰ, hoặc modifier chung (side-effect).
 * ============================================================================
 * CÁCH LÀM: các TODO đang để SAI cố ý -> chạy thấy FAIL, sửa dần tới PASS.
 * Run: javac L3_Collectors.java && java -cp . L3_Collectors
 */
public class L3_Collectors {

    record User(String name, String city, int age) {}

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
        List<User> users = List.of(
                new User("An", "HN", 20),
                new User("Bo", "HCM", 17),
                new User("Ci", "HN", 25),
                new User("De", "HCM", 30));

        System.out.println("--- Bài 0 (mẫu, đọc hiểu) ---");
        // Đọc to: "gom users theo city" -> {HN=[An, Ci], HCM=[Bo, De]}
        Map<String, List<User>> byCity = users.stream()
                .collect(Collectors.groupingBy(User::city));
        check(byCity.keySet().containsAll(List.of("HN", "HCM")) && byCity.get("HN").size() == 2,
                "mẫu: group HN=2, HCM=2", "");

        System.out.println("\n--- TODO-1: groupingBy + counting ---");
        // ĐỀ: đếm user mỗi city. Kết quả: {HN=2, HCM=2}.
        // - Hint 1: groupingBy có bản 2 tham số: (key, "gom tiếp kiểu gì").
        //   Ở đây "gom tiếp" là đếm: Collectors.counting().
        // - Hint 2: khung: groupingBy(User::city, Collectors.counting())
        // - LỖI THƯỜNG GẶP:
        //     quên counting() -> ra Map<String, List> chứ không phải Map<String, Long>
        //     so sánh Long với int (2) bằng == -> sai, phải equals hoặc == 2L
        Map<String, Long> countByCity = Map.of("SAI", 0L); // TODO-1: thay pipeline thật
        check(countByCity.getOrDefault("HN", -1L) == 2L
                && countByCity.getOrDefault("HCM", -1L) == 2L,
                "T1: count HN=2, HCM=2", "groupingBy(User::city, counting())");

        System.out.println("\n--- TODO-2: partitioningBy + joining ---");
        // ĐỀ (2 ý):
        //  (a) chia users thành adult (age>=18) / kid. Đúng: adult=3 (An, Ci, De), kid=1 (Bo).
        //  (b) nối tên adult thành chuỗi "An, Ci, De".
        // - Hint 1: partitioningBy(u -> u.age() >= 18) trả Map<Boolean, List<User>>.
        //   Lấy nhóm adult bằng .get(true).
        // - Hint 2: nối chuỗi: .stream().map(User::name).collect(Collectors.joining(", "))
        // - LỖI THƯỜNG GẶP:
        //     dùng groupingBy thay partitioning -> vẫn ra nhưng key là true/false rời rạc, mất ý nghĩa 2-nhóm
        //     joining() thiếu delimiter ", " -> ra "AnCiDe" dính nhau
        Map<Boolean, List<User>> partitioned = null; // TODO-2a: thay pipeline thật
        String adultNames = "SAI"; // TODO-2b: thay pipeline thật
        check(partitioned != null && partitioned.get(true).size() == 3
                && partitioned.get(false).size() == 1,
                "T2a: adult=3, kid=1", "partitioningBy(u -> u.age() >= 18)");
        check("An, Ci, De".equals(adultNames), "T2b: join tên adult",
                "map(User::name) + joining(\", \")");

        System.out.println("\n--- TODO-3: Optional an toàn ---");
        // ĐỀ: tìm user tên "Bo", lấy age. Vắng thì -1. Đúng: 17.
        // - Hint 1: pipeline: filter theo tên -> findFirst() (ra Optional) -> map(User::age) -> orElse(-1).
        // - Hint 2: findFirst trả Optional vì "có thể không tìm thấy ai".
        // - LỖI THƯỜNG GẶP (bẫy phỏng vấn):
        //     .get() trực tiếp khi rỗng -> NoSuchElementException. Luôn orElse/orElseGet/orElseThrow.
        //     orElse(heavy()) vs orElseGet(() -> heavy()): orElse LUÔN tính heavy() dù không cần,
        //     orElseGet lười hơn (chỉ tính khi vắng). Với fallback rẻ thì không sao, fallback đắt thì khác biệt lớn.
        int boAge = -999; // TODO-3: thay pipeline thật
        check(boAge == 17, "T3: Bo age == 17", "filter + findFirst + map + orElse");
        Optional<User> missing = users.stream().filter(u -> u.name().equals("ZZ")).findFirst();
        check(!missing.isPresent(), "T3b: ZZ vắng mặt", "");
        // THỬ THÊM (uncomment): missing.orElseThrow(() -> new IllegalArgumentException("not found"));

        System.out.println("\n--- TODO-4: parallel (quan sát + 1 câu tư duy) ---");
        // ĐỀ: đoạn dưới dùng parallelStream cho việc nặng (sleep 50ms mỗi user).
        //  Chạy, ghi lại thời gian. Rồi đổi thành .stream() thường, chạy lại, so sánh.
        // - KỲ VỌNG: parallel ~50-80ms (chia nhau ngủ), thường ~200ms (ngủ nối tiếp 4x50).
        // - KHI NÀO PARALLEL CÓ ÍCH: việc nặng, độc lập, data lớn.
        // - KHI NÀO HẠI/SAI: việc nhẹ (overhead chia thread còn lâu hơn),
        //   cần thứ tự (dùng forEachOrdered nếu cần), hoặc cùng sửa 1 ArrayList chung (race!).
        long t0 = System.currentTimeMillis();
        long sum = users.parallelStream().mapToInt(u -> heavy(u.age())).sum();
        long ms = System.currentTimeMillis() - t0;
        System.out.println("  parallel sum=" + sum + " in " + ms + "ms");
        check(sum == 92, "T4: sum vẫn đúng = 92", "");
        System.out.println("  Giờ đổi parallelStream() -> stream(), chạy lại, so thời gian.");

        System.out.printf("%n==== %d PASS, %d FAIL ====%n", passed, failed);
        if (failed > 0) {
            System.out.println("Còn FAIL -> đọc hint từng TODO rồi chạy lại.");
            System.exit(1);
        }
        System.out.println("TỰ TƯ DUY:");
        System.out.println(" 1. orElse vs orElseGet: fallback đắt (query DB) thì dùng cái nào? Vì sao?");
        System.out.println(" 2. parallel + ArrayList.add chung: sai ở đâu? (gợi ý: race condition, mất phần tử)");
    }

    static int heavy(int x) {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return x;
    }

    /*
     * ĐÁP ÁN (bí quá mới mở):
     *  T1: users.stream().collect(Collectors.groupingBy(User::city, Collectors.counting()));
     *  T2a: users.stream().collect(Collectors.partitioningBy(u -> u.age() >= 18));
     *  T2b: partitioned.get(true).stream().map(User::name).collect(Collectors.joining(", "));
     *  T3: users.stream().filter(u -> u.name().equals("Bo")).findFirst().map(User::age).orElse(-1);
     */
}
