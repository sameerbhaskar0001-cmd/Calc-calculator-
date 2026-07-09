import re

with open('app/src/main/java/com/example/CalculatorScreen.kt', 'r') as f:
    code = f.read()

old_block = """        val deleteSenderLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                android.util.Log.d("Vault", "User accepted/cancelled: User accepted")
                android.util.Log.d("Vault", "Delete success/failure: Delete success")
                android.widget.Toast.makeText(context, "Original photo hidden successfully!", android.widget.Toast.LENGTH_SHORT).show()
                viewModel.onOriginalFileDeleted(context)
            } else {
                android.util.Log.d("Vault", "User accepted/cancelled: User cancelled")
                android.util.Log.d("Vault", "Delete success/failure: Delete failure")
            }
            viewModel.clearPendingDelete()
        }"""

new_block = """        val deleteSenderLauncher = rememberLauncherForActivityResult(
            contract = androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == android.app.Activity.RESULT_OK) {
                android.util.Log.d("Vault", "User accepted/cancelled: User accepted")
                android.util.Log.d("Vault", "Delete success/failure: Delete success")
                android.widget.Toast.makeText(context, "Original photo hidden successfully!", android.widget.Toast.LENGTH_SHORT).show()
            } else {
                android.util.Log.d("Vault", "User accepted/cancelled: User cancelled")
                android.util.Log.d("Vault", "Delete success/failure: Delete failure")
                android.widget.Toast.makeText(context, "File secured in vault, but original was not deleted from gallery.", android.widget.Toast.LENGTH_LONG).show()
            }
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

