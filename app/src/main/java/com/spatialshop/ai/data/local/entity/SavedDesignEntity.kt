package com.spatialshop.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spatialshop.ai.domain.model.FitResult
import com.spatialshop.ai.domain.model.FitStatus
import com.spatialshop.ai.domain.model.ProductPlacement
import com.spatialshop.ai.domain.model.SavedDesign

@Entity(tableName = "saved_designs")
data class SavedDesignEntity(
    @PrimaryKey val id: String,
    val name: String,
    val productId: String,
    val rotationY: Float,
    val fitStatus: String,
    val clearanceFront: Float,
    val createdAt: Long = System.currentTimeMillis(),
    val thumbnailPath: String? = null
) {
    fun toDomain(): SavedDesign = SavedDesign(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = createdAt,
        placements = listOf(
            ProductPlacement(
                productId = productId,
                positionX = 0f,
                positionY = 0f,
                positionZ = 0f,
                rotationY = rotationY,
                fitResult = FitResult(
                    status = try { FitStatus.valueOf(fitStatus) } catch (_: Exception) { FitStatus.FITS },
                    clearanceFront = clearanceFront
                )
            )
        ),
        thumbnailPath = thumbnailPath
    )
}
