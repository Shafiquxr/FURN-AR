package com.spatialshop.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val subcategory: String,
    val price: Double,
    val originalPrice: Double?,
    val discountPercent: Int?,
    val currency: String,
    val description: String,
    val images: List<String>,
    val colors: List<ProductColor>,
    val widthCm: Double,
    val depthCm: Double,
    val heightCm: Double,
    val rating: Double,
    val reviewCount: Int,
    val merchantName: String,
    val productUrl: String,
    val modelUrl: String?,
    val modelScale: Float,
    val tags: List<String>
) {
    fun toDomain(isFavorite: Boolean = false): Product = Product(
        id = id,
        name = name,
        category = category,
        subcategory = subcategory,
        price = price,
        originalPrice = originalPrice,
        discountPercent = discountPercent,
        currency = currency,
        description = description,
        images = images,
        colors = colors,
        widthCm = widthCm,
        depthCm = depthCm,
        heightCm = heightCm,
        rating = rating,
        reviewCount = reviewCount,
        merchantName = merchantName,
        productUrl = productUrl,
        modelUrl = modelUrl,
        modelScale = modelScale,
        isFavorite = isFavorite,
        tags = tags
    )

    companion object {
        fun fromDomain(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            name = product.name,
            category = product.category,
            subcategory = product.subcategory,
            price = product.price,
            originalPrice = product.originalPrice,
            discountPercent = product.discountPercent,
            currency = product.currency,
            description = product.description,
            images = product.images,
            colors = product.colors,
            widthCm = product.widthCm,
            depthCm = product.depthCm,
            heightCm = product.heightCm,
            rating = product.rating,
            reviewCount = product.reviewCount,
            merchantName = product.merchantName,
            productUrl = product.productUrl,
            modelUrl = product.modelUrl,
            modelScale = product.modelScale,
            tags = product.tags
        )
    }
}
