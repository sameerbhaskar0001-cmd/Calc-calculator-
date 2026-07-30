import re

with open("app/src/main/java/com/example/CalculatorViewModel.kt", "r") as f:
    content = f.read()

if "override fun onCleared" not in content:
    target = "    fun stopAudio() {"
    replacement = """    override fun onCleared() {
        super.onCleared()
        stopAudio()
    }

    fun stopAudio() {"""
    content = content.replace(target, replacement)
    
    with open("app/src/main/java/com/example/CalculatorViewModel.kt", "w") as f:
        f.write(content)
