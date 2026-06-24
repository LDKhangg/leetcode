//Given two sorted arrays nums1 and nums2 of size m and n respectively, return 
//the median of the two sorted arrays. 
//
// The overall run time complexity should be O(log (m+n)). 
//
// 
// Example 1: 
//
// 
//Input: nums1 = [1,3], nums2 = [2]
//Output: 2.00000
//Explanation: merged array = [1,2,3] and median is 2.
// 
//
// Example 2: 
//
// 
//Input: nums1 = [1,2], nums2 = [3,4]
//Output: 2.50000
//Explanation: merged array = [1,2,3,4] and median is (2 + 3) / 2 = 2.5.
// 
//
// 
// Constraints: 
//
// 
// nums1.length == m 
// nums2.length == n 
// 0 <= m <= 1000 
// 0 <= n <= 1000 
// 1 <= m + n <= 2000 
// -10⁶ <= nums1[i], nums2[i] <= 10⁶ 
// 
//
// Related Topics Array Binary Search Divide and Conquer 👍 32336 👎 3588


//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int totalLength=nums1.length+nums2.length;
        int[] mergedArr=Arrays.copyOf(nums1,totalLength);
        System.arraycopy(nums2,0,mergedArr,nums1.length,nums2.length);
        Arrays.sort(mergedArr);
        if(totalLength%2==0){
            return (double) (mergedArr[totalLength/2]+mergedArr[totalLength/2-1])/2;
        } else {
            return (double)  mergedArr[totalLength/2];
        }
    }
}
//leetcode submit region end(Prohibit modification and deletion)
