package com.sonphil.canadarecallsandsafetyalerts.domain.category_filter

import com.sonphil.canadarecallsandsafetyalerts.data.repository.CategoryFilterRepository
import javax.inject.Inject

/**
 * Created by Sonphil on 12-03-20.
 */

class GetCategoryFiltersUseCase @Inject constructor(
    private val categoryFilterRepository: CategoryFilterRepository
) {
    operator fun invoke() = categoryFilterRepository.getFilters()
}