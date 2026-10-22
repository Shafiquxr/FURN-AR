package com.spatialshop.ai.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.spatialshop.ai.data.local.entity.SavedDesignEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedDesignDao {
    @Query("SELECT * FROM saved_designs ORDER BY createdAt DESC")
    fun getAllSavedDesigns(): Flow<List<SavedDesignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavedDesign(design: SavedDesignEntity)

    @Query("DELETE FROM saved_designs WHERE id = :id")
    suspend fun deleteSavedDesign(id: String)
}
