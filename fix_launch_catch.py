import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    code = f.read()

old_block = """                try {
                    android.util.Log.d("Vault", "IntentSender launched")
                    deleteSenderLauncher.launch(
                        androidx.activity.result.IntentSenderRequest.Builder(sender).build()
                    )
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Any exception with full stack trace", e)
                    viewModel.clearPendingDelete()
                }"""

new_block = """                try {
                    android.util.Log.d("Vault", "IntentSender launched")
                    deleteSenderLauncher.launch(
                        androidx.activity.result.IntentSenderRequest.Builder(sender).build()
                    )
                } catch (e: Exception) {
                    android.util.Log.e("Vault", "Any exception with full stack trace", e)
                    viewModel.onOriginalFileDeleted(context)
                    viewModel.clearPendingDelete()
                }"""

if old_block in code:
    code = code.replace(old_block, new_block)
    with open('app/src/main/java/com/example/CalculatorScreen.kt', 'w') as f:
        f.write(code)
    print("Replaced successfully")
else:
    print("Not found")

