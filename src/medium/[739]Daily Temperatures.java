/*
 * @lc app=leetcode id=739 lang=java
 *
 * [739] Daily Temperatures
 */

// @lc code=start

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int n = temperatures.length;
        int[] ans = new int[n];
        int[] stack = new int[n];
        int head = -1;
        for (int i = 0; i < n; i++) {
            while (head >= 0 && temperatures[i] > temperatures[stack[head]]) {
                ans[stack[head]] = i - stack[head];
                head--;
            }
            head++;
            stack[head] = i;
        }

        return ans;

    }
}
// @lc code=end
