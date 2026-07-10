/*
 * @lc app=leetcode id=160 lang=java
 *
 * [160] Intersection of Two Linked Lists
 */

// @lc code=start
/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode(int x) {
 *         val = x;
 *         next = null;
 *     }
 * }
 */
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
      if (headA == null || headB == null) return null;

      ListNode nA=headA,nB=headB;
      while(nA!=nB){
        nA=(nA==null)?headB:nA.next;
        nB=(nB==null)?headA:nB.next;
      }
      return nA;
    }
}
// @lc code=end

