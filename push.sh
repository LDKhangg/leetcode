#!/bin/bash
cd ~/Dev/leetcode
bash update-readme.sh
git add .
git commit -m "$(date '+%Y-%m-%d') — update solutions"
git push origin main
echo "✅ Pushed to GitHub!"
