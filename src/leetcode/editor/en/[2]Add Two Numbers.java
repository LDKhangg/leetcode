//You are given two non-empty linked lists representing two non-negative 
//integers. The digits are stored in reverse order, and each of their nodes contains a 
//single digit. Add the two numbers and return the sum as a linked list. 
//
// You may assume the two numbers do not contain any leading zero, except the 
//number 0 itself. 
//
// 
// Example 1: 
// 
// 
//Input: l1 = [2,4,3], l2 = [5,6,4]
//Output: [7,0,8]
//Explanation: 342 + 465 = 807.
// 
//
// Example 2: 
//
// 
//Input: l1 = [0], l2 = [0]
//Output: [0]
// 
//
// Example 3: 
//
// 
//Input: l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
//Output: [8,9,9,9,0,0,0,1]
// 
//
// 
// Constraints: 
//
// 
// The number of nodes in each linked list is in the range [1, 100]. 
// 0 <= Node.val <= 9 
// It is guaranteed that the list represents a number that does not have 
//leading zeros. 
// 
//
// Related Topics Linked List Math Recursion 👍 36952 👎 7271


//leetcode submit region begin(Prohibit modification and deletion)
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
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        return recursive(l1,l2,0);
    }
    private ListNode recursive(ListNode l1,ListNode l2,int target){
        if(l1==null && l2 == null && target ==0){
            return null;
        }
        int val1 = l1!=null? l1.val:0;
        int val2 = l2 !=null? l2.val:0;

        int sum = val1+val2+target;
        target=sum/10;

        ListNode node = new ListNode(sum%10);

        ListNode nextl1= (l1!=null) ? l1.next : null;
        ListNode nextl2= (l2!=null)? l2.next : null;

        node.next=recursive(nextl1,nextl2,target);

        return node;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
