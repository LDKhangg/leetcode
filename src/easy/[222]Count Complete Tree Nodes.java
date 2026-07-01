/*
 * @lc app=leetcode id=222 lang=java
 *
 * [222] Count Complete Tree Nodes
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
    public int countNodes(TreeNode root) {

        if(root==null) return 0;

        int leftH = countLeft(root);
        int rightH = countRight(root);

        if(leftH==rightH){
            return (1<<leftH)-1;
        }

        return 1+countNodes(root.left)+countNodes(root.right);
    }

    private int countLeft(TreeNode root){
        int h = 0;
        while(root!=null) {
            h++;
            root=root.left;
        }
        return h;
    }

    private int countRight(TreeNode root){
        int h = 0;
        while(root!=null) {
            h++;
            root=root.right;
        }
        return h;
    }
}
// @lc code=end

