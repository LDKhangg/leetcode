/*
 * @lc app=leetcode id=258 lang=java
 *
 * [258] Add Digits
 */

// @lc code=start
class Solution {
    public int addDigits(int num) {
        // while(num>=10){
        //     int ans=0;
        //     while(num>0){
        //         ans+=num%10;
        //         num/=10;
        //     }
        //     num=ans;
        // }
        // return num;
        if(num==0) return 0;
        return 1+(num-1)%9;
    }
}
// @lc code=end

