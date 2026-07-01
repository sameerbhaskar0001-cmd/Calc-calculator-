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
      MyApplicationTheme {
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
