import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    content = f.read()

content = content.replace("android.media.MediaPlayer(context)", "android.media.MediaPlayer(context.applicationContext)")

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
    f.write(content)
