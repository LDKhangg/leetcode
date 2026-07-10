/*
 * @lc app=leetcode id=705 lang=java
 *
 * [705] Design HashSet
 */

// @lc code=start
class MyHashSet {

    private static class Node{
        int key;
        Node next;
        Node(int key,Node next){
            this.key=key;
            this.next=next;
        }
    }
    private static final int B = 769;
    private Node[] buckets=new Node[B];
    private int hash(int key) {return key%B;}

    public MyHashSet() {
        
    }
    
    public void add(int key) {
        if(contains(key)) return;
        int i = hash(key);
        buckets[i]=new Node(key,buckets[i]);
    }
    
    public void remove(int key) {
        int i = hash(key);
        Node dummy = new Node(0,buckets[i]);
        Node curr = dummy;
        while(curr.next!=null){
            if(curr.next.key==key){
                curr.next=curr.next.next;
                break;
            }
            curr=curr.next;
        }
        buckets[i]=dummy.next;
    }
    
    public boolean contains(int key) {
        for(Node curr = buckets[hash(key)]; curr!=null;curr=curr.next){
            if(curr.key==key) return true;
        }
        return false;
    }
}

/**
 * Your MyHashSet object will be instantiated and called as such:
 * MyHashSet obj = new MyHashSet();
 * obj.add(key);
 * obj.remove(key);
 * boolean param_3 = obj.contains(key);
 */
// @lc code=end

