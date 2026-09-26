# Testing playground (không cần JUnit — chỉ javac/java)

Phong cách FAIL-first: mỗi file có Bài 0 chạy luôn + các TODO để **sai cố ý** —
chạy lần đầu sẽ thấy FAIL kèm hint, sửa tới khi tất cả PASS.

```bash
javac playground/testing/L1_Assert.java && java -ea -cp playground/testing L1_Assert
javac playground/testing/L2_MiniTest.java && java -cp playground/testing L2_MiniTest
javac playground/testing/L3_MockTest.java && java -cp playground/testing L3_MockTest
```

> L1 nhớ thêm `-ea` để bật `assert`. L2/L3 dùng assert tay nên không cần.

## Lộ trình level-up

| Level | Bài | Chủ đề | Câu hỏi phỏng vấn |
|-------|-----|--------|-------------------|
| L1 | `L1_Assert` | average + assert/-ea, AAA, edge cases | AAA là gì? Quên -ea thì sao? Tràn int? |
| L2 | `L2_MiniTest` | MyStack mini-framework: pop rỗng ném lỗi, isEmpty lifecycle, isolation | setup/teardown? Test flaky do đâu? |
| L3 | `L3_MockTest` | FakeMailer DI + HashMap overwrite/null-key | mock vs stub vs fake? Vì sao DI giúp test dễ? |

Mỗi file gồm 4 TODO sai cố ý + Bài 0 mẫu + đáp án giấu cuối file + 3 câu tự tư duy.

## Checklist

- [x] L1 — assert thủ công + checkThrows (4 TODO: single, số âm, rỗng ném lỗi, edge + -ea)
- [x] L2 — mini-framework test() + eq() (4 TODO: LIFO, pop rỗng, isEmpty, isolation)
- [x] L3 — fake Mailer + test collection boundary (4 TODO: trùng mail, overwrite, null key, find vắng)
- [ ] Nâng cao: cài JUnit5 rồi viết lại 3 bài bằng @Test/assertEquals
- [ ] Nâng cao: test concurrency (CountDownLatch + assert eventual)
