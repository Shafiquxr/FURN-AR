package com.spatialshop.ai.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Shape system for Spatial Shopping AI.
 * Rounded corners matching the UI reference design.
 */
val SpatialShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp)
)

// Custom shapes for specific UI components
object CustomShapes {
    val ProductCard = RoundedCornerShape(16.dp)
    val CategoryChip = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(12.dp)
    val BottomSheet = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    val SearchBar = RoundedCornerShape(12.dp)
    val DealsBanner = RoundedCornerShape(20.dp)
    val ImageCard = RoundedCornerShape(12.dp)
    val DimensionCard = RoundedCornerShape(12.dp)
    val ColorSwatch = RoundedCornerShape(50) // Circle
    val FilterButton = RoundedCornerShape(12.dp)
    val CollectionCard = RoundedCornerShape(16.dp)
    val ARControlButton = RoundedCornerShape(50) // Circle
}
