# Sliding Window

Variable-size window template for "longest/shortest substring with constraint" problems (LeetCode 3, 424, 567, 76).

## Template

```java
int left = 0;
for (int right = 0; right < n; right++) {
    // expand: include s[right]
    while (/* constraint violated */) {
        // shrink: exclude s[left++]
    }
    // update answer on the valid window [left, right]
}
```

## Checklist

- Expand `right` every step; move `left` only to restore validity → O(n).
- Frequency map + `formed` counter beats re-scanning the window.
- 76 Minimum Window adds "want vs have" counts; 239 needs a deque, not a plain window.

## Links

- Code drills: `src/medium/[3]...`, `src/medium/[424]...` (TODO paths)
- Tracker: [NEETCODE150](../NEETCODE150.md)
