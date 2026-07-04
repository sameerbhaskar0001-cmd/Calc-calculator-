import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

bad = """        )
    }

@Composable
fun BottomNavItem("""

good = """        )
    }
}

@Composable
fun BottomNavItem("""

content = content.replace(bad, good)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)
