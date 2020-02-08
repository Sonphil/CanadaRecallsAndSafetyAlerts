package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import androidx.annotation.DrawableRes

/**
 * Created by Sonphil on 08-02-20.
 */

typealias MoreItemClickHandler = (index: Int) -> Unit

data class MoreItem(
    @DrawableRes val icon: Int,
    val label: String,
    val moreItemClickHandler: MoreItemClickHandler
)