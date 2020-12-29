package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.CategoryFilterDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toCategories
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toCategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Created by Sonphil on 07-02-20.
 */

class CategoryFilterRepository @Inject constructor(
    private val dao: CategoryFilterDao
) : CategoryFilterRepositoryInterface {
    override suspend fun addFilter(category: Category) {
        dao.insertCategoryFilter(category.toCategoryFilter())
    }

    override suspend fun removeFilter(category: Category) {
        dao.deleteCategoryFilter(category.toCategoryFilter())
    }

    override fun getFilters(): Flow<List<Category>> = dao.getCategoryFilters().map {
        it.toCategories()
    }
}
