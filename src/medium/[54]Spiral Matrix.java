/*
 * @lc app=leetcode id=54 lang=java
 *
 * [54] Spiral Matrix
 */

// @lc code=start
class Solution {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> result = new ArrayList<>();
        int top = 0, bottom = matrix.length - 1;
        int left = 0, right = matrix[0].length - 1;
        while (top <= bottom && left <= right) {
            for(int c = left;c<=right;c++) result.add(matrix[top][c]);
            top++;
            for(int r=top;r<=bottom;r++) result.add(matrix[r][right]);
            right--;
            if(top<=bottom){
                for(int c = right;c>=left;c--){
                    result.add(matrix[bottom][c]);
                }
                bottom--;
            }
            if(left<=right){
                for(int c = bottom;c>=top;c--){
                    result.add(matrix[c][left]);
                }
                left++;
            }
        }
        return result;
    }
}
// @lc code=end

