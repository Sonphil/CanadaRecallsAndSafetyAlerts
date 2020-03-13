package com.sonphil.canadarecallsandsafetyalerts.domain.category_filter

import com.sonphil.canadarecallsandsafetyalerts.data.entity.Category
import com.sonphil.canadarecallsandsafetyalerts.data.repository.CategoryFilterRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class UpdateFilterForCategoryUseCase @Inject constructor(
    private val categoryFilterRepository: CategoryFilterRepository
) {
    suspend operator fun invoke(category: Category, checked: Boolean) {
        if (checked) {
            categoryFilterRepository.addFilter(category)
        } else {
            categoryFilterRepository.removeFilter(category)
        }
    }
}