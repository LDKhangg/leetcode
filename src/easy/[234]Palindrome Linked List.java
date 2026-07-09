/*
 * @lc app=leetcode id=234 lang=java
 *
 * [234] Palindrome Linked List
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
    public boolean isPalindrome(ListNode head) {
        Stack<Integer> stk =new Stack<>();

        ListNode dummy=head;
        ListNode slow =head;
        ListNode fast =head;
         while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode temp = slow;
        while(slow!=null){
            stk.push(slow.val);
            slow=slow.next;
        }while (slow != null) {
            stk.push(slow.val);
            slow = slow.next;
        }

        while (!stk.isEmpty()) {
            if (stk.pop() != dummy.val) {
                return false;
            }
            dummy = dummy.next;
        }

        return true;
    }
}
// @lc code=end

