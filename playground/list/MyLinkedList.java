/**
 * Singly linked list from scratch (int nodes).
 * Run: javac MyLinkedList.java && java -cp . MyLinkedList
 */
public class MyLinkedList {
    private static class Node {
        int val;
        Node next;

        Node(int v) {
            val = v;
        }
    }

    private Node head;
    private int size;

    public void addFirst(int v) {
        Node n = new Node(v);
        n.next = head;
        head = n;
        size++;
    }

    public void addLast(int v) {
        Node n = new Node(v);
        if (head == null) {
            head = n;
        } else {
            Node cur = head;
            while (cur.next != null) {
                cur = cur.next;
            }
            cur.next = n;
        }
        size++;
    }

    public int removeFirst() {
        if (head == null) {
            throw new IllegalStateException("empty");
        }
        int out = head.val;
        head = head.next;
        size--;
        return out;
    }

    /** In-place iterative reversal — the LeetCode 206 pattern. */
    public void reverse() {
        Node prev = null, cur = head;
        while (cur != null) {
            Node nxt = cur.next;
            cur.next = prev;
            prev = cur;
            cur = nxt;
        }
        head = prev;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (Node cur = head; cur != null; cur = cur.next) {
            sb.append(cur.val);
            if (cur.next != null) {
                sb.append(" -> ");
            }
        }
        return sb.append("]").toString();
    }

    // TODO: removeLast(), get(i), cycle detection (fast/slow — LeetCode 141), middle node
    public static void main(String[] args) {
        MyLinkedList l = new MyLinkedList();
        l.addLast(1);
        l.addLast(2);
        l.addLast(3);
        l.addFirst(0);
        System.out.println("list: " + l + " size=" + l.size());
        l.reverse();
        System.out.println("reversed: " + l);
        System.out.println("removeFirst=" + l.removeFirst() + " -> " + l);
    }
}
