package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.db.CategoryFilterDao
import com.sonphil.canadarecallsandsafetyalerts.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.entity.CategoryFilter
import javax.inject.Inject

/**
 * Created by Sonphil on 07-02-20.
 */

class CategoryFilterRepository @Inject constructor(private val dao: CategoryFilterDao) {
    suspend fun addFilter(category: Category) {
        dao.insertCategoryFilter(CategoryFilter(category))
    }

    suspend fun removeFilter(category: Category) {
        dao.deleteCategoryFilter(CategoryFilter(category))
    }

    fun getFilters() = dao.getCategoryFilters()
}