/*
 * @lc app=leetcode id=110 lang=java
 *
 * [110] Balanced Binary Tree
 */

// @lc code=start
/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public boolean isBalanced(TreeNode root) {
        if (root ==null) return true;
        int lD = depth(root.left);
        int rD = depth(root.right);

        return Math.abs(lD - rD) <= 1
                && isBalanced(root.left)
                && isBalanced(root.right);
    }

    private int depth(TreeNode node){
        if(node == null ) return 0;
        int left = depth(node.left);
        int right = depth(node.right);

        return 1+Math.max(left,right);
    }
}
// @lc code=end

