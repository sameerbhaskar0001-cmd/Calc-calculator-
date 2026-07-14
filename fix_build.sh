sed -i 's/import androidx.compose.runtime.collectAsState//g' app/src/main/java/com/example/CalculatorScreen.kt
sed -i 's/val storageInfo by viewModel.storageInfo.collectAsState()/val storageInfo by viewModel.storageInfo.collectAsStateWithLifecycle()/g' app/src/main/java/com/example/CalculatorScreen.kt
