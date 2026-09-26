# Java core interview playground

Phong cách FAIL-first (giống `playground/lambda/L1_Functional.java`):
mỗi file có Bài 0 chạy luôn + các TODO để **sai cố ý** -> chạy ra FAIL kèm hint.
Sửa từng TODO tới khi tất cả PASS (`System.exit(1)` nếu còn FAIL).

```bash
javac playground/java-core/L1_OopRecord.java && java -cp playground/java-core L1_OopRecord
javac playground/java-core/L2_EqualsHash.java && java -cp playground/java-core L2_EqualsHash
javac playground/java-core/L3_Exception.java && java -cp playground/java-core L3_Exception
```

## Lộ trình level-up

| Level | Bài | Câu hỏi phỏng vấn | TODO |
|-------|-----|-------------------|------|
| L1 | `L1_OopRecord` | record vs class, sealed, pattern matching | T1 nhánh Circle (thiếu PI), T2 nhánh Rect (`+` vs `*`), T3 `describe()` instanceof pattern, T4 chu vi, T5 fail-fast `mustBePositive` |
| L2 | `L2_EqualsHash` | equals/hashCode contract, mutable key, fail-fast | T1 `hashCode` cho `NewKey`, T2 đoán số phận key mutable, T3 `==` vs `equals`, T4 `Comparator`, T5 thí nghiệm fail-fast |
| L3 | `L3_Exception` | checked/unchecked, try-with-resources, heap/GC/Xmx | T1 `deleteIfExists` trong `finally`, T2 bắt riêng `NoSuchFileException`, T3 `safeSqrt` fail-fast, T4 fallback có phân loại, T5 thí nghiệm `-Xmx` |

## Checklist

- [x] L1 — OOP record + sealed + switch pattern (FAIL-first, 5 TODO)
- [x] L2 — equals/hashCode bẫy + mutable key (FAIL-first, 5 TODO)
- [x] L3 — exception + try-with-resources + JVM notes (FAIL-first, 5 TODO)
- [ ] Tự viết thêm: Comparable vs Comparator (sort User theo age rồi name)
- [ ] Tự viết thêm: generic bounded (Box<T extends Number>)
