# List playground

Re-implement the list fundamentals here. Each file is standalone with a `main()` demo.

```bash
javac playground/list/MyArrayList.java && java -cp playground/list MyArrayList
javac playground/list/MyLinkedList.java && java -cp playground/list MyLinkedList
```

## Checklist

- [x] `MyArrayList` — dynamic array, amortized O(1) append via doubling
- [x] `MyLinkedList` — singly linked, iterative reverse (LeetCode 206 pattern)
- [ ] Generics: `MyArrayList<E>` backed by `Object[]`
- [ ] Doubly linked list with head/tail pointers (O(1) ends)
- [ ] Fast/slow pointers: middle node, cycle detection (LeetCode 141/876)

## Notes

- See `../../notes/` for write-ups. Related LeetCode: 206, 21, 141, 143.
