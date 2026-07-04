import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# 1. Remove verticalScroll
content = content.replace("                .verticalScroll(rememberScrollState())\n", "")

# 2. Update the Cards container in ExchangeTabContent
# We need to find the Box containing the Cards and Swap Button and change it to be weight-based
pattern = r"(// Cards container with reduced gap and swap button\s+Box\(\s+modifier = Modifier\.fillMaxWidth\(\),)(.*?)(// SWAP BUTTON IN MIDDLE OVERLAYING THE CARDS)"

def replacement(match):
    start = match.group(1).replace("fillMaxWidth()", "fillMaxWidth().weight(1f)")
    inner = match.group(2)
    end = match.group(3)
    
    # Inside inner, change height(130.dp) to weight(1f)
    inner = inner.replace(".height(130.dp)", ".weight(1f)")
    
    # Make sure the Column inside Box fills max size
    inner = inner.replace("Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {", "Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {")
    
    return start + inner + end

content = re.sub(pattern, replacement, content, flags=re.DOTALL)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
