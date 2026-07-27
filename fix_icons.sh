#!/bin/bash
set -e

# 1. Create padded foreground (safe zone is 66/108 = 61% of canvas size)
# 500 * 0.6111 = 305
convert icon_foreground.png -resize 305x305 -background none -gravity center -extent 500x500 padded_foreground.png

# 2. Update the drawable files
cp icon_background.png app/src/main/res/drawable/ic_launcher_background.png
cp padded_foreground.png app/src/main/res/drawable/ic_launcher_foreground.png
cp padded_foreground.png ic_launcher_foreground_user.png

# 3. Create the composite for legacy icons
convert icon_background.png padded_foreground.png -gravity center -composite composite_launcher.png

# 4. Create round composite using a proper circular mask
# We'll use SVG for a smooth circle mask and convert it, or just use IM draw
convert -size 500x500 xc:none -fill white -draw "circle 250,250 250,0" mask.png
convert composite_launcher.png mask.png -compose dst-in -composite composite_launcher_round.png

# 5. Generate mipmaps
sizes=("mipmap-mdpi:48" "mipmap-hdpi:72" "mipmap-xhdpi:96" "mipmap-xxhdpi:144" "mipmap-xxxhdpi:192")

for entry in "${sizes[@]}"; do
  dir="${entry%%:*}"
  px="${entry##*:}"
  mkdir -p "app/src/main/res/$dir"
  convert composite_launcher.png -resize "${px}x${px}" "app/src/main/res/$dir/ic_launcher.png"
  convert composite_launcher_round.png -resize "${px}x${px}" "app/src/main/res/$dir/ic_launcher_round.png"
done

echo "Icons fixed successfully!"
