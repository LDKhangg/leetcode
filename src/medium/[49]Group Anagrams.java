/*
 * @lc app=leetcode id=49 lang=java
 *
 * [49] Group Anagrams
 */

// @lc code=start
class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {
        if (strs == null || strs.length == 0) return new ArrayList<>();

        Map<String,List<String>> map = new HashMap<>();
        //Cách 1: siêu tối ưu tốc dộ chạy và usage
        for(String str: strs){
            String sortedStr = sortedStr(str);
            List<String> list = map.get(sortedStr);
            if(list==null){
                list = new ArrayList<>();
                map.put(sortedStr,list);
            }
                list.add(str);

        //Cách 2: clean code nhưng không nhanh 
        //     String sortedStr = sortedStr(str);
        //     map.computeIfAbsent(sortedStr,k-> new ArrayList<>()).add(str);
        }

       

        return new ArrayList<>(map.values());
        
    }
    private String sortedStr(String str){
        char[] sArr = str.toCharArray();
        Arrays.sort(sArr);
        return new String(sArr);
    }
}
// @lc code=end

