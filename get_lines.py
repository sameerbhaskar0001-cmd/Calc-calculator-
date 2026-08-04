with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start_idx = -1
end_idx = -1

for i, line in enumerate(lines):
    if "var playbackPosition by remember { mutableStateOf(0) }" in line:
        start_idx = i
    if "if (showVolumeIndicator) {" in line and start_idx != -1:
        end_idx = i + 12 # It has a few more lines to close

print(start_idx, end_idx)
for i in range(end_idx - 5, end_idx + 5):
    print(i, repr(lines[i]))
