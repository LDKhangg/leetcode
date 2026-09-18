#!/usr/bin/env python3
"""Generate neetcode-graph.svg: dependency tree + donut, dark style like NeetCode roadmap screenshot."""
import json, math, os

BASE = os.path.dirname(os.path.abspath(__file__))
JSON_PATH = os.path.join(BASE, "neetcode150.json")

# display label -> json category
NODES = [
    # key, label, x, y (top-left), lines
    ("arrays", "Arrays & Hashing", 425, 20, ["Arrays & Hashing"]),
    ("two", "Two Pointers", 325, 100, ["Two Pointers"]),
    ("stack", "Stack", 525, 100, ["Stack"]),
    ("bin", "Binary Search", 205, 180, ["Binary Search"]),
    ("sliding", "Sliding Window", 365, 180, ["Sliding Window"]),
    ("linked", "Linked List", 525, 180, ["Linked List"]),
    ("trees", "Trees", 365, 260, ["Trees"]),
    ("tries", "Tries", 225, 340, ["Trie"]),
    ("back", "Backtracking", 505, 340, ["Backtracking"]),
    ("heap", "Heap / Priority", 305, 420, ["Heap / Priority Queue"]),
    ("graphs", "Graphs", 465, 420, ["Graphs"]),
    ("dp1", "1-D Dynamic", 625, 420, ["1-D DP"]),
    ("intervals", "Intervals", 45, 510, ["Intervals"]),
    ("greedy", "Greedy", 205, 540, ["Greedy"]),
    ("advg", "Advanced Graphs", 365, 540, ["Advanced Graphs"]),
    ("dp2", "2-D Dynamic", 525, 540, ["2-D DP"]),
    ("bit", "Bit Manipulation", 685, 510, ["Bit Manipulation"]),
    ("math", "Math & Geometry", 525, 630, ["Math & Geometry"]),
]
W, H = 150, 46
EDGES = [
    ("arrays", "two"), ("arrays", "stack"),
    ("two", "bin"), ("two", "sliding"), ("stack", "sliding"), ("stack", "linked"),
    ("bin", "trees"), ("sliding", "trees"), ("linked", "trees"),
    ("trees", "tries"), ("trees", "heap"), ("trees", "back"),
    ("tries", "heap"), ("back", "heap"),
    ("tries", "intervals"), ("heap", "intervals"),
    ("heap", "greedy"), ("heap", "advg"),
    ("back", "graphs"), ("back", "dp1"),
    ("graphs", "dp2"), ("dp1", "dp2"),
    ("graphs", "advg"),
    ("dp2", "math"), ("bit", "math"), ("advg", "math"),
]
POS = {k: (x, y) for k, _, x, y, _ in NODES}
LABEL = {k: l for k, l, _, _, _ in NODES}
CATS = {}
for k, _, _, _, cats in NODES:
    CATS[k] = cats

def load(done_ids=None):
    data = json.load(open(JSON_PATH))
    valid_ids = set(p["id"] for p in data)
    if done_ids is None:
        # import scanner from check-neetcode to avoid duplication
        import importlib.util
        spec = importlib.util.spec_from_file_location("chk", os.path.join(BASE, "check-neetcode.py"))
        chk = importlib.util.module_from_spec(spec)
        spec.loader.exec_module(chk)
        done_map, _ = chk.scan_done()
        done_ids = set(done_map.keys())
    done_ids = set(done_ids) & valid_ids
    by_cat = {}
    for p in data:
        by_cat.setdefault(p["category"], []).append(p)
    stats = {}
    for k, cats in CATS.items():
        tot = sum(len(by_cat.get(c, [])) for c in cats)
        done = sum(1 for c in cats for p in by_cat.get(c, []) if p["id"] in done_ids)
        stats[k] = (done, tot)
    # donut by difficulty over whole 150
    e = m = h = et = mt = ht = 0
    for p in data:
        d = p["difficulty"]
        if d == "Easy":
            et += 1
            if p["id"] in done_ids:
                e += 1
        elif d == "Medium":
            mt += 1
            if p["id"] in done_ids:
                m += 1
        else:
            ht += 1
            if p["id"] in done_ids:
                h += 1
    return stats, (e, et, m, mt, h, ht, len(done_ids))

def center(k):
    x, y = POS[k]
    return (x + W / 2, y + H / 2)

def edge_path(a, b):
    x1, y1 = center(a)
    x2, y2 = center(b)
    # cubic from bottom of a to top of b
    sx, sy = x1, POS[a][1] + H
    ex, ey = x2, POS[b][1]
    my = (sy + ey) / 2
    return f"M {sx},{sy} C {sx},{my} {ex},{my} {ex},{ey}"

def arc_path(cx, cy, r, start_deg, span_deg, color, sw=10):
    if span_deg <= 0:
        return ""
    a1 = math.radians(start_deg)
    a2 = math.radians(start_deg + min(span_deg, 359.9))
    x1, y1 = cx + r * math.cos(a1), cy + r * math.sin(a1)
    x2, y2 = cx + r * math.cos(a2), cy + r * math.sin(a2)
    large = 1 if span_deg > 180 else 0
    return (f'<path d="M {x1:.1f},{y1:.1f} A {r},{r} 0 {large},1 {x2:.1f},{y2:.1f}" '
            f'stroke="{color}" stroke-width="{sw}" fill="none" stroke-linecap="round"/>')

