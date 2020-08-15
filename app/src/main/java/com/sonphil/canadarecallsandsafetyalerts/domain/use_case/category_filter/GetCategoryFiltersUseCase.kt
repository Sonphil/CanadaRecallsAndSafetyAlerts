package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.category_filter

import com.sonphil.canadarecallsandsafetyalerts.domain.repository.CategoryFilterRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetCategoryFiltersUseCase @Inject constructor(
    private val categoryFilterRepository: CategoryFilterRepositoryInterface
) {
    operator fun invoke() = categoryFilterRepository.getFilters()
}
