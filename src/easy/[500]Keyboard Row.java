/*
 * @lc app=leetcode id=500 lang=java
 *
 * [500] Keyboard Row
 */

// @lc code=start
class Solution {
    public String[] findWords(String[] words) {
        Set<Character> row1 =
            "qwertyuiop".chars().mapToObj(c->(char) c).collect(Collectors.toSet());
        Set<Character> row2 =
            "asdfghjkl".chars().mapToObj(c->(char) c).collect(Collectors.toSet());
        Set<Character> row3 =
            "zxcvbnm".chars().mapToObj(c->(char) c).collect(Collectors.toSet());

        ArrayList<String> result = new ArrayList<>();
        for(String word:words){
            Set<Character> chars=word.toLowerCase().chars()
                                .mapToObj(c->(char) c).collect(Collectors.toSet());
            if(row1.containsAll(chars)||row2.containsAll(chars)||row3.containsAll(chars))
                result.add(word);
        }
        return result.toArray(new String[0]);
    }
}
// @lc code=end

