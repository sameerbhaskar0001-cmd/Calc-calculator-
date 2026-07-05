import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

old_delays = """            transitionState = 1 // Authenticating
            delay(3000)
            transitionState = 2 // Transition
            delay(300)"""

new_delays = """            transitionState = 1 // Authenticating
            delay(2000)
            transitionState = 2 // Welcome Transition
            delay(3000)"""

content = content.replace(old_delays, new_delays)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
