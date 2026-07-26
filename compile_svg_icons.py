import os
import subprocess

drawable_dir = "app/src/main/res/drawable"
os.makedirs(drawable_dir, exist_ok=True)

print("1. Compiling uncorrupted SVGs to high-resolution PNGs...")

# Render icon_background.svg to PNGs in drawable (512x512)
subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_background.svg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "ic_launcher_background.png")
], check=True)

subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_background.svg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "icon_background.png")
], check=True)

# Render icon_foreground.svg to PNGs in drawable (512x512)
subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_foreground.svg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "ic_launcher_foreground.png")
], check=True)

subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_foreground.svg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "icon_foreground.png")
], check=True)

subprocess.run([
    "ffmpeg", "-y",
    "-i", "icon_foreground.svg",
    "-vf", "scale=512:512",
    os.path.join(drawable_dir, "ic_launcher_foreground_user.png")
], check=True)

print("2. Generating composited launcher PNG (512x512)...")
# Scale foreground artwork to 312x312 (61% of 512px canvas for 66dp/108dp safe zone)
subprocess.run([
    "ffmpeg", "-y",
    "-i", os.path.join(drawable_dir, "ic_launcher_background.png"),
    "-i", os.path.join(drawable_dir, "ic_launcher_foreground.png"),
    "-filter_complex", "[1:v]scale=312:312[fg];[0:v][fg]overlay=100:100",
    "master_composite_512.png"
], check=True)

# Create circle mask SVG and PNG
circle_mask_svg = '''<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <circle cx="256" cy="256" r="256" fill="#FFFFFF"/>
</svg>'''
with open("circle_mask.svg", "w") as f:
    f.write(circle_mask_svg)

subprocess.run(["ffmpeg", "-y", "-i", "circle_mask.svg", "circle_mask.png"], check=True)

# Merge circle mask to create round composite
subprocess.run([
    "ffmpeg", "-y",
    "-i", "master_composite_512.png",
    "-i", "circle_mask.png",
    "-filter_complex", "[0:v][1:v]alphamerge",
    "master_composite_round_512.png"
], check=True)

print("3. Generating all mipmap density PNG files to overwrite any legacy robot heads...")
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
    
    # Generate ic_launcher.png
    out_ic = os.path.join(dir_path, "ic_launcher.png")
    subprocess.run(["ffmpeg", "-y", "-i", "master_composite_512.png", "-vf", f"scale={px}:{px}", out_ic], check=True)
    
    # Generate ic_launcher_round.png
    out_ic_round = os.path.join(dir_path, "ic_launcher_round.png")
    subprocess.run(["ffmpeg", "-y", "-i", "master_composite_round_512.png", "-vf", f"scale={px}:{px}", out_ic_round], check=True)

print("Successfully compiled and updated all custom launcher icons and densities!")
