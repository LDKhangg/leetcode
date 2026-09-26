# Strings playground

```bash
javac playground/strings/L1_Equality.java && java -cp playground/strings L1_Equality
javac playground/strings/L2_Builder.java && java -cp playground/strings L2_Builder
javac playground/strings/L3_Intern.java && java -cp playground/strings L3_Intern
```

## Lộ trình level-up

| Level | Bài | Câu hỏi phỏng vấn |
|-------|-----|-------------------|
| L1 | `L1_Equality` | `==` vs equals, pool, null-safe, isBlank |
| L2 | `L2_Builder` | `+=` O(n²) vs builder O(n), reverse/mask/repeat |
| L3 | `L3_Intern` | intern, switch String, text block, String.join |

## Checklist

- [x] L1 — equality + pool + null-safe equals
- [x] L2 — StringBuilder + đo nanoTime N=20000
- [x] L3 — intern + switch + text block + join
- [ ] Tự viết thêm: đếm từ trong câu (split + strip + filter rỗng)
- [ ] Tự viết thêm: slugify (lowercase + replace spaces + bỏ dấu câu)
