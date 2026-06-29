/*
 * @lc app=leetcode id=66 lang=java
 *
 * [66] Plus One
 */

// @lc code=start
class Solution {
    public int[] plusOne(int[] digits) {
    int n = digits.length;
    //     int carry = 0;
    //     if (digits[n - 1] + 1 > 9) {
    //         carry = 1;
    //         digits[n-1]=0;
    //         for (int i = n - 2; i >= 0; i--) {
    //             if (carry == 0)
    //                 return digits;
    //             else{
    //                 digits[i]++;
    //                 carry--;
    //             }
    //             if (digits[i] == 10) {
    //                 digits[i] = 0;
    //                 carry++;
    //             }
    //         }
    //         if (carry == 1) {
    //             int[] result = new int[n + 1];
    //             result[0] = 1;
    //             result[1] = digits[0] % 10;
    //             for (int i = 2; i < n + 1; i++) {
    //                 result[i] = digits[i - 1];
    //             }
    //             return result;
    //         }
    //         return digits;
    //     }

    //     digits[n - 1]++;
    //     return digits;
    // }
    for(int i = n-1;i>=0;i--){
        if(digits[i]+1>9)
            digits[i]=0;
        else {
            digits[i]++;
            return digits;
        }
    }
    int[] result = new int[n+1];
    result[0]=1;
    for(int i = 1;i<n+1;i++){
        result[i]=digits[i-1];
    }
    return result;
    }
}
// @lc code=end

