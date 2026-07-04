import re

with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
    content = f.read()

# Fix the syntax error from the previous bad replacement.
bad_syntax = """        )
    }
        )
    }
}

@Composable
fun BottomNavItem("""

good_syntax = """        )
    }

@Composable
fun BottomNavItem("""

content = content.replace(bad_syntax, good_syntax)

with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
    f.write(content)

