#!/bin/bash

cd ~/Dev/leetcode

SOLUTION_DIR="src/leetcode/editor/en"
CACHE_FILE=".difficulty-cache.json"

# Init cache if not exists
if [ ! -f "$CACHE_FILE" ]; then
    echo "{}" > "$CACHE_FILE"
fi

EASY=0
MEDIUM=0
HARD=0
EASY_LIST=""
MEDIUM_LIST=""
HARD_LIST=""

while IFS= read -r f; do
    name=$(basename "$f" .java)
    num=$(echo "$name" | grep -oP '(?<=\[)\d+(?=\])')

    # Check cache first
    cached=$(python3 -c "import json; d=json.load(open('$CACHE_FILE')); print(d.get('$num',''))" 2>/dev/null)

    if [ -z "$cached" ]; then
        # Get titleSlug from editor.xml
        slug=$(grep -A5 "\[$num\]" .idea/leetcode/editor.xml | grep titleSlug | grep -oP '(?<=value=")[^"]+' | head -1)
        if [ -n "$slug" ]; then
            diff=$(curl -s -X POST "https://leetcode.com/graphql" \
                -H "Content-Type: application/json" \
                -d "{\"query\":\"{ question(titleSlug: \\\"$slug\\\") { difficulty } }\"}" \
                | grep -oP '(?<="difficulty":")[^"]+')
            if [ -n "$diff" ]; then
                python3 -c "
import json
d=json.load(open('$CACHE_FILE'))
d['$num']='$diff'
json.dump(d,open('$CACHE_FILE','w'))
"
                cached="$diff"
            fi
        fi
    fi

    case "$cached" in
        "Easy")
            EASY=$((EASY+1))
            EASY_LIST="$EASY_LIST\n- [\`$name\`]($f)"
            ;;
        "Medium")
            MEDIUM=$((MEDIUM+1))
            MEDIUM_LIST="$MEDIUM_LIST\n- [\`$name\`]($f)"
            ;;
        "Hard")
            HARD=$((HARD+1))
            HARD_LIST="$HARD_LIST\n- [\`$name\`]($f)"
            ;;
    esac
done < <(find "$SOLUTION_DIR" -maxdepth 1 -name "*.java" | sort -t'[' -k2 -n)

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
└── src/leetcode/editor/en/
\`\`\`

## 🗂️ Solutions

### 🟢 Easy
MDEOF

echo -e "$EASY_LIST" >> README.md
echo -e "\n### 🟡 Medium" >> README.md
echo -e "$MEDIUM_LIST" >> README.md
echo -e "\n### 🔴 Hard" >> README.md
echo -e "$HARD_LIST" >> README.md
echo "" >> README.md
echo "---" >> README.md
echo "*Last updated: $(date '+%Y-%m-%d %H:%M')*" >> README.md
