import subprocess
print(subprocess.getoutput("convert icon_foreground.png -resize 40x20 -depth 8 txt: | awk -F'[:,]' 'NR>1 {print $4}' | head -n 10"))
