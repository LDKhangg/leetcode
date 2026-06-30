/*
 * @lc app=leetcode id=119 lang=java
 *
 * [119] Pascal's Triangle II
 */

// @lc code=start
class Solution {
    public List<Integer> getRow(int rowIndex) {
        // List<List<Integer>> result = new ArrayList<>();
        // for(int i = 0 ; i <= rowIndex;i++){
        //     List<Integer> row = new ArrayList<>();
        //     for(int j = 0;j<=i;j++){
        //         if(j==0||j==i){
        //             row.add(1);
        //         } else{
        //             List<Integer> prevRow= result.get(i-1);
        //             row.add(prevRow.get(j-1)+prevRow.get(j));
        //         }
        //     }
        //     result.add(row);
        // }
        // return result.get(rowIndex);

        //recurrence
        List<Integer> result = new ArrayList<>();
        long ans=1;
        result.add(1);
        for(int i = 1; i<=rowIndex;i++){
            ans*=(rowIndex-i+1);
            ans/=i;
            result.add((int)ans);
        }
        return result;
    }
}
// @lc code=end

