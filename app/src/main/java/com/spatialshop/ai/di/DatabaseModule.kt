package com.spatialshop.ai.di

import android.content.Context
import androidx.room.Room
import com.spatialshop.ai.data.local.AppDatabase
import com.spatialshop.ai.data.local.dao.CartDao
import com.spatialshop.ai.data.local.dao.FavoriteDao
import com.spatialshop.ai.data.local.dao.ProductDao
import com.spatialshop.ai.data.local.dao.SavedDesignDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideProductDao(database: AppDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao {
        return database.favoriteDao()
    }

    @Provides
    fun provideCartDao(database: AppDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideSavedDesignDao(database: AppDatabase): SavedDesignDao {
        return database.savedDesignDao()
    }
}
