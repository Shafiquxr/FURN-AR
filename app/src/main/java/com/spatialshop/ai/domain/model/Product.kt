package com.spatialshop.ai.domain.model

/**
 * Core Product domain model for Spatial Shopping AI.
 * Represents a furniture product with all its attributes.
 */
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val subcategory: String = "",
    val price: Double,
    val originalPrice: Double? = null,
    val discountPercent: Int? = null,
    val currency: String = "₹",
    val description: String,
    val images: List<String>,
    val colors: List<ProductColor> = emptyList(),
    val widthCm: Double,
    val depthCm: Double,
    val heightCm: Double,
    val rating: Double = 0.0,
    val reviewCount: Int = 0,
    val merchantName: String = "",
    val productUrl: String = "",
    val modelUrl: String? = null,
    val modelScale: Float = 1.0f,
    val isFavorite: Boolean = false,
    val tags: List<String> = emptyList()
) {
    /**
     * Formatted price string with currency symbol.
     * e.g., "₹9,499"
     */
    val formattedPrice: String
        get() = "$currency ${String.format("%,.0f", price)}"

    /**
     * Formatted original price for strikethrough display.
     * e.g., "₹12,999"
     */
    val formattedOriginalPrice: String?
        get() = originalPrice?.let { "$currency ${String.format("%,.0f", it)}" }

    /**
     * Discount label text.
     * e.g., "30% OFF"
     */
    val discountLabel: String?
        get() = discountPercent?.let { "${it}% OFF" }

    /**
     * Whether this product has a 3D model available.
     */
    val has3DModel: Boolean
        get() = !modelUrl.isNullOrEmpty()
}

/**
 * Represents a color variant for a product.
 */
data class ProductColor(
    val name: String,
    val hexCode: String,
    val imageUrl: String? = null
)

/**
 * Product category for filtering.
 */
data class Category(
    val id: String,
    val name: String,
    val iconName: String = "",
    val imageUrl: String = "",
    val productCount: Int = 0
)

/**
 * A furniture collection (e.g., "Living Room", "Bedroom").
 */
data class FurnitureCollection(
    val id: String,
    val name: String,
    val imageUrl: String = "",
    val itemCount: Int = 0,
    val description: String = ""
)

/**
 * Cart item wrapping a product with quantity.
 */
data class CartItem(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: ProductColor? = null
) {
    val totalPrice: Double
        get() = product.price * quantity

    val formattedTotalPrice: String
        get() = "${product.currency} ${String.format("%,.0f", totalPrice)}"
}
