import java.util.LinkedList;

/**
 * HashMap from scratch: separate chaining + resize at load factor 0.75.
 * Run: javac MyHashMap.java && java -cp . MyHashMap
 */
public class MyHashMap {
    private static class Entry {
        final String key;
        int value;

        Entry(String k, int v) {
            key = k;
            value = v;
        }
    }

    private LinkedList<Entry>[] table;
    private int size;
    private static final double LOAD = 0.75;

    @SuppressWarnings("unchecked")
    public MyHashMap(int capacity) {
        table = new LinkedList[Math.max(2, capacity)];
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public MyHashMap() {
        this(16);
    }

    private int bucket(String key) {
        return (key.hashCode() & 0x7fffffff) % table.length;
    }

    public void put(String key, int value) {
        LinkedList<Entry> b = table[bucket(key)];
        for (Entry e : b) {
            if (e.key.equals(key)) {
                e.value = value;
                return;
            }
        }
        b.add(new Entry(key, value));
        if (++size > table.length * LOAD) {
            resize();
        }
    }

    public Integer get(String key) {
        for (Entry e : table[bucket(key)]) {
            if (e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    public boolean remove(String key) {
        LinkedList<Entry> b = table[bucket(key)];
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).key.equals(key)) {
                b.remove(i);
                size--;
                return true;
            }
        }
        return false;
    }

    public int size() {
        return size;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        LinkedList<Entry>[] old = table;
        table = new LinkedList[old.length * 2];
        for (int i = 0; i < table.length; i++) {
            table[i] = new LinkedList<>();
        }
        size = 0;
        for (LinkedList<Entry> b : old) {
            for (Entry e : b) {
                put(e.key, e.value);
            }
        }
    }

    // TODO: null-key support, treeify long chains (JDK 8 idea), iteration order
    public static void main(String[] args) {
        MyHashMap m = new MyHashMap(2); // tiny to force resize
        m.put("two-sum", 1);
        m.put("anagram", 242);
        m.put("duplicate", 217);
        m.put("anagram", 49); // overwrite
        System.out.println("get(anagram)=" + m.get("anagram"));
        System.out.println("get(missing)=" + m.get("missing"));
        System.out.println("remove(duplicate)=" + m.remove("duplicate") + " size=" + m.size());
        System.out.println("capacity=" + m.table.length);
    }
}
