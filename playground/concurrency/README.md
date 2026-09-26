# Concurrency playground

```bash
javac playground/concurrency/L1_RaceCounter.java && java -cp playground/concurrency L1_RaceCounter
javac playground/concurrency/L2_ProducerConsumer.java && java -cp playground/concurrency L2_ProducerConsumer
javac playground/concurrency/L3_ExecutorFuture.java && java -cp playground/concurrency L3_ExecutorFuture
```

## Track chính (bài tập FAIL-first, làm theo thứ tự)

| Level | Bài | Câu hỏi phỏng vấn |
|-------|-----|-------------------|
| L1 | `L1_RaceCounter` | data race, synchronized, AtomicInteger, volatile vs atomicity |
| L2 | `L2_ProducerConsumer` | BlockingQueue, backpressure, poison pill |
| L3 | `L3_ExecutorFuture` | pool, Future vs CompletableFuture, exceptionally, allOf, shutdown |

Mỗi file có Bài 0 mẫu + TODO để SAI cố ý -> chạy thấy FAIL, sửa tới khi PASS.
Đáp án giấu cuối file. `exit(1)` khi còn FAIL.

## Đọc thêm (demo cũ, tham khảo logic — không cần sửa)

```bash
javac playground/concurrency/CounterRace.java && java -cp playground/concurrency CounterRace
javac playground/concurrency/ProducerConsumer.java && java -cp playground/concurrency ProducerConsumer
javac playground/concurrency/VisibilityDemo.java && java -cp playground/concurrency VisibilityDemo
javac playground/concurrency/WaitNotifyQueue.java && java -cp playground/concurrency WaitNotifyQueue
javac playground/concurrency/ExecutorFuture.java && java -cp playground/concurrency ExecutorFuture
```

## Lộ trình level-up

| Level | Bài | Câu hỏi phỏng vấn |
|-------|-----|-------------------|
| L0 | `CounterRace` | data race, synchronized |
| L0 | `ProducerConsumer` | BlockingQueue, backpressure, poison pill |
| L1 | `VisibilityDemo` | volatile, JMM happens-before, AtomicBoolean |
| L2 | `WaitNotifyQueue` | wait/notify, spurious wakeup, notifyAll |
| L3 | `ExecutorFuture` | pool, Future vs CompletableFuture, virtual threads |

## Checklist

- [x] `CounterRace` — lost updates vs `synchronized` fix
- [x] `ProducerConsumer` — bounded `BlockingQueue` with poison-pill shutdown
- [x] `VisibilityDemo` — L1 `volatile` / `AtomicBoolean` visibility demo
- [x] `WaitNotifyQueue` — L2 hand-rolled `wait()`/`notify()` queue (so sánh với `BlockingQueue`)
- [x] `ExecutorFuture` — L3 `ExecutorService` + `CompletableFuture` fan-out

## Notes

Key interview talking points live in `../../notes/concurrency-basics.md` (TODO):Data race vs race condition, monitor locks, backpressure.

## Thread in java

### java.lang.Thread:
Thread in java is just a Java's object wrap around OS'thread.
- create thread in java is create OS thread, it has stack(512Kb-1MB base on platform,can configurate -Xss) and has real expense (sysccall, allocate kernel memory).
- Thread scheduling is entirely determined by the OS,not JVM. JVM can't controll when one thread has CPU,how long, move to what thread. So **Thread.setPriority()** just suggest OS schedule. 
- Cause of use same address space of process,every thread in JVM will watch 1 heap.

### Memory in Thread:

| Memory area           | Shared between every thread |                                               what has |
| :-------------------- | :-------------------------: | -----------------------------------------------------: |
| Heap                  |          together           |                    every Object,array,field of instane |
| Method Area/Metaspace |          together           |               bytecode of class,static field,constants |
| Stack                 |         indivitual          | stack frame: local variable, parameters,return address |
| PC Register           |         indivitual          |                          pointer to executing bytecode |
| Native method stack   |         indivitual          |                       Frame when call code native(JNI) |

### Thread's lifecycle in JVM layer, what diff OS
OS just have 4 state : **READY,RUNNING, BLOCKED, TERMINATED**.
Java define 6 state:
```
NEW → RUNNABLE ⇄ BLOCKED / WAITING / TIMED_WAITING → TERMINATED
```
- **NEW:** created Thread object,not call start() yet.in this step does not have any OS thread, just common Java object in heap.
- **RUNNABLE**: **start()** was executed. gather **READY** and **RUNNING**.
- **BLOCKED**: try to get 1 **monitor lock(synchronized)** that lock was hold by other thread. This is separate state of `synchronized`; `ReentrantLock` not use this state.
- **WAITING**: proactively call `Object.wait()` (not timeout),`Thread.join()`(no timeout), or `LockSupport.park()` . Thread voluntarily give in CPU, waiting for something wake up.
- **TIMED_WAITING:** same **WAITING** but has limit: `sleep(ms)`, `wait(ms)`, `join(ms)`, `LockeSupport.parkNanos()`.
- **TERMINATED:** `run()` execute finish or throw exception cant catch.



