/*
 * @lc app=leetcode id=875 lang=java
 *
 * [875] Koko Eating Bananas
 */

// @lc code=start
class Solution {
    public int minEatingSpeed(int[] piles, int h) {
        int n = piles.length;
        if (n == 0)
            return 0;
        int maxPile = 0;
        for (int pile : piles) {
            if (pile > maxPile)
                maxPile = pile;
        }

        int left = 1, right = maxPile;
        while (left < right) {
            int mid = left + (right - left) / 2;

            if (totalHours(h, mid, piles)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    private boolean totalHours(int h, int k, int[] piles) {
        long total = 0;
        for (int i : piles) {
            // total += Math.ceil((double) i / k);
            total += (i + k - 1) / k;

            if (total > h) {
                return false;
            }
        }
        return true;
    }
}
// @lc code=end
