/*
 * @lc app=leetcode id=424 lang=java
 *
 * [424] Longest Repeating Character Replacement
 */

// @lc code=start
class Solution {
    public int characterReplacement(String s, int k) {
        byte[] cArr = s.getBytes();
        int[] count = new int[26];

        int left = 0, maxFreq = 0;
        for (int right = 0; right < cArr.length; right++) {
            int currIdx = cArr[right] - 'A';
            count[currIdx]++;
            if (maxFreq < count[currIdx]) {
                maxFreq = count[currIdx];
            }
            if (right - left + 1 - k > maxFreq) {
                count[cArr[left] - 'A'] -= 1;
                left++;
            }

        }
        return cArr.length - left;
    }
}
// @lc code=end
