import os
import subprocess

densities = {
    "mipmap-mdpi": 48,
    "mipmap-hdpi": 72,
    "mipmap-xhdpi": 96,
    "mipmap-xxhdpi": 144,
    "mipmap-xxxhdpi": 192
}

# First create a merged composite 512x512 PNG (background + centered foreground artwork)
# Foreground artwork scaled to 61% (312px) inside 512px canvas for 66dp safe zone
fg_scaled_cmd = [
    "ffmpeg", "-y",
    "-i", "ic_launcher_background.png",
    "-i", "ic_launcher_foreground_user.png",
    "-filter_complex", "[1:v]scale=312:312[fg];[0:v][fg]overlay=100:100",
    "composite_launcher_512.png"
]
subprocess.run(fg_scaled_cmd, check=True)

# Also create round composite with circle crop mask
circle_mask_svg = '''<svg width="512" height="512" viewBox="0 0 512 512" xmlns="http://www.w3.org/2000/svg">
  <circle cx="256" cy="256" r="256" fill="#FFFFFF"/>
</svg>'''
with open("circle_mask.svg", "w") as f:
    f.write(circle_mask_svg)

subprocess.run(["ffmpeg", "-y", "-i", "circle_mask.svg", "circle_mask.png"], check=True)

round_composite_cmd = [
    "ffmpeg", "-y",
    "-i", "composite_launcher_512.png",
    "-i", "circle_mask.png",
    "-filter_complex", "[0:v][1:v]alphamerge",
    "composite_launcher_round_512.png"
]
subprocess.run(round_composite_cmd, check=True)

for folder, px in densities.items():
    dir_path = os.path.join("app/src/main/res", folder)
    os.makedirs(dir_path, exist_ok=True)
    
    # Generate ic_launcher.png
    out_ic = os.path.join(dir_path, "ic_launcher.png")
    subprocess.run(["ffmpeg", "-y", "-i", "composite_launcher_512.png", "-vf", f"scale={px}:{px}", out_ic], check=True)
    
    # Generate ic_launcher_round.png
    out_ic_round = os.path.join(dir_path, "ic_launcher_round.png")
    subprocess.run(["ffmpeg", "-y", "-i", "composite_launcher_round_512.png", "-vf", f"scale={px}:{px}", out_ic_round], check=True)

print("Generated all mipmaps successfully!")