def generate(out=None, done_ids=None):
    stats, (e, et, m, mt, h, ht, total) = load(done_ids)
    out = out or os.path.join(BASE, "neetcode-graph.svg")
    VW, VH = 1000, 720
    parts = []
    parts.append(f'<svg xmlns="http://www.w3.org/2000/svg" width="{VW}" height="{VH}" viewBox="0 0 {VW} {VH}">')
    parts.append('<defs><pattern id="dots" width="18" height="18" patternUnits="userSpaceOnUse">'
                 '<rect width="18" height="18" fill="#0d1117"/>'
                 '<circle cx="2" cy="2" r="1" fill="#21262d"/></pattern></defs>')
    parts.append(f'<rect width="{VW}" height="{VH}" rx="12" fill="url(#dots)"/>')
    # edges
    for a, b in EDGES:
        if a in POS and b in POS:
            parts.append(f'<path d="{edge_path(a,b)}" stroke="#6e7681" stroke-width="1.6" fill="none" opacity="0.8"/>')
    # nodes
    for k, label, x, y, _ in NODES:
        done, tot = stats.get(k, (0, 0))
        pct = (done / tot) if tot else 0
        stroke = "#22c55e" if tot and done == tot else "#6b6bd0"
        parts.append(f'<rect x="{x}" y="{y}" width="{W}" height="{H}" rx="8" fill="#3b3b6d" stroke="{stroke}" stroke-width="1.4"/>')
        # wrap label (max 2 lines)
        words = label.split(" / ") if " / " in label else label.split(" ")
        if len(words) == 1:
            parts.append(f'<text x="{x+W/2}" y="{y+20}" text-anchor="middle" fill="white" font-size="11.5" font-weight="600" font-family="sans-serif">{words[0]}</text>')
        else:
            # crude: first line + second line
            mid = (len(words) + 1) // 2
            l1 = " ".join(words[:mid]).replace(" /", "/")
            l2 = " ".join(words[mid:])
            parts.append(f'<text x="{x+W/2}" y="{y+16}" text-anchor="middle" fill="white" font-size="11" font-weight="600" font-family="sans-serif">{l1}</text>')
            parts.append(f'<text x="{x+W/2}" y="{y+29}" text-anchor="middle" fill="white" font-size="11" font-weight="600" font-family="sans-serif">{l2}</text>')
        parts.append(f'<text x="{x+W/2}" y="{y+H-14}" text-anchor="middle" fill="#c9d1d9" font-size="9.5" font-family="monospace">{done}/{tot}</text>')
        # progress bar
        bw = W - 20
        parts.append(f'<rect x="{x+10}" y="{y+H-9}" width="{bw}" height="3" rx="1.5" fill="#ffffff22"/>')
        parts.append(f'<rect x="{x+10}" y="{y+H-9}" width="{bw*pct:.1f}" height="3" rx="1.5" fill="white"/>')
    # right panel: donut
    px, py, pw, ph = 850, 20, 135, 200
    parts.append(f'<rect x="{px}" y="{py}" width="{pw}" height="{ph}" rx="10" fill="#161b22" stroke="#30363d"/>')
    cx, cy, r = px + pw / 2, py + 62, 30
    ARC, START = 270, 135
    tot3 = e + m + h
    all3 = et + mt + ht
    if all3:
        es = e / all3 * ARC
        ms = m / all3 * ARC
        hs = h / all3 * ARC
    else:
        es = ms = hs = 0
    bx1, by1 = cx + r * math.cos(math.radians(START)), cy + r * math.sin(math.radians(START))
    bx2, by2 = cx + r * math.cos(math.radians(START + ARC)), cy + r * math.sin(math.radians(START + ARC))
    parts.append(f'<path d="M {bx1:.1f},{by1:.1f} A {r},{r} 0 1,1 {bx2:.1f},{by2:.1f}" stroke="#2d2d3d" stroke-width="9" fill="none" stroke-linecap="round"/>')
    cur = START
    parts.append(arc_path(cx, cy, r, cur, es, "#22c55e", 9))
    cur += es
    parts.append(arc_path(cx, cy, r, cur, ms, "#f59e0b", 9))
    cur += ms
    parts.append(arc_path(cx, cy, r, cur, hs, "#ef4444", 9))
    parts.append(f'<text x="{cx}" y="{cy+2}" text-anchor="middle" fill="white" font-size="20" font-weight="bold" font-family="monospace">{total}</text>')
    parts.append(f'<text x="{cx}" y="{cy+15}" text-anchor="middle" fill="#8b949e" font-size="9" font-family="sans-serif">/150 Solved</text>')
    parts.append(f'<text x="{px+10}" y="{py+115}" fill="#22c55e" font-size="10" font-family="sans-serif">Easy {e}/{et}</text>')
    parts.append(f'<text x="{px+10}" y="{py+133}" fill="#f59e0b" font-size="10" font-family="sans-serif">Med {m}/{mt}</text>')
    parts.append(f'<text x="{px+10}" y="{py+151}" fill="#ef4444" font-size="10" font-family="sans-serif">Hard {h}/{ht}</text>')
    parts.append(f'<text x="{px+10}" y="{py+175}" fill="#8b949e" font-size="9" font-family="sans-serif">NeetCode 150</text>')
    parts.append('</svg>')
    open(out, "w").write("\n".join(parts))
    print(f"OK graph {total}/150 -> {out}")
    return out

if __name__ == "__main__":
    generate()
