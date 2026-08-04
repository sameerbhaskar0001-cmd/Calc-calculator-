with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()
print("Total braces:", content.count('{') - content.count('}'))
