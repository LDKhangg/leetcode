import java.util.Arrays;

/**
 * Minimal dynamic array (ArrayList from scratch).
 * Run: javac MyArrayList.java && java -cp . MyArrayList
 */
public class MyArrayList {
    private int[] data;
    private int size;

    public MyArrayList() {
        this(8);
    }

    public MyArrayList(int capacity) {
        data = new int[Math.max(1, capacity)];
    }

    public void add(int v) {
        if (size == data.length) {
            data = Arrays.copyOf(data, data.length * 2);
        }
        data[size++] = v;
    }

    public int get(int i) {
        check(i);
        return data[i];
    }

    public int removeAt(int i) {
        check(i);
        int out = data[i];
        System.arraycopy(data, i + 1, data, i, size - i - 1);
        size--;
        return out;
    }

    public int size() {
        return size;
    }

    private void check(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("i=" + i + ", size=" + size);
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(Arrays.copyOf(data, size));
    }

    // TODO: add(E e), set(i, v), trimToSize(), iterator, generics (MyArrayList<E> with Object[])
    public static void main(String[] args) {
        MyArrayList a = new MyArrayList(2); // tiny capacity to force a resize
        for (int v : new int[]{3, 1, 4, 1, 5}) {
            a.add(v);
        }
        System.out.println("after add: " + a + " size=" + a.size());
        System.out.println("get(2)=" + a.get(2));
        System.out.println("removeAt(1)=" + a.removeAt(1) + " -> " + a);
    }
}
