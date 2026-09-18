# Concurrency playground

```bash
javac playground/concurrency/CounterRace.java && java -cp playground/concurrency CounterRace
javac playground/concurrency/ProducerConsumer.java && java -cp playground/concurrency ProducerConsumer
```

## Checklist

- [x] `CounterRace` — lost updates vs `synchronized` fix
- [x] `ProducerConsumer` — bounded `BlockingQueue` with poison-pill shutdown
- [ ] `AtomicInteger` / `volatile` visibility demo
- [ ] Hand-rolled `wait()`/`notify()` queue (then compare with `BlockingQueue`)
- [ ] `ExecutorService` + `CompletableFuture` fan-out example

## Notes

Key interview talking points live in `../../notes/concurrency-basics.md` (TODO):Data race vs race condition, monitor locks, backpressure.
