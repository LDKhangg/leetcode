#!/bin/bash

cd ~/Dev/leetcode

SOLUTION_DIR="src/leetcode/editor/en"
TOTAL=$(find "$SOLUTION_DIR" -maxdepth 1 -name "*.java" 2>/dev/null | wc -l)

cat > README.md << MDEOF
# 🧩 LeetCode Solutions

My LeetCode solutions in Java.

## 📊 Progress

![Total](https://img.shields.io/badge/Total-${TOTAL}-blue)

| Solved |
|--------|
| ${TOTAL} problems |

## 🗂️ Solutions

MDEOF

for f in $(find "$SOLUTION_DIR" -maxdepth 1 -name "*.java" | sort -t'[' -k2 -n); do
    name=$(basename "$f" .java)
    echo "- [\`$name\`]($f)" >> README.md
done

echo "" >> README.md
echo "---" >> README.md
echo "*Last updated: $(date '+%Y-%m-%d %H:%M')*" >> README.md
