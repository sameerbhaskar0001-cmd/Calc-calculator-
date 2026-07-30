import re

with open("app/src/main/java/com/example/MainAppActivity.kt", "r") as f:
    content = f.read()

target = """  override fun onStop() {"""
replacement = """  override fun onPause() {
      super.onPause()
      if (!isChangingConfigurations && viewModel?.isPickingFile != true) {
          viewModel?.onAppBackgrounded()
      }
  }
  
  override fun onStop() {"""
content = content.replace(target, replacement)

with open("app/src/main/java/com/example/MainAppActivity.kt", "w") as f:
    f.write(content)
