# Map playground

```bash
javac playground/map/MyHashMap.java && java -cp playground/map MyHashMap
```

## Checklist

- [x] `MyHashMap` — chaining, overwrite on duplicate key, resize at load factor 0.75
- [ ] Open addressing (linear probing) variant for comparison
- [ ] Null-key handling like `java.util.HashMap`
- [ ] Tiny ordered-map (insertion order via extra linked list)

## Why this matters

Hashing underpins NeetCode "Arrays & Hashing" (242, 49, 347, 1). Knowing resize/treeify behavior explains real-world `HashMap` performance cliffs.
