/*
 * @lc app=leetcode id=463 lang=java
 *
 * [463] Island Perimeter
 */

// @lc code=start
class Solution {
    public int islandPerimeter(int[][] grid) {
        int landCount = 0;
        int sharedEdges = 0;
        for(int i = 0; i < grid.length;i++){
            for(int j=0;j<grid[0].length;j++){
                if(grid[i][j]==1){
                    landCount++;
                    if(j+1<grid[0].length && grid[i][j+1]==1){
                        sharedEdges++;
                    }
                    if(i+1<grid.length&&grid[i+1][j]==1){
                        sharedEdges++;
                    }
                }
            }
        }
        return landCount*4-sharedEdges*2;
    }
}
// @lc code=end

