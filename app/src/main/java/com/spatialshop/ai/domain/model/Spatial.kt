package com.spatialshop.ai.domain.model

/**
 * Fit status result from the Spatial Fit Engine.
 */
enum class FitStatus {
    FITS,                    // Product fits with good clearance
    FITS_WITH_RESTRICTIONS,  // Fits but tight spaces
    DOES_NOT_FIT,           // Product exceeds available space
    INSUFFICIENT_DATA       // Can't determine - need more scanning
}

/**
 * Result of a spatial fit analysis.
 */
data class FitResult(
    val status: FitStatus,
    val clearanceLeft: Float? = null,
    val clearanceRight: Float? = null,
    val clearanceFront: Float? = null,
    val clearanceBack: Float? = null,
    val heightClearance: Float? = null,
    val conflicts: List<SpatialConflict> = emptyList(),
    val confidence: Float = 0f,
    val recommendations: List<String> = emptyList()
)

/**
 * A spatial conflict detected during fit analysis.
 */
data class SpatialConflict(
    val type: ConflictType,
    val description: String,
    val severity: ConflictSeverity
)

enum class ConflictType {
    BOUNDARY_EXCEEDED,
    HEIGHT_EXCEEDED,
    COLLISION_DETECTED,
    CLEARANCE_INSUFFICIENT,
    OBSTACLE_NEARBY
}

enum class ConflictSeverity {
    LOW,
    MEDIUM,
    HIGH
}

/**
 * Saved design with product placements in a room.
 */
data class SavedDesign(
    val id: String,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long,
    val placements: List<ProductPlacement>,
    val thumbnailPath: String? = null
)

/**
 * A product placement in 3D space.
 */
data class ProductPlacement(
    val productId: String,
    val positionX: Float,
    val positionY: Float,
    val positionZ: Float,
    val rotationY: Float,
    val fitResult: FitResult? = null
)
