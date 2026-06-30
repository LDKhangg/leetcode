/*
 * @lc app=leetcode id=118 lang=java
 *
 * [118] Pascal's Triangle
 */

// @lc code=start
class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0;i<numRows;i++){
            List<Integer> ls = new ArrayList<>();
            for(int j = 0;j<=i;j++){
                if(j==0||j==i)
                    ls.add(1);
                else{
                    List<Integer> prevL=result.get(i-1);
                    ls.add(prevL.get(j-1)+prevL.get(j));
                }
            }
            result.add(ls);
        }
        return result;
    }
}
// @lc code=end

