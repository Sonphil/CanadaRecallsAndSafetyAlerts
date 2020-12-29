package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import kotlinx.coroutines.flow.Flow

/**
 * Created by Sonphil on 15-08-20.
 */

interface CategoryFilterRepositoryInterface {
    suspend fun addFilter(category: Category)

    suspend fun removeFilter(category: Category)

    fun getFilters(): Flow<List<Category>>
}
