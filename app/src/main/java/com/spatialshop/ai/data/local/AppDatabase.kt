package com.spatialshop.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.spatialshop.ai.data.local.converters.Converters
import com.spatialshop.ai.data.local.dao.CartDao
import com.spatialshop.ai.data.local.dao.FavoriteDao
import com.spatialshop.ai.data.local.dao.ProductDao
import com.spatialshop.ai.data.local.dao.SavedDesignDao
import com.spatialshop.ai.data.local.entity.CartItemEntity
import com.spatialshop.ai.data.local.entity.FavoriteEntity
import com.spatialshop.ai.data.local.entity.ProductEntity
import com.spatialshop.ai.data.local.entity.SavedDesignEntity

@Database(
    entities = [
        ProductEntity::class,
        FavoriteEntity::class,
        CartItemEntity::class,
        SavedDesignEntity::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun cartDao(): CartDao
    abstract fun savedDesignDao(): SavedDesignDao

    companion object {
        const val DATABASE_NAME = "spatial_shop_db"
    }
}
