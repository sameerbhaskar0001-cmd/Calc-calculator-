with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Replace AutoMirrored ArrowForward usage with Default ArrowForward
content = content.replace("Icons.AutoMirrored.Filled.ArrowForward", "Icons.Default.ArrowForward")

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
