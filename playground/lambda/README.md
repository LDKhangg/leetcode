# Lambda & Stream playground

```bash
javac playground/lambda/L1_Functional.java && java -cp playground/lambda L1_Functional
javac playground/lambda/L2_Streams.java && java -cp playground/lambda L2_Streams
javac playground/lambda/L3_Collectors.java && java -cp playground/lambda L3_Collectors
```

## Lộ trình level-up

| Level | Bài | Câu hỏi phỏng vấn |
|-------|-----|-------------------|
| L1 | `L1_Functional` | SAM, effectively-final, method ref, Comparator |
| L2 | `L2_Streams` | intermediate vs terminal, lazy, stream 1 lần |
| L3 | `L3_Collectors` | groupingBy/partitioning, Optional, parallel |

## Checklist

- [x] L1 — lambda + functional interface + method ref
- [x] L2 — stream map/filter/reduce + lazy demo
- [x] L3 — collectors + Optional + parallel
- [ ] Tự viết thêm: custom collector (toMap với merge function)
- [ ] Tự viết thêm: flatMap (List<Order> -> List<Item>)
