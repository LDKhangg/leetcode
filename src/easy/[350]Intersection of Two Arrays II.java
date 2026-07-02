/*
 * @lc app=leetcode id=350 lang=java
 *
 * [350] Intersection of Two Arrays II
 */

// @lc code=start
class Solution {
    public int[] intersect(int[] nums1, int[] nums2) {
        Map<Integer,Integer> map1 = new HashMap<>();
        // for(int i = 0;i<nums1.length;i++){
        //     map1.put(nums1[i],map1.getOrDefault(nums1[i],0)+1);
        // }
        // Map<Integer,Integer> map2=new HashMap<>();
        // for(int i = 0 ; i<nums2.length;i++){
        //     if(map1.containsKey(nums2[i])){
        //     map2.put(nums2[i],map2.getOrDefault(nums2[i],0)+1);
        //     }
        // }

        // for(Map.Entry<Integer,Integer> entry : map2.entrySet()){
        //     int key=entry.getKey();
        //     if(map1.containsKey(key)){
        //         map2.put(key,Math.min(map1.get(key),map2.get(key)));
        //     }
        // }
        // int nums=0;
        // for(Map.Entry<Integer,Integer> entry:map2.entrySet()){
        //     nums+=entry.getValue();
        // }
        // int[] result = new int[nums];
        //  int i =0;
        // for(Map.Entry<Integer,Integer> entry:map2.entrySet()){
        //     int key = entry.getKey();
        //     int value = entry.getValue();
        //     while(value!=0){
        //         result[i++]=key;
        //         value--;
        //     }
        // }

        for(int num:nums1){
            map1.put(num,map1.getOrDefault(num,0)+1);
        }

        List<Integer> resultList=new ArrayList<>();
        for(int num:nums2){
            int count = map1.getOrDefault(num,0);
            if(count>0){
                resultList.add(num);
                map1.put(num,count-1);
            }
        }

        int[] result = new int[resultList.size()];
        for(int i=0;i<resultList.size();i++){
            result[i]=resultList.get(i);
        }


        return result;
    }
}
// @lc code=end

