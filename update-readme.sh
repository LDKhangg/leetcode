#!/bin/bash

EASY=$(find src/easy -name "*.java" 2>/dev/null | wc -l)
MEDIUM=$(find src/medium -name "*.java" 2>/dev/null | wc -l)
HARD=$(find src/hard -name "*.java" 2>/dev/null | wc -l)
TOTAL=$((EASY + MEDIUM + HARD))

cat > README.md << MDEOF
# 🧩 LeetCode Solutions

My LeetCode solutions in Java.

## 📊 Progress

![Total](https://img.shields.io/badge/Total-${TOTAL}-blue)
![Easy](https://img.shields.io/badge/Easy-${EASY}-brightgreen)
![Medium](https://img.shields.io/badge/Medium-${MEDIUM}-orange)
![Hard](https://img.shields.io/badge/Hard-${HARD}-red)

| Difficulty | Solved |
|------------|--------|
| 🟢 Easy    | ${EASY} |
| 🟡 Medium  | ${MEDIUM} |
| 🔴 Hard    | ${HARD} |

## 📁 Structure

\`\`\`
leetcode/
├── src/
│   ├── easy/
│   ├── medium/
│   └── hard/
\`\`\`

## 🗂️ Solutions

### 🟢 Easy
MDEOF

for f in $(find src/easy -name "*.java" | sort); do
    name=$(basename "$f" .java)
    echo "- [\`$name\`]($f)" >> README.md
done

echo -e "\n### 🟡 Medium" >> README.md
for f in $(find src/medium -name "*.java" | sort); do
    name=$(basename "$f" .java)
    echo "- [\`$name\`]($f)" >> README.md
done

echo -e "\n### 🔴 Hard" >> README.md
for f in $(find src/hard -name "*.java" | sort); do
    name=$(basename "$f" .java)
    echo "- [\`$name\`]($f)" >> README.md
done

echo "" >> README.md
echo "---" >> README.md
echo "*Last updated: $(date '+%Y-%m-%d %H:%M')*" >> README.md
