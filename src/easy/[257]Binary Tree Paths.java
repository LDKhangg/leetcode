/*
 * @lc app=leetcode id=257 lang=java
 *
 * [257] Binary Tree Paths
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

    private List<String> res = new ArrayList();

    public List<String> binaryTreePaths(TreeNode root) {
        dfs(root,"");
        return res;
    }

    private void dfs(TreeNode node,String path){

        if(node==null) return;

        if(path.isEmpty()) path=String.valueOf(node.val);

        else path= path + "->" + String.valueOf(node.val);

        if(node.left==null && node.right==null) {
            res.add(path);
            return;
        }

        dfs(node.left,path);
        dfs(node.right,path);
    } 
}
// @lc code=end

