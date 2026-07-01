/*
 * @lc app=leetcode id=228 lang=java
 *
 * [228] Summary Ranges
 */

// @lc code=start
class Solution {
    public List<String> summaryRanges(int[] nums) {
        int n = nums.length;
        int i = 0;
        List<String> result = new ArrayList<>();
        while(i<n){
            StringBuilder sb= new StringBuilder();
            sb.append(nums[i]);
            int j = i;
            while (j + 1 < n && nums[j + 1] == nums[j] + 1) {
                j++;
            }
            if (j > i) {
                sb.append("->").append(nums[j]);
            }
            result.add(sb.toString());
            i = j + 1;
        }
        return result;
    }
}
// @lc code=end

