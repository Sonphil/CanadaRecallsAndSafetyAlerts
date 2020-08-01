package com.sonphil.canadarecallsandsafetyalerts.presentation.more

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Created by Sonphil on 08-02-20.
 */

typealias MoreItemClickHandler = (index: Int) -> Unit

data class MoreItem(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    @StringRes val description: Int,
    val moreItemClickHandler: MoreItemClickHandler
)
