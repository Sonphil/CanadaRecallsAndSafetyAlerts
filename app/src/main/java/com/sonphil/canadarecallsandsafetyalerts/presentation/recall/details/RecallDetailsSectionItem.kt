package com.sonphil.canadarecallsandsafetyalerts.presentation.recall.details

import androidx.annotation.LayoutRes
import com.sonphil.canadarecallsandsafetyalerts.R

/**
 * Created by Sonphil on 01-03-20.
 */

sealed class RecallDetailsSectionItem(@LayoutRes open val layoutId: Int) {
    data class RecallDetailsSectionHeaderItem(val title: String) :
        RecallDetailsSectionItem(R.layout.item_recall_details_section_header)

    data class RecallDetailsSectionContentItem(val htmlContent: String) :
        RecallDetailsSectionItem(R.layout.item_recall_details_section_content)
}