import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"(val keys = listOf\(\n\s+listOf\(\"C\", \" \", \"⌫\"\),\n\s+listOf\(\"7\", \"8\", \"9\"\),\n\s+listOf\(\"4\", \"5\", \"6\"\),\n\s+listOf\(\"1\", \"2\", \"3\"\),\n\s+listOf\(\"\.\", \"0\", \"00\"\)\n\s+\)),\n\s+listOf\(\"4\", \"5\", \"6\", \"C\"\),\n\s+listOf\(\"1\", \"2\", \"3\", \" \"\),\n\s+listOf\(\" \", \"0\", \"\.\", \" \"\)\n\s+\)"

replacement = r"\1"

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
