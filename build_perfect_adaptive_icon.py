import os
import subprocess

# 1. Generate Background SVG (512x512)
# Dark charcoal/slate concentric radial grooves matching input_file_0.png
bg_svg = '''<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <radialGradient id="bgGrad" cx="50%" cy="50%" r="65%">
      <stop offset="0%" stop-color="#3F4147"/>
      <stop offset="50%" stop-color="#2D2F34"/>
      <stop offset="85%" stop-color="#1B1C20"/>
      <stop offset="100%" stop-color="#141518"/>
    </radialGradient>
  </defs>
  <!-- Background rect -->
  <rect width="512" height="512" fill="url(#bgGrad)"/>
  
  <!-- Concentric vinyl groove circles -->
  <g stroke="#FFFFFF" stroke-opacity="0.04" fill="none" stroke-width="1.2">
'''

for r in range(4, 256, 3):
    bg_svg += f'    <circle cx="256" cy="256" r="{r}"/>\n'

bg_svg += '''  </g>
</svg>'''

with open("bg.svg", "w") as f:
    f.write(bg_svg)

# 2. Generate Foreground Calculator SVG (512x512)
# Designed specifically to fit within the 66dp safe zone (approx 312px diameter circle at center 256,256)
# Calculator box: width 224px (x=144 to 368), height 300px (y=106 to 406)
fg_svg = '''<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <!-- Soft outer drop shadow -->
    <filter id="outerShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="10" stdDeviation="14" flood-color="#000000" flood-opacity="0.65"/>
    </filter>
    <!-- Key drop shadow -->
    <filter id="keyShadow" x="-20%" y="-20%" width="140%" height="140%">
      <feDropShadow dx="0" dy="2" stdDeviation="2" flood-color="#000000" flood-opacity="0.5"/>
    </filter>
    <linearGradient id="bodyGrad" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%" stop-color="#3B3D43"/>
      <stop offset="100%" stop-color="#232529"/>
    </linearGradient>
    <linearGradient id="screenGrad" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%" stop-color="#1A1C1E"/>
      <stop offset="100%" stop-color="#26282D"/>
    </linearGradient>
    <linearGradient id="keyGrad" x1="0" y1="0" x2="0" y2="1">
      <stop offset="0%" stop-color="#33353B"/>
      <stop offset="100%" stop-color="#26272C"/>
    </linearGradient>
    <linearGradient id="vaultGrad" x1="0" y1="0" x2="1" y2="1">
      <stop offset="0%" stop-color="#4E5158"/>
      <stop offset="50%" stop-color="#2F3136"/>
      <stop offset="100%" stop-color="#1A1B1F"/>
    </linearGradient>
  </defs>

  <g>
    <!-- Main Calculator Body Box (width=224, height=300, centered at x=256, y=256 -> x:144..368, y:106..406) -->
    <rect x="144" y="106" width="224" height="300" rx="34" fill="url(#bodyGrad)" stroke="#4E5158" stroke-width="2"/>
    <rect x="147" y="109" width="218" height="294" rx="31" fill="none" stroke="#161719" stroke-width="1.5" opacity="0.8"/>

    <!-- Display Screen (x:162..350, width=188, y:128..192, height=64) -->
    <rect x="162" y="128" width="188" height="64" rx="12" fill="url(#screenGrad)" stroke="#121315" stroke-width="1.5"/>
    <rect x="164" y="130" width="184" height="60" rx="10" fill="none" stroke="#3A3D43" stroke-width="1" opacity="0.4"/>
    
    <!-- Screen Text '0' -->
    <text x="336" y="174" font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif" font-size="42" font-weight="300" fill="#CBD5E1" text-anchor="end">0</text>

    <!-- Keypad Matrix (4 Cols x 4 Rows) -->
    <!-- Col width: 40px, gap: 8px -> total 4*40 + 3*8 = 184px (x: 164, 212, 260, 308) -->
    <!-- Row height: 40px, gap: 8px -> total 4*40 + 3*8 = 184px (y: 206, 254, 302, 350) -->
    
    <!-- Key Rectangles -->
    <!-- Row 0: 7, 8, 9, ÷ -->
    <rect x="164" y="206" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="212" y="206" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="260" y="206" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="308" y="206" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>

    <!-- Row 1: 4, 5, 6, × -->
    <rect x="164" y="254" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="212" y="254" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="260" y="254" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="308" y="254" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>

    <!-- Row 2: 1, 2, 3, - -->
    <rect x="164" y="302" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="212" y="302" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="260" y="302" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="308" y="302" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>

    <!-- Row 3: 0, ., C, + -->
    <rect x="164" y="350" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="212" y="350" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="260" y="350" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>
    <rect x="308" y="350" width="40" height="40" rx="8" fill="url(#keyGrad)" stroke="#1A1B1E" stroke-width="1.2"/>

    <!-- Key Text Labels -->
    <g font-family="-apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Arial, sans-serif" font-size="18" font-weight="500" fill="#E2E8F0" text-anchor="middle">
      <!-- Row 0 -->
      <text x="184" y="232">7</text>
      <text x="232" y="232">8</text>
      <text x="280" y="232">9</text>
      <text x="328" y="233" font-size="20">÷</text>

      <!-- Row 1 -->
      <text x="184" y="280">4</text>
      <text x="232" y="280">5</text>
      <text x="280" y="280">6</text>
      <text x="328" y="280" font-size="18">×</text>

      <!-- Row 2 -->
      <text x="184" y="328">1</text>
      <text x="232" y="328">2</text>
      <text x="280" y="328">3</text>
      <text x="328" y="326" font-size="22">−</text>

      <!-- Row 3 -->
      <text x="184" y="376">0</text>
      <text x="232" y="374" font-size="22">.</text>
      <text x="280" y="376">C</text>
      <text x="328" y="376" font-size="20">+</text>
    </g>

    <!-- Central Vault Wheel Lock (Centered over middle of keypad grid at x=256, y=278) -->
    <g transform="translate(256, 278)">
      <!-- Outer shadow ring -->
      <circle cx="0" cy="0" r="36" fill="#121316" opacity="0.65"/>
      <!-- Outer metallic wheel ring -->
      <circle cx="0" cy="0" r="32" fill="url(#vaultGrad)" stroke="#5E616A" stroke-width="2.5"/>
      <circle cx="0" cy="0" r="25" fill="none" stroke="#16171A" stroke-width="1.8"/>
      
      <!-- 6 Radial Handles/Spokes -->
      <g stroke="url(#vaultGrad)" stroke-width="6" stroke-linecap="round">
        <line x1="0" y1="-36" x2="0" y2="-16"/>
        <line x1="0" y1="16" x2="0" y2="36"/>
        <line x1="-31" y1="-18" x2="-14" y2="-8"/>
        <line x1="14" y1="8" x2="31" y2="18"/>
        <line x1="-31" y1="18" x2="-14" y2="-8"/>
        <line x1="14" y1="-8" x2="31" y2="-18"/>
      </g>
      
      <!-- Inner Hub -->
      <circle cx="0" cy="0" r="16" fill="#232529" stroke="#4E5158" stroke-width="1.8"/>
      <circle cx="0" cy="0" r="8" fill="#121315" stroke="#33353B" stroke-width="1.2"/>
    </g>
  </g>
</svg>'''

with open("fg.svg", "w") as f:
    f.write(fg_svg)

print("SVGs written successfully!")

# Render SVGs to PNGs using ffmpeg
subprocess.run(["ffmpeg", "-y", "-i", "bg.svg", "ic_launcher_background.png"], check=True)
subprocess.run(["ffmpeg", "-y", "-i", "fg.svg", "ic_launcher_foreground_user.png"], check=True)

# Save to app/src/main/res/drawable
os.makedirs("app/src/main/res/drawable", exist_ok=True)
subprocess.run(["cp", "ic_launcher_background.png", "app/src/main/res/drawable/ic_launcher_background.png"], check=True)
subprocess.run(["cp", "ic_launcher_foreground_user.png", "app/src/main/res/drawable/ic_launcher_foreground_user.png"], check=True)
subprocess.run(["cp", "ic_launcher_foreground_user.png", "app/src/main/res/drawable/ic_launcher_foreground.png"], check=True)

print("PNG assets copied to drawable!")
