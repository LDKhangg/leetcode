/*
 * @lc app=leetcode id=347 lang=java
 *
 * [347] Top K Frequent Elements
 */

// @lc code=start
class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer,Integer> map = new HashMap<>();
        for(int i : nums){
            if(map.get(i)==null){
                map.put(i,1);
            }
            else{
                map.put(i,map.get(i)+1);
            }
        }
        int[] arr = new int[k];
        Map<Integer,List<Integer>> treeMap = new TreeMap<>(Comparator.reverseOrder());
        for(Map.Entry<Integer,Integer> entry: map.entrySet()){
            int num = entry.getKey();
            int req = entry.getValue();

            if(treeMap.get(req)==null){
                List<Integer> rList = new ArrayList<>();
                rList.add(num);
                treeMap.put(req,rList);
            }
            else{
                treeMap.get(req).add(num);
            }
        }
        List<Integer> c = new ArrayList<>();
        for(Map.Entry<Integer, List<Integer> > entry : treeMap.entrySet()){
            if(k == 0 ) break;
            for(Integer i : entry.getValue()){
                c.add(i);
                k--;
                if(k==0) break;
            }
        }
        return c.stream().mapToInt(i -> i).toArray();
        
    }
}
// @lc code=end

