/*
 * @lc app=leetcode id=563 lang=java
 *
 * [563] Binary Tree Tilt
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

    int res = 0;

    public int findTilt(TreeNode root) {
        calculateSum(root);
        return res;
    }

    private int calculateSum(TreeNode node){
        if(node ==null) return 0;

        int leftSum=calculateSum(node.left);
        int rightSum=calculateSum(node.right);

        int currentTilt = Math.abs(leftSum-rightSum);

        res+=currentTilt;

        return node.val+leftSum+rightSum;
    }
}
// @lc code=end

