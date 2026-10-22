package com.spatialshop.ai.data.remote.dto

import com.google.gson.annotations.SerializedName
import com.spatialshop.ai.data.local.entity.ProductEntity
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor

data class ProductDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("category") val category: String,
    @SerializedName("subcategory") val subcategory: String = "",
    @SerializedName("price") val price: Double,
    @SerializedName("original_price") val originalPrice: Double? = null,
    @SerializedName("discount_percent") val discountPercent: Int? = null,
    @SerializedName("currency") val currency: String = "₹",
    @SerializedName("description") val description: String,
    @SerializedName("images") val images: List<String> = emptyList(),
    @SerializedName("colors") val colors: List<ProductColorDto> = emptyList(),
    @SerializedName("width_cm") val widthCm: Double,
    @SerializedName("depth_cm") val depthCm: Double,
    @SerializedName("height_cm") val heightCm: Double,
    @SerializedName("rating") val rating: Double = 0.0,
    @SerializedName("review_count") val reviewCount: Int = 0,
    @SerializedName("merchant_name") val merchantName: String = "",
    @SerializedName("product_url") val productUrl: String = "",
    @SerializedName("model_url") val modelUrl: String? = null,
    @SerializedName("model_scale") val modelScale: Float = 1.0f,
    @SerializedName("tags") val tags: List<String> = emptyList()
) {
    fun toDomain(): Product = Product(
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
        colors = colors.map { it.toDomain() },
        widthCm = widthCm,
        depthCm = depthCm,
        heightCm = heightCm,
        rating = rating,
        reviewCount = reviewCount,
        merchantName = merchantName,
        productUrl = productUrl,
        modelUrl = modelUrl,
        modelScale = modelScale,
        tags = tags
    )

    fun toEntity(): ProductEntity = ProductEntity(
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
        colors = colors.map { it.toDomain() },
        widthCm = widthCm,
        depthCm = depthCm,
        heightCm = heightCm,
        rating = rating,
        reviewCount = reviewCount,
        merchantName = merchantName,
        productUrl = productUrl,
        modelUrl = modelUrl,
        modelScale = modelScale,
        tags = tags
    )
}

data class ProductColorDto(
    @SerializedName("name") val name: String,
    @SerializedName("hex_code") val hexCode: String,
    @SerializedName("preview_image_url") val previewImageUrl: String? = null
) {
    fun toDomain(): ProductColor = ProductColor(
        name = name,
        hexCode = hexCode,
        imageUrl = previewImageUrl
    )
}
