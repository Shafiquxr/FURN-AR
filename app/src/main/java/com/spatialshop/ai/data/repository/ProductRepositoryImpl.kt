package com.spatialshop.ai.data.repository

import com.spatialshop.ai.data.local.dao.CartDao
import com.spatialshop.ai.data.local.dao.FavoriteDao
import com.spatialshop.ai.data.local.dao.ProductDao
import com.spatialshop.ai.data.local.entity.CartItemEntity
import com.spatialshop.ai.data.local.entity.FavoriteEntity
import com.spatialshop.ai.data.local.entity.ProductEntity
import com.spatialshop.ai.domain.model.CartItem
import com.spatialshop.ai.domain.model.Category
import com.spatialshop.ai.domain.model.FurnitureCollection
import com.spatialshop.ai.domain.model.Product
import com.spatialshop.ai.domain.model.ProductColor
import com.spatialshop.ai.domain.repository.ProductRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

import com.spatialshop.ai.data.local.dao.SavedDesignDao
import com.spatialshop.ai.data.local.entity.SavedDesignEntity
import com.spatialshop.ai.domain.model.SavedDesign
import com.spatialshop.ai.data.repository.SampleData
import com.spatialshop.ai.data.remote.api.SpatialShopApi
import kotlinx.coroutines.withContext
import java.util.UUID

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val favoriteDao: FavoriteDao,
    private val cartDao: CartDao,
    private val savedDesignDao: SavedDesignDao,
    private val api: SpatialShopApi
) : ProductRepository {

    init {
        // Seed and sync products with latest 3D GLB asset models
        CoroutineScope(Dispatchers.IO).launch {
            val entities = SampleData.products.map { ProductEntity.fromDomain(it) }
            productDao.insertProducts(entities)
        }
    }

    override fun getProducts(): Flow<List<Product>> {
        return combine(
            productDao.getAllProducts(),
            favoriteDao.getAllFavoriteIds()
        ) { entities: List<ProductEntity>, favIds: List<String> ->
            val favSet = favIds.toSet()
            if (entities.isEmpty()) {
                SampleData.products.map { it.copy(isFavorite = favSet.contains(it.id)) }
            } else {
                entities.map { it.toDomain(isFavorite = favSet.contains(it.id)) }
            }
        }
    }

    override fun getProductById(id: String): Flow<Product?> {
        return combine(
            productDao.getProductById(id),
            favoriteDao.isFavorite(id)
        ) { entity: ProductEntity?, isFav: Boolean ->
            entity?.toDomain(isFavorite = isFav)
                ?: SampleData.products.find { it.id == id }?.copy(isFavorite = isFav)
        }
    }

    override suspend fun getProductByIdDirect(id: String): Product? {
        val isFav = favoriteDao.isFavoriteDirect(id)
        return productDao.getProductByIdDirect(id)?.toDomain(isFavorite = isFav)
            ?: SampleData.products.find { it.id == id }?.copy(isFavorite = isFav)
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        if (category.equals("All", ignoreCase = true)) {
            return getProducts()
        }
        return combine(
            productDao.getProductsByCategory(category),
            favoriteDao.getAllFavoriteIds()
        ) { entities: List<ProductEntity>, favIds: List<String> ->
            val favSet = favIds.toSet()
            if (entities.isEmpty()) {
                SampleData.products
                    .filter { it.category.equals(category, ignoreCase = true) }
                    .map { it.copy(isFavorite = favSet.contains(it.id)) }
            } else {
                entities.map { it.toDomain(isFavorite = favSet.contains(it.id)) }
            }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        if (query.isBlank()) {
            return getProducts()
        }
        return combine(
            productDao.searchProducts(query),
            favoriteDao.getAllFavoriteIds()
        ) { entities: List<ProductEntity>, favIds: List<String> ->
            val favSet = favIds.toSet()
            if (entities.isEmpty()) {
                SampleData.products
                    .filter {
                        it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true) ||
                        it.category.contains(query, ignoreCase = true)
                    }
                    .map { it.copy(isFavorite = favSet.contains(it.id)) }
            } else {
                entities.map { it.toDomain(isFavorite = favSet.contains(it.id)) }
            }
        }
    }

    override fun getBestSellers(): Flow<List<Product>> {
        return getProducts().map { list ->
            list.filter { it.rating >= 4.7 }
        }
    }

    override fun getDeals(): Flow<List<Product>> {
        return getProducts().map { list ->
            list.filter { (it.discountPercent ?: 0) >= 20 }
        }
    }

    override fun getCategories(): Flow<List<Category>> = flow {
        emit(SampleData.categories)
    }

    override fun getCollections(): Flow<List<FurnitureCollection>> = flow {
        emit(SampleData.collections)
    }

    override fun getFavorites(): Flow<List<Product>> {
        return combine(
            getProducts(),
            favoriteDao.getAllFavoriteIds()
        ) { products: List<Product>, favIds: List<String> ->
            val favSet = favIds.toSet()
            products.filter { favSet.contains(it.id) }
        }
    }

    override fun isFavorite(productId: String): Flow<Boolean> {
        return favoriteDao.isFavorite(productId)
    }

    override suspend fun toggleFavorite(productId: String) {
        val exists = favoriteDao.isFavoriteDirect(productId)
        if (exists) {
            favoriteDao.removeFavorite(productId)
        } else {
            favoriteDao.addFavorite(FavoriteEntity(productId = productId))
        }
    }

    override fun getCartItems(): Flow<List<CartItem>> {
        return combine(
            cartDao.getAllCartItems(),
            getProducts()
        ) { cartEntities: List<CartItemEntity>, products: List<Product> ->
            val productMap = products.associateBy { it.id }
            cartEntities.mapNotNull { entity ->
                val product = productMap[entity.productId]
                    ?: SampleData.products.find { it.id == entity.productId }
                product?.let { prod ->
                    val color = if (entity.selectedColorName != null && entity.selectedColorHex != null) {
                        ProductColor(name = entity.selectedColorName, hexCode = entity.selectedColorHex)
                    } else null
                    CartItem(
                        product = prod,
                        quantity = entity.quantity,
                        selectedColor = color
                    )
                }
            }
        }
    }

    override suspend fun addToCart(
        product: Product,
        quantity: Int,
        selectedColor: ProductColor?
    ) {
        cartDao.insertCartItem(
            CartItemEntity(
                productId = product.id,
                quantity = quantity,
                selectedColorName = selectedColor?.name,
                selectedColorHex = selectedColor?.hexCode
            )
        )
    }

    override suspend fun updateCartQuantity(cartItemId: Long, quantity: Int) {
        if (quantity <= 0) {
            cartDao.deleteCartItem(cartItemId)
        } else {
            cartDao.updateQuantity(cartItemId, quantity)
        }
    }

    override suspend fun removeFromCart(cartItemId: Long) {
        cartDao.deleteCartItem(cartItemId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }

    override fun getCartCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }

    override fun getSavedDesigns(): Flow<List<SavedDesign>> {
        return savedDesignDao.getAllSavedDesigns().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun saveDesign(
        name: String,
        productId: String,
        rotationY: Float,
        fitStatus: String,
        clearanceFront: Float,
        thumbnailPath: String?
    ): String {
        val id = UUID.randomUUID().toString()
        val entity = SavedDesignEntity(
            id = id,
            name = name,
            productId = productId,
            rotationY = rotationY,
            fitStatus = fitStatus,
            clearanceFront = clearanceFront,
            thumbnailPath = thumbnailPath
        )
        savedDesignDao.insertSavedDesign(entity)
        return id
    }

    override suspend fun deleteSavedDesign(id: String) {
        savedDesignDao.deleteSavedDesign(id)
    }

    override suspend fun syncProducts(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.getProducts()
            if (response.isSuccessful) {
                val dtoList = response.body().orEmpty()
                if (dtoList.isNotEmpty()) {
                    val entities = dtoList.map { it.toEntity() }
                    productDao.insertProducts(entities)
                }
                Result.success(Unit)
            } else {
                Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
