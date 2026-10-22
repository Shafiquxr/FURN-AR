package com.spatialshop.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spatialshop.ai.domain.model.ProductColor

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productId: String,
    val quantity: Int = 1,
    val selectedColorName: String? = null,
    val selectedColorHex: String? = null,
    val addedAt: Long = System.currentTimeMillis()
)
