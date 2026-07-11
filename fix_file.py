import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.read().split("\n")

out_lines = []
skip = 0
for i, line in enumerate(lines):
    if skip > 0:
        skip -= 1
        continue
    
    if "var showColorOptions by remember" in line:
        continue
    if "var showBgColorOptions by remember" in line:
        continue
    if "showColorOptions =" in line:
        continue
    if "showBgColorOptions =" in line:
        continue
        
    if "IconButton(" in line:
        # peek ahead
        block = "\n".join(lines[i:i+20])
        if "Icon(Icons.Default.FormatColorText" in block:
            # find the closing brace of the IconButton
            # We know it ends with }
            # Let's just find the index of Icon(Icons.Default.FormatColorText
            # and count braces. Or just skip exactly 13 lines.
            skip = 13
            # Check if previous line was a divider and remove it
            if "Box(modifier = Modifier.width(1.dp).height(20.dp).background(Color.White.copy(alpha = 0.1f)))" in out_lines[-1]:
                out_lines.pop()
            continue
        if "Icon(Icons.Default.Brush" in block:
            skip = 13
            continue
            
    if "AnimatedVisibility(visible = showColorOptions)" in line:
        # Skip until the end of this block
        # AnimatedVisibility block ends at line 5067
        # 5034 to 5067 is 33 lines. Let's count braces to be safe.
        braces = 0
        started = False
        for j in range(i, len(lines)):
            braces += lines[j].count("{") - lines[j].count("}")
            if "{" in lines[j]:
                started = True
            if started and braces <= 0:
                skip = j - i
                break
        continue
        
    if "AnimatedVisibility(visible = showBgColorOptions)" in line:
        braces = 0
        started = False
        for j in range(i, len(lines)):
            braces += lines[j].count("{") - lines[j].count("}")
            if "{" in lines[j]:
                started = True
            if started and braces <= 0:
                skip = j - i
                break
        continue

    out_lines.append(line)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write("\n".join(out_lines))
