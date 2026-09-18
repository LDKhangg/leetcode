#!/usr/bin/env python3
"""Single-source tracker: scan src/**/*.java by numeric ID, join with neetcode150.json.
Generates NEETCODE150.md + calls generate-neetcode-graph.py.
Match by ID only (never title/slug). Tolerant to IntelliJ '[ID]Title' and VSCode 'id.kebab' styles."""
import json, os, re, glob
from urllib.parse import quote

BASE = os.path.dirname(os.path.abspath(__file__))
JSON_PATH = os.path.join(BASE, "neetcode150.json")
MD_PATH = os.path.join(BASE, "NEETCODE150.md")

PAT_BRACKET = re.compile(r"^\[(\d+)\]")
PAT_PLAIN = re.compile(r"^(\d+)[\.\-_ ]")

def extract_id(basename):
    m = PAT_BRACKET.match(basename)
    if m:
        return int(m.group(1))
    m = PAT_PLAIN.match(basename)
    if m:
        return int(m.group(1))
    return None

def scan_done():
    files = glob.glob(os.path.join(BASE, "src", "easy", "*.java"))
    files += glob.glob(os.path.join(BASE, "src", "medium", "*.java"))
    files += glob.glob(os.path.join(BASE, "src", "hard", "*.java"))
    # also tolerate stray vscode-style files anywhere under src (except editor stash)
    files += [f for f in glob.glob(os.path.join(BASE, "src", "*.java"))]
    done = {}
    warned = []
    for f in files:
        if "/src/leetcode/" in f:
            continue
        bid = extract_id(os.path.basename(f))
        if bid is None:
            warned.append(f)
            continue
        rel = os.path.relpath(f, BASE)
        if bid in done:
            warned.append(f"duplicate ID {bid}: {rel} (kept {done[bid]})")
            continue
        done[bid] = rel
    return done, warned

def link_for(problem, done):
    pid = problem["id"]
    if pid in done:
        url = quote(done[pid], safe="/")
        return f"[x] local `{done[pid]}`", f"[{problem['title']}]({url})"
    slug = problem.get("slug", "")
    pre = " 🔒" if problem.get("premium") else ""
    return f"[ ]{pre}", f"[{problem['title']}](https://leetcode.com/problems/{slug}/)"

ORDER = ["Arrays & Hashing", "Two Pointers", "Stack", "Sliding Window", "Binary Search",
         "Linked List", "Trees", "Trie", "Heap / Priority Queue", "Backtracking",
         "Graphs", "Advanced Graphs", "1-D DP", "2-D DP", "Greedy", "Intervals",
         "Math & Geometry", "Bit Manipulation"]

def main():
    data = json.load(open(JSON_PATH))
    done, warned = scan_done()
    done_ids = set(done.keys())
    by_cat = {c: [] for c in ORDER}
    for p in data:
        by_cat.setdefault(p["category"], []).append(p)
    total_done = sum(1 for p in data if p["id"] in done_ids)
    lines = []
    lines.append("# NeetCode 150 — Tracker (single source: `src/**/*.java`)")
    lines.append("")
    lines.append(f"**Progress: {total_done}/150** — match by numeric ID only. Title/slug mismatch ignored.")
    lines.append("")
    lines.append("![NeetCode graph](neetcode-graph.svg)")
    lines.append("")
    lines.append("| # | Category | Done | Bar |")
    lines.append("|---|----------|------|-----|")
    for i, c in enumerate(ORDER, 1):
        items = by_cat.get(c, [])
        d = sum(1 for p in items if p["id"] in done_ids)
        t = len(items)
        pct = d / t if t else 0
        bar = "█" * round(pct * 10) + "░" * (10 - round(pct * 10))
        lines.append(f"| {i} | {c} | {d}/{t} | `{bar}` |")
    lines.append("")
    # Top 15 todo — foundation first (cat order asc)
    lines.append("## Next up (foundation first)")
    n = 0
    for c in ORDER:
        for p in by_cat.get(c, []):
            if p["id"] not in done_ids and not p.get("premium"):
                lines.append(f"- [ ] {p['id']} [{p['title']}](https://leetcode.com/problems/{p['slug']}/) *({c})*")
                n += 1
                if n >= 15:
                    break
        if n >= 15:
            break
    lines.append("")
    for c in ORDER:
        items = by_cat.get(c, [])
        d = sum(1 for p in items if p["id"] in done_ids)
        lines.append(f"<details open>")
        lines.append(f"<summary><b>{c}</b> — {d}/{len(items)}</summary>")
        lines.append("")
        lines.append("| | # | Problem | Diff | File / Link |")
        lines.append("|---|----|---------|------|-------------|")
        for p in items:
            box, link = link_for(p, done)
            pre = "🔒 " if p.get("premium") else ""
            mark = "✅" if p["id"] in done_ids else "⬜"
            lines.append(f"| {mark} | {p['id']} | {link} | {p['difficulty']} | {pre}`{p['slug']}` |")
        lines.append("")
        lines.append("</details>")
        lines.append("")
    if warned:
        lines.append("<details><summary>Warnings</summary>\n")
        for w in warned[:30]:
            lines.append(f"- {w}")
        lines.append("</details>")
    open(MD_PATH, "w").write("\n".join(lines))
    print(f"OK NEETCODE150 {total_done}/150 -> {MD_PATH}")
    # graph
    try:
        import importlib.util
        spec = importlib.util.spec_from_file_location("ncg", os.path.join(BASE, "generate-neetcode-graph.py"))
        ncg = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(ncg)
        ncg.generate(done_ids=done_ids)
    except Exception as e:
        print(f"graph failed: {e}")
    # stdout top list for update-readme reuse
    return total_done

if __name__ == "__main__":
    main()
