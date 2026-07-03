import sys

def rewrite():
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "r") as f:
        lines = f.readlines()

    out = []
    i = 0
    while i < len(lines):
        line = lines[i]
        if line.strip() == "fun VaultTabContent(":
            # Go back to @Composable
            if out[-1].strip() == "@Composable":
                out.pop()
            break
        out.append(line)
        i += 1

    # Now we are at VaultTabContent
    # Skip until the end of VaultTabContent
    # How to find the end? It's line 7257. Let's find "@Composable\nfun EmptyVaultSectionState("
    
    j = i
    while j < len(lines):
        if "fun EmptyVaultSectionState(" in lines[j]:
            break
        j += 1
    
    # Extract the whole VaultTabContent from i to j-1
    # Actually wait, we already know what's in there. We can just use Python to find the parts.
    content_lines = lines[i-1:j-1] if lines[i-1].strip() == '@Composable' else lines[i:j-1]
    
    # We will write the new functions.
    
    new_code = """
@Composable
fun VaultTabContent(
    viewModel: CalculatorViewModel,
    onLockExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    if (!vaultUnlocked) {
        VaultTabLockedContent(viewModel, onLockExit, modifier)
    } else {
        VaultTabUnlockedContent(viewModel, onLockExit, modifier)
    }
}

@Composable
fun VaultTabLockedContent(
    viewModel: CalculatorViewModel,
    onLockExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vaultUnlocked by viewModel.vaultUnlocked.collectAsState()
    var pinInput by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val activity = context as? androidx.fragment.app.FragmentActivity

    fun triggerBiometric() {
        if (activity != null && biometricEnabled) {
            val executor = androidx.core.content.ContextCompat.getMainExecutor(activity)
            val biometricPrompt = androidx.biometric.BiometricPrompt(
                activity,
                executor,
                object : androidx.biometric.BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                    }

                    override fun onAuthenticationSucceeded(result: androidx.biometric.BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        viewModel.unlockVault(isDecoy = false)
                        android.widget.Toast.makeText(context, "Vault Unlocked via Biometrics!", android.widget.Toast.LENGTH_SHORT).show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                    }
                }
            )

            val promptInfo = androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Vault")
                .setSubtitle("Authenticate using fingerprint or face recognition")
                .setNegativeButtonText("Use PIN Pad")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }

    if (!vaultUnlocked && biometricEnabled) {
        LaunchedEffect(Unit) {
            triggerBiometric()
        }
    }
"""
    # Now extract the locked UI
    locked_start = -1
    locked_end = -1
    for k, l in enumerate(content_lines):
        if "if (!vaultUnlocked) {" in l:
            locked_start = k + 1
        if "} else {" in l and locked_start != -1:
            locked_end = k
            break
            
    for k in range(locked_start, locked_end):
        new_code += content_lines[k]
        
    new_code += "}\n\n@Composable\nfun VaultTabUnlockedContent(\n    viewModel: CalculatorViewModel,\n    onLockExit: () -> Unit,\n    modifier: Modifier = Modifier\n) {\n"
    
    # Now add the variables for unlocked content
    # Basically from line 2258 to 2352 (before triggerBiometric)
    # But wait, let's just find them by slicing
    var_start = -1
    var_end = -1
    for k, l in enumerate(content_lines):
        if "val vaultNotes by viewModel.vaultNotes.collectAsState()" in l:
            var_start = k
        if "fun triggerBiometric() {" in l:
            var_end = k
            break
            
    for k in range(var_start, var_end):
        new_code += content_lines[k]
        
    # Then add the unified sensor detector for panic
    # Which is between triggerBiometric and if (!vaultUnlocked)
    sensor_start = -1
    for k, l in enumerate(content_lines):
        if "// Unified sensor detector for Panic Gesture" in l:
            sensor_start = k
            break
            
    sensor_end = locked_start - 1
    if sensor_start != -1:
        for k in range(sensor_start, sensor_end):
            new_code += content_lines[k]
            
    # Then add the unlocked UI content
    unlocked_start = locked_end + 1
    unlocked_end = len(content_lines) - 2 # skip the closing brace of VaultTabContent
    
    for k in range(unlocked_start, unlocked_end):
        new_code += content_lines[k]
        
    new_code += "}\n\n"
    
    out.append(new_code)
    
    # Append the rest
    j -= 1 # to include @Composable
    while j < len(lines):
        out.append(lines[j])
        j += 1
        
    with open("app/src/main/java/com/example/CalculatorScreen.kt", "w") as f:
        f.writelines(out)

rewrite()
