with open("app/src/main/java/com/example/CalculatorScreen.kt") as f:
    text = f.read()

# I will just write a regex to clean up the end of the filter function
import re

text = re.sub(
r"return TransformedText\(transformedAnnotatedString, offsetMapping\)\n\s*\}\n\s*\} catch \(e: Exception\)",
r"return TransformedText(transformedAnnotatedString, offsetMapping)\n        } catch (e: Exception)",
text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)
print("done")
