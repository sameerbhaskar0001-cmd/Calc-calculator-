import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    text = f.read()

text = text.replace("vibrator.vibrate(effect, attributes)", "if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) { vibrator.vibrate(effect, attributes) } else { vibrator.vibrate(effect) }")

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(text)
