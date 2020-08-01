package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.data.entity.CategoryFilter
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 07-02-20.
 */

@Dao
interface CategoryFilterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryFilter(categoryFilter: CategoryFilter)

    @Delete
    suspend fun deleteCategoryFilter(categoryFilter: CategoryFilter)

    @Query("SELECT category FROM categoryfilter")
    fun getCategoryFilters(): Flow<List<Category>>
}
