//Given a signed 32-bit integer x, return x with its digits reversed. If 
//reversing x causes the value to go outside the signed 32-bit integer range [-2³¹, 2³¹ -
// 1], then return 0. 
//
// Assume the environment does not allow you to store 64-bit integers (signed 
//or unsigned). 
//
// 
// Example 1: 
//
// 
//Input: x = 123
//Output: 321
// 
//
// Example 2: 
//
// 
//Input: x = -123
//Output: -321
// 
//
// Example 3: 
//
// 
//Input: x = 120
//Output: 21
// 
//
// 
// Constraints: 
//
// 
// -2³¹ <= x <= 2³¹ - 1 
// 
//
// Related Topics Math 👍 15644 👎 14015


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public int reverse(int x) {
        if(x==0) return 0;
        StringBuilder sb = new StringBuilder();
        boolean isNegative = (x < 0) ? true : false;
        if(x==Integer.MIN_VALUE) x=Integer.MAX_VALUE;
        else if(isNegative) x*=-1;
        while (x > 0) {
            sb.append(x % 10);
            x = x/ 10;
        }
        long reversed = Long.parseLong(sb.toString());

        if (isNegative) {
            reversed = -reversed;
        }

        if (reversed < Integer.MIN_VALUE || reversed > Integer.MAX_VALUE) {
            return 0;
        }

        return (int) reversed;}
}
//leetcode submit region end(Prohibit modification and deletion)
