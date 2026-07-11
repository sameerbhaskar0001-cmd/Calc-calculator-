import re

with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

start_idx = text.find("override fun filter(text: AnnotatedString): TransformedText {")
end_idx = text.find("private fun applyStyleRangeLocal(builder: AnnotatedString.Builder")

if start_idx != -1 and end_idx != -1:
    filter_body = text[start_idx:end_idx]
    
    # We want to replace everything inside filter with a try-catch.
    # We can just split it by lines and insert try { and catch { }
    lines = filter_body.split("\n")
    
    new_lines = [lines[0], "        try {"]
    for line in lines[1:-2]: # skip last two lines (return and })
        new_lines.append("        " + line)
        
    # Now find where the return TransformedText is
    # We will modify offset mapping to have try-catch inside it too
    
    new_body = "\n".join(new_lines) + """
        } catch (e: Exception) {
            e.printStackTrace()
            return TransformedText(
                androidx.compose.ui.text.AnnotatedString(text.text), 
                androidx.compose.ui.text.input.OffsetMapping.Identity
            )
        }
    }
    """
    
    text = text[:start_idx] + new_body + "\n    " + text[end_idx:]
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.write(text)
    print("Replaced filter with try-catch")
else:
    print("Could not find filter function")
