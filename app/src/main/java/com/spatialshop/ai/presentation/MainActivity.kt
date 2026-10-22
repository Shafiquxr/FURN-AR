package com.spatialshop.ai.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.spatialshop.ai.presentation.navigation.SpatialShopNavigation
import com.spatialshop.ai.presentation.theme.SpatialShoppingTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for Spatial Shopping AI.
 * Hosts the Compose navigation and handles edge-to-edge display.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            SpatialShoppingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SpatialShopNavigation()
                }
            }
        }
    }
}
