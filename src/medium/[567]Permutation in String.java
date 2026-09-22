/*
 * @lc app=leetcode id=567 lang=java
 *
 * [567] Permutation in String
 */

// @lc code=start
class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int[] count = new int[26];
        char[] c1Arr = s1.toCharArray();
        char[] c2Arr = s2.toCharArray();
        for (int i = 0; i < c1Arr.length; i++) {
            count[c1Arr[i] - 'a']++;
        }
        for (int i = 0; i < c2Arr.length; i++) {
            count[c2Arr[i] - 'a']++;
        }
        for (int i = 0; i < 26; i++) {
            if (count[i] == 1)
                return false;
        }
        return true;

    }
}
// @lc code=end
