/*
 * @lc app=leetcode id=86 lang=java
 *
 * [86] Partition List
 */

// @lc code=start
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {
    public ListNode partition(ListNode head, int x) {
        ListNode lessX= new ListNode(0),greatX= new ListNode(0);
        ListNode lessTail = lessX,greateTail=greatX;
        ListNode curr=head;
        while(curr!=null){
            if(curr.val<x){
                lessTail.next=new ListNode(curr.val);
                lessTail=lessTail.next;
            } else{
                greateTail.next=new ListNode(curr.val);
                greateTail=greateTail.next;
            }
            curr=curr.next;
        }
        lessTail.next=greatX.next;
        return lessX.next;
    }
}
// @lc code=end

