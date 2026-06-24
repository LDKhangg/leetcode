#!/usr/bin/env python3
import math, sys

easy   = int(sys.argv[1])
medium = int(sys.argv[2])
hard   = int(sys.argv[3])
total  = easy + medium + hard

# Arc: 270° from 135° to 45° clockwise (gap at bottom)
ARC   = 270
START = 135
CX, CY, R = 100, 90, 62
SW = 14   # stroke width

def pt(angle_deg):
    a = math.radians(angle_deg)
    return CX + R * math.cos(a), CY + R * math.sin(a)

def arc(start, span, color):
    if span <= 0:
        return ""
    end = start + span
    x1, y1 = pt(start)
    # avoid full-circle edge case
    clamp = min(span, 359.9)
    x2, y2 = pt(start + clamp)
    large = 1 if clamp > 180 else 0
    return (f'<path d="M {x1:.3f},{y1:.3f} A {R},{R} 0 {large},1 {x2:.3f},{y2:.3f}" '
            f'stroke="{color}" stroke-width="{SW}" fill="none" stroke-linecap="round"/>')

if total == 0:
    e_span = m_span = h_span = 0
    bg_span = ARC
else:
    e_span = easy   / total * ARC
    m_span = medium / total * ARC
    h_span = hard   / total * ARC
    bg_span = 0

cursor = START
arc_e = arc(cursor, e_span, "#22c55e"); cursor += e_span
arc_m = arc(cursor, m_span, "#f59e0b"); cursor += m_span
arc_h = arc(cursor, h_span, "#ef4444"); cursor += h_span

# background track
bx1, by1 = pt(START)
bx2, by2 = pt(START + ARC)
bg_path = (f'<path d="M {bx1:.3f},{by1:.3f} A {R},{R} 0 1,1 {bx2:.3f},{by2:.3f}" '
           f'stroke="#2d2d3d" stroke-width="{SW}" fill="none" stroke-linecap="round"/>')

svg = f"""<svg xmlns="http://www.w3.org/2000/svg" width="320" height="160" viewBox="0 0 320 160">
  <rect width="320" height="160" rx="12" fill="#161b22"/>

  <!-- donut -->
  {bg_path}
  {arc_e}
  {arc_m}
  {arc_h}

  <!-- center text -->
  <text x="{CX}" y="{CY - 8}" text-anchor="middle" fill="white" font-size="22" font-weight="bold" font-family="monospace">{total}</text>
  <text x="{CX}" y="{CY + 12}" text-anchor="middle" fill="#8b949e" font-size="11" font-family="sans-serif">Solved</text>

  <!-- stats labels -->
  <text x="220" y="55"  fill="#22c55e" font-size="12" font-weight="bold" font-family="sans-serif">Easy</text>
  <text x="300" y="55"  fill="#e6edf3" font-size="12" font-family="monospace" text-anchor="end">{easy}</text>

  <text x="220" y="82"  fill="#f59e0b" font-size="12" font-weight="bold" font-family="sans-serif">Medium</text>
  <text x="300" y="82"  fill="#e6edf3" font-size="12" font-family="monospace" text-anchor="end">{medium}</text>

  <text x="220" y="109" fill="#ef4444" font-size="12" font-weight="bold" font-family="sans-serif">Hard</text>
  <text x="300" y="109" fill="#e6edf3" font-size="12" font-family="monospace" text-anchor="end">{hard}</text>
</svg>"""

print(svg)
