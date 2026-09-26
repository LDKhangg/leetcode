# Generics playground

```bash
javac playground/generics/L1_Box.java && java -cp playground/generics L1_Box
javac playground/generics/L2_Wildcard.java && java -cp playground/generics L2_Wildcard
javac playground/generics/L3_Erasure.java && java -cp playground/generics L3_Erasure
```

## Lộ trình level-up

| Level | Bài | Câu hỏi phỏng vấn |
|-------|-----|-------------------|
| L1 | `L1_Box` | generic class, diamond, generic method, bounded `<T extends Number>` |
| L2 | `L2_Wildcard` | PECS, `? extends` vs `? super`, copy(src, dest) |
| L3 | `L3_Erasure` | erasure, heap pollution, bridge method, `new T[]` |

## Checklist

- [x] L1 — Box\<T\> + generic method + bounded type
- [x] L2 — wildcard PECS + copy producer->consumer
- [x] L3 — erasure + heap pollution + bridge method
- [ ] Tự viết thêm: generic stack `MyStack<T>` (push/pop/peek + tự mở rộng mảng)
- [ ] Tự viết thêm: `maxBy(List<T>, Comparator<? super T>)` kiểu PECS 2 chiều
