/*
 * @lc app=leetcode id=61 lang=java
 *
 * [61] Rotate List
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
    public ListNode rotateRight(ListNode head, int k) {
        if(head==null || head.next==null||k==0) return head;

        int n=1;
        ListNode tail = head;
        while(tail.next!=null){
            tail=tail.next;
            n++;
        }

        k%=n;

        if(k==0) return head;

        tail.next=head;

        ListNode newTail = head;

        for(int i =0;i<n-k-1;i++){
            newTail=newTail.next;
        }

        ListNode res = newTail.next;
        newTail.next = null;
        return res;
    }
}
// @lc code=end

