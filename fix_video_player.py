with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    lines = f.readlines()

start_idx = 7853 - 1
end_idx = 8006 - 1

block = "".join(lines[start_idx:end_idx+1])

# Replace with the function call
replacement = "                                        VideoPlayerWithControls(path = path, context = context)\n"

new_lines = lines[:start_idx] + [replacement] + lines[end_idx+1:]

composable = """
@Composable
fun VideoPlayerWithControls(path: String, context: android.content.Context) {
""" + block + """
}
"""

# Fix indentation inside the composable
composable_lines = composable.split("\n")
fixed_lines = []
for line in composable_lines:
    if line.startswith("                                        "):
        fixed_lines.append(line[40:])
    else:
        fixed_lines.append(line)

new_content = "".join(new_lines) + "\n".join(fixed_lines) + "\n"

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(new_content)

