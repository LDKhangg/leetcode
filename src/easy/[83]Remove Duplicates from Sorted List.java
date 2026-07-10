/*
 * @lc app=leetcode id=83 lang=java
 *
 * [83] Remove Duplicates from Sorted List
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
    public ListNode deleteDuplicates(ListNode head) {
        ListNode dummy= new ListNode(0);
        dummy.next = head;
        ListNode prev=dummy;
        ListNode curr =head;
        while(curr!=null){
            if(curr.next!=null && curr.next.val==curr.val){
                int val =curr.val;
                while(curr.next!=null&&curr.next.val==val) curr=curr.next;
                prev.next=curr;
            }else{
                prev=curr;
                curr=curr.next;
            }
        }
        return dummy.next;
    }
}
// @lc code=end

