import os
import subprocess

# Paths to output drawable directory
drawable_dir = "app/src/main/res/drawable"
os.makedirs(drawable_dir, exist_ok=True)

print("1. Preparing and cleaning SVG assets to prevent rendering bugs...")
# Read raw icon_foreground.svg and strip filters that cause standard SVG renderers to output empty canvases
if os.path.exists("icon_foreground.svg"):
    with open("icon_foreground.svg", "r") as f:
        svg_content = f.read()
    
    # Strip drop shadow filter calls
    clean_svg = svg_content.replace('filter="url(#shadow)"', '').replace('filter="url(#keyShadow)"', '')
    
    with open("temp_foreground_clean.svg", "w") as f:
        f.write(clean_svg)
    print("✓ Created clean temporary SVG vector: temp_foreground_clean.svg")
else:
    raise FileNotFoundError("Missing icon_foreground.svg in workspace root!")

print("2. Rendering cleaned SVGs to high-resolution transparent PNG drawables...")
# Compile clean SVG foreground to 512x512 transparent PNG
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "temp_foreground_clean.svg",
    "-o", os.path.join(drawable_dir, "icon_foreground.png")
], check=True)

# Also create ic_launcher_foreground.png
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "temp_foreground_clean.svg",
    "-o", os.path.join(drawable_dir, "ic_launcher_foreground.png")
], check=True)

# Compile background SVG to 512x512 PNG
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "icon_background.svg",
    "-o", os.path.join(drawable_dir, "icon_background.png")
], check=True)

# Also create ic_launcher_background.png
subprocess.run([
    "rsvg-convert", "-w", "512", "-h", "512",
    "icon_background.svg",
    "-o", os.path.join(drawable_dir, "ic_launcher_background.png")
], check=True)

print("✓ Successfully generated all high-resolution PNG drawables with alpha transparency!")

print("3. Compositing adaptive launcher icon within the 66dp (61%) safe zone...")
# Center and scale foreground (312x312 px inside 512x512 px canvas) on top of the background
subprocess.run([
    "convert",
    os.path.join(drawable_dir, "icon_background.png"),
    "(", os.path.join(drawable_dir, "icon_foreground.png"), "-resize", "312x312!", ")",
    "-gravity", "center",
    "-composite",
    "master_composite_512.png"
], check=True)

# Apply a perfect anti-aliased circular mask to generate the round composite version
subprocess.run([
    "convert", "master_composite_512.png",
    "(", "-size", "512x512", "xc:none", "-fill", "white", "-draw", "circle 256,256 256,0", ")",
    "-alpha", "off",
    "-compose", "CopyOpacity",
    "-composite",
    "master_composite_round_512.png"
], check=True)

print("✓ Generated master composites (square & round)!")

print("4. Downsampling and exporting legacy PNG assets for all density folders...")
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

print("✓ Mipmap densities updated!")

# Clean up temporary SVG file
if os.path.exists("temp_foreground_clean.svg"):
    os.remove("temp_foreground_clean.svg")

print("★ Success! Every single launcher icon in the app has been successfully rebuilt from source!")
