/*
 * @lc app=leetcode id=101 lang=java
 *
 * [101] Symmetric Tree
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
    public boolean isSymmetric(TreeNode root) {
        return isSameNode(root.left,root.right);
    }

    public boolean isSameNode(TreeNode left,TreeNode right){
        if(left==null & right == null) return true;
        if(left==null || right == null ) return false;
        if(left.val != right.val) return false;

        return isSameNode(left.left,right.right)&& isSameNode(left.right,right.left);
    }
}
// @lc code=end

