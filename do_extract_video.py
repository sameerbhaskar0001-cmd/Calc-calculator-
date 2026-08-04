import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r'                                        var isDragging by remember \{ mutableStateOf\(false\) \}.*?if \(showVolumeIndicator\) \{.*?\n                                                \}\n                                            \}\n                                        \}'

match = re.search(pattern, content, re.DOTALL)
if match:
    extracted = match.group(0)
    
    # Let's see what variables it uses from outside:
    # `context`, `path`, `audioManager`, `playbackPosition`, `videoViewRef`
    # We can pass `path` and `context`.
    # `audioManager` can be fetched inside. `playbackPosition` and `videoViewRef` can be internal.
    
    # Wait, `playbackPosition` and `videoViewRef` and `audioManager` are defined just above this block!
    # Let's replace the whole thing including those definitions.
    
    pattern_full = r'                                        var playbackPosition by remember \{ mutableStateOf\(0\) \}\n                                        var videoViewRef by remember \{ mutableStateOf<android\.widget\.VideoView\?>\(null\) \}\n.*?' + pattern
    
    match_full = re.search(pattern_full, content, re.DOTALL)
    if match_full:
        full_extracted = match_full.group(0)
        
        replacement = """                                        VideoPlayerWithControls(path = path, context = context)"""
        
        new_content = content.replace(full_extracted, replacement)
        
        composable = """
@Composable
fun VideoPlayerWithControls(path: String, context: android.content.Context) {
""" + full_extracted + """
}
"""
        # Fix indentation in composable
        lines = composable.split('\n')
        fixed_lines = []
        for line in lines:
            if line.startswith("                                        "):
                fixed_lines.append(line[40:])
            else:
                fixed_lines.append(line)
        
        new_content += "\n".join(fixed_lines)
        
        with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
            f.write(new_content)
        print("Replaced and appended.")
    else:
        print("Full block not found.")
else:
    print("Block not found")
