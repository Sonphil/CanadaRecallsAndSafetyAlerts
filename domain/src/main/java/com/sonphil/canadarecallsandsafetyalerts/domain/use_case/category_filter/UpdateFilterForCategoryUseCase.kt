package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.category_filter

import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class UpdateFilterForCategoryUseCase @Inject constructor(
    private val categoryFilterRepository: CategoryFilterRepositoryInterface
) {
    suspend operator fun invoke(category: Category, checked: Boolean) {
        if (checked) {
            categoryFilterRepository.addFilter(category)
        } else {
            categoryFilterRepository.removeFilter(category)
        }
    }
}
