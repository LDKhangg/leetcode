/*
 * @lc app=leetcode id=496 lang=java
 *
 * [496] Next Greater Element I
 */

// @lc code=start
class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        int n1=nums1.length;
        int n2=nums2.length;
        int[] ans = new int[n1];
        Stack<Integer> stk = new Stack<>();
        Map<Integer,Integer> map=new HashMap<>();
        for(int num :nums2){
            while(!stk.isEmpty()&&stk.peek()<num){
                map.put(stk.pop(),num);
            }
            stk.push(num);
        }
        while (!stk.isEmpty()) {
            map.put(stk.pop(), -1);
        }
        for (int i = 0; i < n1; i++) {
            ans[i] = map.get(nums1[i]);
        }
        
        return ans;
    }
}
// @lc code=end

