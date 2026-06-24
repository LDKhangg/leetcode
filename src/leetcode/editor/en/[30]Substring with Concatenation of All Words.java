//You are given a string s and an array of strings words. All the strings of 
//words are of the same length. 
//
// A concatenated string is a string that exactly contains all the strings of 
//any permutation of words concatenated. 
//
// 
// For example, if words = ["ab","cd","ef"], then "abcdef", "abefcd", "cdabef", 
//"cdefab", "efabcd", and "efcdab" are all concatenated strings. "acdbef" is not 
//a concatenated string because it is not the concatenation of any permutation of 
//words. 
// 
//
// Return an array of the starting indices of all the concatenated substrings 
//in s. You can return the answer in any order. 
//
// 
// Example 1: 
//
// 
// Input: s = "barfoothefoobarman", words = ["foo","bar"] 
// 
//
// Output: [0,9] 
//
// Explanation: 
//
// The substring starting at 0 is "barfoo". It is the concatenation of ["bar",
//"foo"] which is a permutation of words. The substring starting at 9 is "foobar". 
//It is the concatenation of ["foo","bar"] which is a permutation of words. 
//
// Example 2: 
//
// 
// Input: s = "wordgoodgoodgoodbestword", words = ["word","good","best","word"] 
//
// 
//
// Output: [] 
//
// Explanation: 
//
// There is no concatenated substring. 
//
// Example 3: 
//
// 
// Input: s = "barfoofoobarthefoobarman", words = ["bar","foo","the"] 
// 
//
// Output: [6,9,12] 
//
// Explanation: 
//
// The substring starting at 6 is "foobarthe". It is the concatenation of [
//"foo","bar","the"]. The substring starting at 9 is "barthefoo". It is the 
//concatenation of ["bar","the","foo"]. The substring starting at 12 is "thefoobar". It is 
//the concatenation of ["the","foo","bar"]. 
//
// 
// Constraints: 
//
// 
// 1 <= s.length <= 10⁴ 
// 1 <= words.length <= 5000 
// 1 <= words[i].length <= 30 
// s and words[i] consist of lowercase English letters. 
// 
//
// Related Topics Hash Table String Sliding Window 👍 2695 👎 442


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//leetcode submit region begin(Prohibit modification and deletion)
class Solution {
    public List<Integer> findSubstring(String s, String[] words) {
        int wordLen=words[0].length();
        int wordCount=words.length;
        int totalLen=wordCount*wordLen;
        if(s.length()<totalLen) return new ArrayList<>();
        Map<String,Integer> target = new HashMap<>();
        for(String word:words){
            target.put(word,target.getOrDefault(word,0)+1);
        }
        List<Integer> result =new ArrayList<>();
        for(int i=0;i<wordLen;i++){
            int left=i;
            int count=0;
            Map<String,Integer> window=new HashMap<>();
            for(int j=i;j+wordLen<=s.length();j+=wordLen){
                String subString=s.substring(j,j+wordLen);
                if(!target.containsKey(subString)){
                    window.clear();
                    count=0;
                    left=j+wordLen;
                    continue;
                }
                window.put(subString,window.getOrDefault(subString,0)+1);
                count++;
                while(window.get(subString)>target.get(subString)){
                    String leftWord=s.substring(left,left+wordLen);
                    window.put(leftWord,window.get(leftWord)-1);
                    count--;
                    left+=wordLen;
                }

                if(count==wordCount){
                    result.add(left);
                    String leftWord=s.substring(left,left+wordLen);
                    window.put(leftWord,window.get(leftWord)-1);
                    count--;
                    left+=wordLen;
                }
            }
        }
        return result;
    }
}
//leetcode submit region end(Prohibit modification and deletion)
