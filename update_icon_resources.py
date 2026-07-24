import os
import subprocess

# 1. Ensure drawable dir exists
drawable_dir = "app/src/main/res/drawable"
os.makedirs(drawable_dir, exist_ok=True)

# 2. Source images:
# - icon_background.jpg (or bg image) -> app/src/main/res/drawable/icon_background.png
# - icon_foreground.jpg (or ic_launcher_foreground.png) -> app/src/main/res/drawable/icon_foreground.png

# Convert icon_background.jpg to 512x512 PNG in drawable
subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_background.jpg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "icon_background.png")
], check=True)

# Convert ic_launcher_foreground.png / icon_foreground.jpg to 1024x1024 PNG in drawable
# (using ic_launcher_foreground.png as it is the 2048x2048 RGBA original)
# We use ImageMagick's convert to dynamically detect and remove the white/off-white background
# via floodfill starting from the top-left corner, ensuring perfect transparency.
src_fg = "ic_launcher_foreground.png" if os.path.exists("ic_launcher_foreground.png") else "icon_foreground.jpg"
subprocess.run([
    "convert", src_fg,
    "-resize", "1024x1024!",
    "-alpha", "set",
    "-bordercolor", "white",
    "-border", "1x1",
    "-fuzz", "12%",
    "-fill", "none",
    "-draw", "matte 0,0 floodfill",
    "-shave", "1x1",
    os.path.join(drawable_dir, "icon_foreground.png")
], check=True)

print("Created drawable/icon_background.png and drawable/icon_foreground.png")

# 3. Create composite 512x512 PNGs for raster mipmaps:
# Scale foreground artwork to 312x312 (approx 61% of 512px canvas, matching 66dp/108dp safe zone)
subprocess.run([
    "ffmpeg", "-y",
    "-i", os.path.join(drawable_dir, "icon_background.png"),
    "-i", os.path.join(drawable_dir, "icon_foreground.png"),
    "-filter_complex", "[1:v]scale=312:312[fg];[0:v][fg]overlay=100:100",
    "master_composite_512.png"
], check=True)

# Round composite with circle mask
circle_mask_svg = '''<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <circle cx="256" cy="256" r="256" fill="#FFFFFF"/>
</svg>'''
with open("circle_mask.svg", "w") as f:
    f.write(circle_mask_svg)

subprocess.run(["ffmpeg", "-y", "-i", "circle_mask.svg", "circle_mask.png"], check=True)

subprocess.run([
    "ffmpeg", "-y",
    "-i", "master_composite_512.png",
    "-i", "circle_mask.png",
    "-filter_complex", "[0:v][1:v]alphamerge",
    "master_composite_round_512.png"
], check=True)

# 4. Generate all mipmap densities
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
    
    out_ic = os.path.join(dir_path, "ic_launcher.png")
    subprocess.run(["ffmpeg", "-y", "-i", "master_composite_512.png", "-vf", f"scale={px}:{px}", out_ic], check=True)
    
    out_ic_round = os.path.join(dir_path, "ic_launcher_round.png")
    subprocess.run(["ffmpeg", "-y", "-i", "master_composite_round_512.png", "-vf", f"scale={px}:{px}", out_ic_round], check=True)

print("Updated all raster mipmap density PNG files!")
