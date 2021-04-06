package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.CategoryFilterDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toCategories
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toCategoryFilter
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 07-02-20.
 */

class CategoryFilterRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val dao: CategoryFilterDao
) : CategoryFilterRepositoryInterface {
    override suspend fun addFilter(category: Category) = withContext(appDispatchers.io) {
        dao.insertCategoryFilter(category.toCategoryFilter())
    }

    override suspend fun removeFilter(
        category: Category
    ) = withContext(appDispatchers.io) {
        dao.deleteCategoryFilter(category.toCategoryFilter())
    }

    override fun getFilters(): Flow<List<Category>> = dao.getCategoryFilters().map {
        it.toCategories()
    }.flowOn(appDispatchers.io)
}
