/*
 * @lc app=leetcode id=888 lang=java
 *
 * [888] Fair Candy Swap
 */

// @lc code=start
class Solution {
    public int[] fairCandySwap(int[] aliceSizes, int[] bobSizes) {
        int sumA=0;
        int sumB=0;

        for(int candy:aliceSizes){
            sumA+=candy;
        }
        
        Set<Integer> bobSet = new HashSet<>();
        for(int candy:bobSizes){
            sumB+=candy;
            bobSet.add(candy);
        }

        int difference=(sumB-sumA)/2;
        for(int aliceCandy:aliceSizes){
            int bobCandy = aliceCandy+difference;
            if(bobSet.contains(bobCandy))
                return new int[]{aliceCandy,bobCandy};
        }
        return new int[0];

    }
}
// @lc code=end

