package com.spatialshop.ai.domain.repository

import com.spatialshop.ai.domain.model.CartItem
import com.spatialshop.ai.domain.model.Category
import com.spatialshop.ai.domain.model.FurnitureCollection
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor
import com.spatialshop.ai.domain.model.SavedDesign
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: String): Flow<Product?>
    suspend fun getProductByIdDirect(id: String): Product?
    fun getProductsByCategory(category: String): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    fun getBestSellers(): Flow<List<Product>>
    fun getDeals(): Flow<List<Product>>
    fun getCategories(): Flow<List<Category>>
    fun getCollections(): Flow<List<FurnitureCollection>>

    // Favorites
    fun getFavorites(): Flow<List<Product>>
    fun isFavorite(productId: String): Flow<Boolean>
    suspend fun toggleFavorite(productId: String)

    // Cart
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(product: Product, quantity: Int = 1, selectedColor: ProductColor? = null)
    suspend fun updateCartQuantity(cartItemId: Long, quantity: Int)
    suspend fun removeFromCart(cartItemId: Long)
    suspend fun clearCart()
    fun getCartCount(): Flow<Int>

    // Saved Designs (Phase 4)
    fun getSavedDesigns(): Flow<List<SavedDesign>>
    suspend fun saveDesign(
        name: String,
        productId: String,
        rotationY: Float,
        fitStatus: String,
        clearanceFront: Float,
        thumbnailPath: String? = null
    ): String
    suspend fun deleteSavedDesign(id: String)

    // Remote Sync (Phase 7)
    suspend fun syncProducts(): Result<Unit>
}
