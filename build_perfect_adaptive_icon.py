import os
import re
import subprocess

# Paths
drawable_dir = "app/src/main/res/drawable"
os.makedirs(drawable_dir, exist_ok=True)

print("1. Generating perfectly centered icon_background.svg...")
# We use cx="256" cy="256" to align perfectly with the 512x512 canvas center
svg_bg = """<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <defs>
    <radialGradient id="bgGrad" cx="50%" cy="50%" r="50%">
      <stop offset="0%" stop-color="#42444A"/>
      <stop offset="60%" stop-color="#2D2F35"/>
      <stop offset="100%" stop-color="#1A1B1F"/>
    </radialGradient>
  </defs>
  <!-- Background base -->
  <rect width="512" height="512" fill="url(#bgGrad)"/>
  
  <!-- Perfectly centered concentric grooves -->
  <g stroke="#ffffff" stroke-opacity="0.04" fill="none" stroke-width="1">
"""

for r in range(10, 250, 4):
    svg_bg += f'    <circle cx="256" cy="256" r="{r}"/>\n'

svg_bg += """  </g>
</svg>"""

with open("icon_background.svg", "w") as f:
    f.write(svg_bg)
print("✓ Generated icon_background.svg")


print("2. Cleaning icon_foreground.svg by stripping filters...")
# Read the original SVG foreground
if os.path.exists("icon_foreground.svg"):
    with open("icon_foreground.svg", "r") as f:
        svg_fg_content = f.read()
else:
    raise FileNotFoundError("Missing icon_foreground.svg in workspace root!")

# Let's remove any <filter>...</filter> tags completely using regex
clean_svg = re.sub(r'<filter\b[^>]*>.*?</filter>', '', svg_fg_content, flags=re.DOTALL)

# Let's remove any filter="..." attributes from any tags
clean_svg = re.sub(r'\s*filter="url\(#[^)]+\)"', '', clean_svg)

# Write out the clean SVG
with open("temp_foreground_clean.svg", "w") as f:
    f.write(clean_svg)
print("✓ Created clean temporary SVG vector: temp_foreground_clean.svg")


print("3. Compiling SVGs to PNG...")
# 1. Render background SVG to drawable directory
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "icon_background.svg",
    "-o", os.path.join(drawable_dir, "ic_launcher_background.png")
], check=True)

# Also make a copy as icon_background.png
subprocess.run([
    "cp",
    os.path.join(drawable_dir, "ic_launcher_background.png"),
    os.path.join(drawable_dir, "icon_background.png")
], check=True)

# 2. Render clean foreground SVG to raw_fg.png
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "temp_foreground_clean.svg",
    "-o", "raw_fg.png"
], check=True)
print("✓ Rendered raw assets")


print("4. Optimizing and scaling foreground for perfect Android safe-zone sizing...")
# Trim the transparent borders around the calculator body to get a tight bounding box
subprocess.run([
    "convert", "raw_fg.png",
    "-trim", "+repage",
    "trimmed_fg.png"
], check=True)

# Resize the trimmed calculator to fit inside a 240x240 box (maintaining aspect ratio)
# This is inside the 72dp (61%) safe zone of the 108dp adaptive icon canvas
subprocess.run([
    "convert", "trimmed_fg.png",
    "-resize", "240x240",
    "scaled_fg.png"
], check=True)

# Center the scaled calculator body exactly in the center of a transparent 512x512 canvas
subprocess.run([
    "convert", "-size", "512x512", "xc:none",
    "scaled_fg.png",
    "-gravity", "center",
    "-composite",
    os.path.join(drawable_dir, "ic_launcher_foreground.png")
], check=True)

# Also make a copy as icon_foreground.png
subprocess.run([
    "cp",
    os.path.join(drawable_dir, "ic_launcher_foreground.png"),
    os.path.join(drawable_dir, "icon_foreground.png")
], check=True)
print("✓ Sized and centered foreground perfectly!")


print("5. Compositing square and round launcher icons...")
# Center the scaled foreground on top of the background to make the legacy square icon
subprocess.run([
    "convert",
    os.path.join(drawable_dir, "ic_launcher_background.png"),
    "scaled_fg.png",
    "-gravity", "center",
    "-composite",
    "master_composite_512.png"
], check=True)

# Apply a perfect circle mask to make the legacy round icon
subprocess.run([
    "convert", "master_composite_512.png",
    "(", "-size", "512x512", "xc:none", "-fill", "white", "-draw", "circle 256,256 256,0", ")",
    "-alpha", "off",
    "-compose", "CopyOpacity",
    "-composite",
    "master_composite_round_512.png"
], check=True)
print("✓ Built square and round master composites")


print("6. Scaling and writing to all density mipmap folders...")
densities = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

for folder, px in densities.items():
    dir_path = os.path.join("app/src/main/res", folder)
    os.makedirs(dir_path, exist_ok=True)
    
    # Export square legacy launcher icon
    subprocess.run([
        "convert", "master_composite_512.png",
        "-resize", f"{px}x{px}!",
        os.path.join(dir_path, "ic_launcher.png")
    ], check=True)
    
    # Export round legacy launcher icon
    subprocess.run([
        "convert", "master_composite_round_512.png",
        "-resize", f"{px}x{px}!",
        os.path.join(dir_path, "ic_launcher_round.png")
    ], check=True)

print("✓ Updated all mipmap density folder PNG files")


print("7. Cleaning up temporary build artifacts...")
temp_files = ["temp_foreground_clean.svg", "raw_fg.png", "trimmed_fg.png", "scaled_fg.png", "master_composite_512.png", "master_composite_round_512.png"]
for tf in temp_files:
    if os.path.exists(tf):
        os.remove(tf)
        
print("★ SUCCESS! The launcher icons have been generated perfectly with perfect proportions, zero filters (no black/white background bugs), and perfectly centered backgrounds!")
