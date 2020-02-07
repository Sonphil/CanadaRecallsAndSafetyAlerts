package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.CategoryFilter
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

    @Query("SELECT * FROM categoryfilter")
    suspend fun getCategoryFilters(): Flow<List<CategoryFilter>>
}