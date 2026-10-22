package com.spatialshop.ai.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Material 3 color scheme for Spatial Shopping AI.
 * Light theme with Mustard Yellow accent matching UI references.
 */
private val SpatialLightColorScheme = lightColorScheme(
    primary = SpatialColors.Primary,
    onPrimary = SpatialColors.OnPrimary,
    primaryContainer = SpatialColors.PrimaryContainer,
    onPrimaryContainer = SpatialColors.TextPrimary,
    secondary = SpatialColors.PrimaryDark,
    onSecondary = SpatialColors.OnPrimary,
    secondaryContainer = SpatialColors.PrimaryLight,
    onSecondaryContainer = SpatialColors.TextPrimary,
    tertiary = SpatialColors.SuccessGreen,
    onTertiary = SpatialColors.OnPrimary,
    background = SpatialColors.Background,
    onBackground = SpatialColors.TextPrimary,
    surface = SpatialColors.Surface,
    onSurface = SpatialColors.TextPrimary,
    surfaceVariant = SpatialColors.SurfaceVariant,
    onSurfaceVariant = SpatialColors.TextSecondary,
    outline = SpatialColors.Outline,
    outlineVariant = SpatialColors.Divider,
    error = SpatialColors.ErrorRed,
    onError = SpatialColors.OnPrimary,
    scrim = SpatialColors.Scrim
)

/**
 * Spatial Shopping AI Theme.
 * Currently supports light mode only, matching the UI reference.
 */
@Composable
fun SpatialShoppingTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = SpatialLightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SpatialColors.Background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SpatialTypography,
        shapes = SpatialShapes,
        content = content
    )
}
