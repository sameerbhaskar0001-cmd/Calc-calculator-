with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

in_anim = False
brace_count = 0
for i in range(2966, 10086):
    line = lines[i]
    if "AnimatedContent(" in line:
        in_anim = True
    
    if in_anim:
        brace_count += line.count('{')
        brace_count -= line.count('}')
        if brace_count == 0 and '{' in line:
            pass # this is not reliable if they are on same line, wait
        
        # let's just find the closing brace of the when statement and then the closing brace of AnimatedContent
