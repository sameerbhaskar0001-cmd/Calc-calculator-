import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    text = f.read()

text = text.replace("java.lang.Math.pow", "Math.pow")
text = text.replace("java.lang.Math.log10", "Math.log10")

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(text)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    text = f.read()

text = text.replace("java.lang.Math.pow", "Math.pow")
text = text.replace("java.lang.Math.log10", "Math.log10")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(text)

