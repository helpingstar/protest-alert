package io.github.helpingstar.protest_alert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.github.helpingstar.protest_alert.core.data.util.NetworkMonitor
import io.github.helpingstar.protest_alert.ui.PaApp
import io.github.helpingstar.protest_alert.ui.rememberPaAppState
import io.github.helpingstar.protest_alert.ui.theme.ProtestAlertTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
//
//    @Inject
//    lateinit var networkMonitor: NetworkMonitor

    private val viewModel: MainActivityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val appState = rememberPaAppState(
                networkMonitor = networkMonitor,
            )
            PaApp(appState)
        }
    }
}
