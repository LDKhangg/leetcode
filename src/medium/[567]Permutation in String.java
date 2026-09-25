/*
 * @lc app=leetcode id=567 lang=java
 *
 * [567] Permutation in String
 */

// @lc code=start

import java.lang.reflect.Array;
import java.util.Arrays;

class Solution {
    public boolean checkInclusion(String s1, String s2) {
        int n1 = s1.length();
        int n2 = s2.length();

        if (n1 > n2)
            return false;

        // Cách 1
        // byte[] count1 = new byte[26];
        // byte[] count2 = new byte[26];

        // for (int i = 0; i < n1; i++) {
        // count1[s1.charAt(i) - 'a']++;
        // count2[s2.charAt(i) - 'a']++;
        // }

        // for (int i = 0; i < n2 - n1; i++) {

        // if (Arrays.equals(count1, count2))
        // return true;

        // count2[s2.charAt(i) - 'a']--;
        // count2[s2.charAt(i + n1) - 'a']++;
        // }
        // return Arrays.equals(count1, count2);

        // Cách 2
        int[] count = new int[26];

        for (int i = 0; i < n1; i++) {
            count[s1.charAt(i) - 'a']++;
        }

        char[] s2CharArr = s2.toCharArray();
        int left = 0, right = 0, required = n1;
        while (right < n2) {
            if (count[s2CharArr[right] - 'a'] > 0) {
                required--;
            }
            count[s2CharArr[right] - 'a']--;
            right++;

            if (required == 0)
                return true;

            if (right - left == n1) {
                if (count[s2CharArr[left] - 'a'] >= 0) {
                    required++;
                }
                count[s2CharArr[left] - 'a']++;
                left++;
            }
        }
        return false;
    }

}
// @lc code=end
