package com.spatialshop.ai.core.fit

import com.spatialshop.ai.domain.model.ConflictSeverity
import com.spatialshop.ai.domain.model.ConflictType
import com.spatialshop.ai.domain.model.FitResult
import com.spatialshop.ai.domain.model.FitStatus
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.SpatialConflict
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Bounds of the scanned physical area in centimeters.
 */
data class SpaceBounds(
    val availableWidthCm: Float,
    val availableDepthCm: Float,
    val availableHeightCm: Float = 240f, // standard ceiling height
    val distanceToLeftObstacleCm: Float,
    val distanceToRightObstacleCm: Float,
    val distanceToFrontObstacleCm: Float,
    val distanceToBackObstacleCm: Float
)

@Singleton
class SpatialFitEngine @Inject constructor() {

    companion object {
        const val MIN_WALKWAY_CLEARANCE_CM = 60f
        const val IDEAL_WALKWAY_CLEARANCE_CM = 90f
        const val MIN_WALL_CLEARANCE_CM = 8f
        const val MIN_FRONT_ACCESS_CLEARANCE_CM = 50f
    }

    /**
     * Analyzes whether a product fits in the scanned physical space
     * with given rotation and architectural standards.
     */
    fun analyzeFit(
        product: Product,
        space: SpaceBounds,
        rotationDegrees: Float = 0f
    ): FitResult {
        val rad = Math.toRadians(rotationDegrees.toDouble())
        val effectiveWidth = (abs(product.widthCm * cos(rad)) + abs(product.depthCm * sin(rad))).toFloat()
        val effectiveDepth = (abs(product.widthCm * sin(rad)) + abs(product.depthCm * cos(rad))).toFloat()

        val conflicts = mutableListOf<SpatialConflict>()
        val recommendations = mutableListOf<String>()

        // 1. Check total dimensional boundaries
        val widthExcess = effectiveWidth - space.availableWidthCm
        val depthExcess = effectiveDepth - space.availableDepthCm
        val heightExcess = product.heightCm.toFloat() - space.availableHeightCm

        if (widthExcess > 0) {
            conflicts.add(
                SpatialConflict(
                    type = ConflictType.BOUNDARY_EXCEEDED,
                    description = "Product exceeds available room width by ${widthExcess.roundToInt()} cm",
                    severity = ConflictSeverity.HIGH
                )
            )
        }

        if (depthExcess > 0) {
            conflicts.add(
                SpatialConflict(
                    type = ConflictType.BOUNDARY_EXCEEDED,
                    description = "Product exceeds available room depth by ${depthExcess.roundToInt()} cm",
                    severity = ConflictSeverity.HIGH
                )
            )
        }

        if (heightExcess > 0) {
            conflicts.add(
                SpatialConflict(
                    type = ConflictType.HEIGHT_EXCEEDED,
                    description = "Product exceeds ceiling clearance by ${heightExcess.roundToInt()} cm",
                    severity = ConflictSeverity.HIGH
                )
            )
        }

        // 2. Check Walkway / Obstacle Clearances
        val clearanceLeft = space.distanceToLeftObstacleCm
        val clearanceRight = space.distanceToRightObstacleCm
        val clearanceFront = space.distanceToFrontObstacleCm
        val clearanceBack = space.distanceToBackObstacleCm

        if (clearanceFront < MIN_FRONT_ACCESS_CLEARANCE_CM) {
            conflicts.add(
                SpatialConflict(
                    type = ConflictType.CLEARANCE_INSUFFICIENT,
                    description = "Front access is tight (${clearanceFront.roundToInt()} cm). Recommended: >= 50 cm for comfortable use.",
                    severity = ConflictSeverity.MEDIUM
                )
            )
        }

        val sideClearance = clearanceLeft + clearanceRight
        if (sideClearance < MIN_WALKWAY_CLEARANCE_CM) {
            conflicts.add(
                SpatialConflict(
                    type = ConflictType.CLEARANCE_INSUFFICIENT,
                    description = "Side walkway clearance is restricted (${sideClearance.roundToInt()} cm).",
                    severity = ConflictSeverity.MEDIUM
                )
            )
        }

        // 3. Generate Intelligent Placement Suggestions
        val alternateRad = Math.toRadians((rotationDegrees + 90.0))
        val altWidth = (abs(product.widthCm * cos(alternateRad)) + abs(product.depthCm * sin(alternateRad))).toFloat()
        val altDepth = (abs(product.widthCm * sin(alternateRad)) + abs(product.depthCm * cos(alternateRad))).toFloat()

        if (effectiveWidth > effectiveDepth && altWidth < effectiveWidth && space.availableWidthCm < effectiveWidth) {
            recommendations.add("Rotate 90° to reduce width footprint from ${effectiveWidth.roundToInt()} cm to ${altWidth.roundToInt()} cm")
        }

        if (clearanceBack < MIN_WALL_CLEARANCE_CM) {
            val pullForward = (MIN_WALL_CLEARANCE_CM - clearanceBack).roundToInt()
            recommendations.add("Pull product forward by $pullForward cm to prevent wall scuffing")
        } else if (clearanceFront < MIN_FRONT_ACCESS_CLEARANCE_CM && clearanceBack > 25f) {
            recommendations.add("Push product back closer to wall to gain front walkway space")
        }

        if (conflicts.isEmpty()) {
            recommendations.add("Optimal placement! Meets architectural clearance guidelines")
        }

        // 4. Determine Overall Fit Status
        val status = when {
            conflicts.any { it.severity == ConflictSeverity.HIGH } -> FitStatus.DOES_NOT_FIT
            conflicts.any { it.severity == ConflictSeverity.MEDIUM } -> FitStatus.FITS_WITH_RESTRICTIONS
            else -> FitStatus.FITS
        }

        val confidence = when (status) {
            FitStatus.FITS -> 0.96f
            FitStatus.FITS_WITH_RESTRICTIONS -> 0.88f
            FitStatus.DOES_NOT_FIT -> 0.99f
            FitStatus.INSUFFICIENT_DATA -> 0.50f
        }

        return FitResult(
            status = status,
            clearanceLeft = clearanceLeft,
            clearanceRight = clearanceRight,
            clearanceFront = clearanceFront,
            clearanceBack = clearanceBack,
            heightClearance = space.availableHeightCm - product.heightCm.toFloat(),
            conflicts = conflicts,
            confidence = confidence,
            recommendations = recommendations
        )
    }

    /**
     * Determines the optimal rotation (0°, 90°, 180°, 270°) for maximum walkway clearance.
     */
    fun findOptimalRotation(product: Product, space: SpaceBounds): Float {
        val angles = listOf(0f, 90f, 180f, 270f)
        return angles.maxByOrNull { angle ->
            val result = analyzeFit(product, space, angle)
            when (result.status) {
                FitStatus.FITS -> 1000f + (result.clearanceFront ?: 0f)
                FitStatus.FITS_WITH_RESTRICTIONS -> 500f + (result.clearanceFront ?: 0f)
                FitStatus.DOES_NOT_FIT -> 0f
                FitStatus.INSUFFICIENT_DATA -> 100f
            }
        } ?: 0f
    }
}
