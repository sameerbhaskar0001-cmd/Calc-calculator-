import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    content = f.read()

pattern = r"(else -> \{ // Digits 0-9\s+)if \(currentInput == \"0\" && key == \"0\"\) return(\s+)val newValue = if \(currentInput == \"0\"\) key else currentInput \+ key"

replacement = r"\1if (currentInput == \"0\" && (key == \"0\" || key == \"00\")) return\2val newValue = if (currentInput == \"0\" && key != \"00\") key else if (currentInput == \"0\" && key == \"00\") \"0\" else currentInput + key"

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(content)
