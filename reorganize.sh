#!/bin/bash
SRC="$HOME/Dev/leetcode/src/leetcode/editor/en"
DST="$HOME/Dev/leetcode/src"

mkdir -p "$DST/easy" "$DST/medium" "$DST/hard"

move() {
  local diff="$1" file="$2"
  [ -f "$SRC/$file" ] && mv "$SRC/$file" "$DST/$diff/$file" && echo "[$diff] $file"
}

# Easy
move easy "[1]Two Sum.java"
move easy "[9]Palindrome Number.java"
move easy "[13]Roman to Integer.java"
move easy "[14]Longest Common Prefix.java"
move easy "[20]Valid Parentheses.java"
move easy "[21]Merge Two Sorted Lists.java"
move easy "[26]Remove Duplicates from Sorted Array.java"
move easy "[27]Remove Element.java"
move easy "[28]Find the Index of the First Occurrence in a String.java"
move easy "[35]Search Insert Position.java"

# Medium
move medium "[2]Add Two Numbers.java"
move medium "[3]Longest Substring Without Repeating Characters.java"
move medium "[5]Longest Palindromic Substring.java"
move medium "[6]Zigzag Conversion.java"
move medium "[7]Reverse Integer.java"
move medium "[8]String to Integer (atoi).java"
move medium "[11]Container With Most Water.java"
move medium "[12]Integer to Roman.java"
move medium "[15]3Sum.java"
move medium "[16]3Sum Closest.java"
move medium "[17]Letter Combinations of a Phone Number.java"
move medium "[18]4Sum.java"
move medium "[19]Remove Nth Node From End of List.java"
move medium "[22]Generate Parentheses.java"
move medium "[29]Divide Two Integers.java"
move medium "[31]Next Permutation.java"
move medium "[33]Search in Rotated Sorted Array.java"
move medium "[34]Find First and Last Position of Element in Sorted Array.java"
move medium "[53]Maximum Subarray.java"

# Hard
move hard "[4]Median of Two Sorted Arrays.java"
move hard "[10]Regular Expression Matching.java"
move hard "[23]Merge k Sorted Lists.java"
move hard "[25]Reverse Nodes in k-Group.java"
move hard "[30]Substring with Concatenation of All Words.java"
move hard "[32]Longest Valid Parentheses.java"

echo ""
echo "Done! Easy: $(ls $DST/easy/*.java 2>/dev/null | wc -l) | Medium: $(ls $DST/medium/*.java 2>/dev/null | wc -l) | Hard: $(ls $DST/hard/*.java 2>/dev/null | wc -l)"
