package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ui.theme.MyApplicationTheme
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.WindowManager

class MainActivity : FragmentActivity() {
  private lateinit var viewModel: CalculatorViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Reset any previously modified dynamic launcher icon component settings to restore default icon
    try {
        val pm = packageManager
        pm.setComponentEnabledSetting(
            android.content.ComponentName(this, MainActivity::class.java),
            android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            android.content.pm.PackageManager.DONT_KILL_APP
        )

        val aliases = listOf(
            "com.example.LauncherClassic",
            "com.example.LauncherRetro",
            "com.example.LauncherNeon",
            "com.example.LauncherNotes",
            "com.example.LauncherWeather",
            "com.example.LauncherCompass",
            "com.example.LauncherSudoku",
            "com.example.LauncherVoice"
        )
        for (alias in aliases) {
            try {
                pm.setComponentEnabledSetting(
                    android.content.ComponentName(this, alias),
                    android.content.pm.PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    android.content.pm.PackageManager.DONT_KILL_APP
                )
            } catch (e: Exception) {
                // Ignore if not present
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

    lifecycleScope.launch {
      viewModel.preventScreenshots.collectLatest { prevent ->
        if (prevent) {
          window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
          window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
      }
    }

    setContent {
      val selectedTheme by viewModel.selectedTheme.collectAsState()
      MyApplicationTheme(theme = selectedTheme) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
          CalculatorScreen(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding)
          )
        }
      }
    }
  }

  override fun onStop() {
    super.onStop()
    if (!isChangingConfigurations) {
      viewModel.lockVault()
    }
  }
}
