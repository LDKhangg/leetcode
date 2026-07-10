/*
 * @lc app=leetcode id=82 lang=java
 *
 * [82] Remove Duplicates from Sorted List II
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
        ListNode dummy = new ListNode(0);
        dummy.next=head;

        ListNode curr = head;
        ListNode prev = dummy;
        while(curr!=null){
            if(curr.next!=null && curr.val==curr.next.val){
                int val = curr.val;
                while(curr!=null && curr.val==val){
                    curr=curr.next;
                }
                prev.next=curr;
            }
            else{
                prev=curr;
                curr=curr.next;
            }

        }
        return dummy.next;
    }
}
// @lc code=end

