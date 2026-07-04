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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import com.example.ui.theme.MyApplicationTheme
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.view.WindowManager

class MainAppActivity : FragmentActivity() {
  private var viewModel: CalculatorViewModel? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    try {
        viewModel = ViewModelProvider(this)[CalculatorViewModel::class.java]

        lifecycleScope.launch {
          viewModel?.preventScreenshots?.collectLatest { prevent ->
            if (prevent) {
              window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            } else {
              window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
          }
        }

        setContent {
          val selectedTheme by (viewModel?.selectedTheme ?: kotlinx.coroutines.flow.MutableStateFlow(com.example.ui.theme.AppTheme.GRAPHITE)).collectAsState()
          MyApplicationTheme(theme = selectedTheme) {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
              if (viewModel != null) {
                  CalculatorScreen(
                    viewModel = viewModel!!,
                    modifier = Modifier.padding(innerPadding)
                  )
              }
            }
          }
        }
    } catch (e: Throwable) {
        e.printStackTrace()
        setContent {
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "CRASH DETECTED:\n${e.message}\n\n${e.stackTraceToString()}",
                    color = Color.Red
                )
            }
        }
    }
  }

  override fun onStop() {
    super.onStop()
    if (!isChangingConfigurations) {
      try {
          viewModel?.lockVault()
      } catch (e: Exception) {
          e.printStackTrace()
      }
    }
  }
}
