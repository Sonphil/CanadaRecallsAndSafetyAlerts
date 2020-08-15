package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.CategoryFilterDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.data.entity.CategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Created by Sonphil on 07-02-20.
 */

class CategoryFilterRepository @Inject constructor(
    private val dao: CategoryFilterDao
) : CategoryFilterRepositoryInterface {
    override suspend fun addFilter(category: Category) {
        dao.insertCategoryFilter(CategoryFilter(category))
    }

    override suspend fun removeFilter(category: Category) {
        dao.deleteCategoryFilter(CategoryFilter(category))
    }

    override fun getFilters(): Flow<List<Category>> = dao.getCategoryFilters()
}
