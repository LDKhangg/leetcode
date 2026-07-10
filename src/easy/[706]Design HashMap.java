/*
 * @lc app=leetcode id=706 lang=java
 *
 * [706] Design HashMap
 */

// @lc code=start
class MyHashMap {

    private static class Node{
        int key;
        Node next;
        int value;
        Node(int key,int value,Node next){
            this.key=key;
            this.next=next;
            this.value = value;
        }
    }
    private static final int B = 769;
    private Node[] buckets=new Node[B];
    private int hash(int key) {return key%B;}


    public MyHashMap() {
        
    }
    
    public void put(int key, int value) {
        int i = hash(key);
        for(Node curr=buckets[i];curr!=null;curr=curr.next){
            if(curr.key==key){
                curr.value=value;
                return;
            }
        }
        buckets[i]= new Node(key,value,buckets[i]);
    }
    
    public int get(int key) {
        int i = hash(key);
        for(Node curr=buckets[i];curr!=null;curr=curr.next){
            if(curr.key==key){
                return curr.value;
            }
        }
        return -1;
    }
    
    public void remove(int key) {
        int i = hash(key);
        Node dummy = new Node(0,0,buckets[i]);
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
}

/**
 * Your MyHashMap object will be instantiated and called as such:
 * MyHashMap obj = new MyHashMap();
 * obj.put(key,value);
 * int param_2 = obj.get(key);
 * obj.remove(key);
 */
// @lc code=end

