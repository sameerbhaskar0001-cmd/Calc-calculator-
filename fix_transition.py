import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    content = f.read()

# Update text
content = content.replace('text = "Authenticating...",', 'text = "Unlocking Secure Vault...",')

# Update delays
old_delays = """            transitionState = 1 // Authenticating
            delay(300)
            transitionState = 2 // Transition
            delay(300)"""

new_delays = """            transitionState = 1 // Authenticating
            delay(2000)
            transitionState = 2 // Transition
            delay(1500)"""

content = content.replace(old_delays, new_delays)

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
    f.write(content)
