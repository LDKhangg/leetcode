/*
 * @lc app=leetcode id=25 lang=java
 *
 * [25] Reverse Nodes in k-Group
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
    public ListNode reverseKGroup(ListNode head, int k) {

         //change val;
         /**
        * int count = 0;
        *ListNode temp = head;
        *
        *while (temp != null) {
        *    count++;
        *    temp = temp.next;
        *}
        *
        *int groups = count / k;
        *
        *ListNode curr = head;
        *ListNode write = head;
        *
        *while (groups > 0) {
        *    int[] arr = new int[k];
        *
        *    for (int i = 0; i < k; i++) {
        *        arr[i] = curr.val;
        *        curr = curr.next;
        *    }
        *
        *    for (int i = k - 1; i >= 0; i--) {
        *        write.val = arr[i];
        *        write = write.next;
        *    }
        *
        *    groups--;
        *}

        return head;
        */

        //change node;
        ListNode dummy = new ListNode(0);
        dummy.next=head;
        ListNode groupPrev= dummy;
        while(true){
            ListNode Kth=getKth(groupPrev,k);

            if(Kth==null) break;

            ListNode groupNext= Kth.next;

            ListNode prev = groupNext;
            ListNode curr = groupPrev.next;

            while(curr!=groupNext){
                ListNode next = curr.next;
                curr.next=prev;
                prev=curr;
                curr=next;
            }

            ListNode oldGroupStart = groupPrev.next;
            groupPrev.next=Kth;
            groupPrev=oldGroupStart;
        }
        return dummy.next;
    }
     private ListNode getKth(ListNode curr, int k) {
        while (curr != null && k > 0) {
            curr = curr.next;
            k--;
        }
        return curr;
    }
}
// @lc code=end

