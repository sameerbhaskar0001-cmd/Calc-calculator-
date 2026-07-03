with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

content = content.replace("modifier: Modifier\n", "modifier: Modifier = Modifier\n")
content = content.replace("modifier: Modifier,", "modifier: Modifier = Modifier,")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
