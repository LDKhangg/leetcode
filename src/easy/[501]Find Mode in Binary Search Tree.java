/*
 * @lc app=leetcode id=501 lang=java
 *
 * [501] Find Mode in Binary Search Tree
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
    private Integer prev;
    private int count;
    private int maxCount;
    private List<Integer> result =new ArrayList<>();

    public int[] findMode(TreeNode root) {
        inorder(root);
        int[] answer = new int[result.size()];

        for(int i = 0 ; i < result.size();i++){
            answer[i]=result.get(i);
        }
        return answer;
    }

    private void inorder(TreeNode node){
        if(node == null) return;

        inorder(node.left);

        if(prev!=null && prev==node.val) count++;
        else count = 1;

        if(count>maxCount){
            maxCount=count;
            result.clear();
            result.add(node.val);
        }

        else if(count==maxCount){
            result.add(node.val);
        }

        prev = node.val;

        inorder(node.right);
    }
}
// @lc code=end

