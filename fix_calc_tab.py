import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

pattern = r"(// Upper Display Zone\s+Column\(\s+modifier = Modifier\s+\.fillMaxWidth\(\)\s+\.weight\(1f\)\s+\.padding\(vertical = 8\.dp\),\s+verticalArrangement = Arrangement\.Bottom\s+\)\s+\{)(.*?)(Spacer\(modifier = Modifier\.height\(8\.dp\)\)\s+// Conversion container)"

def replacement(match):
    start = match.group(1)
    inner = match.group(2)
    end = match.group(3)
    # wrap inner in a weight(1f) column
    new_inner = "\n            Column(\n                modifier = Modifier.fillMaxWidth().weight(1f),\n                verticalArrangement = Arrangement.Bottom\n            ) {" + inner + "            }\n            "
    return start + new_inner + end

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
