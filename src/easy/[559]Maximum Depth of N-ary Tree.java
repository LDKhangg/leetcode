/*
 * @lc app=leetcode id=559 lang=java
 *
 * [559] Maximum Depth of N-ary Tree
 */

// @lc code=start
/*
// Definition for a Node.
class Node {
    public int val;
    public List<Node> children;

    public Node() {}

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, List<Node> _children) {
        val = _val;
        children = _children;
    }
};
*/

class Solution {
    public int maxDepth(Node root) {
        if(Node == null) return 0;

        int maxChildDepth =0;

        for(Node child:root.children){
            maxChildDepth=Math.max(maxChildDepth, maxChildDepth(child));
        }

        return 1+ maxChildDepth;
    }
}
// @lc code=end

