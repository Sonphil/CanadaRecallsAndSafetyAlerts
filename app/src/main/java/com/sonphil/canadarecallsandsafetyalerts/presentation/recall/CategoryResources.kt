package com.sonphil.canadarecallsandsafetyalerts.presentation.recall

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.sonphil.canadarecallsandsafetyalerts.R
import com.sonphil.canadarecallsandsafetyalerts.domain.model.Category

/**
 * Created by Sonphil on 11-02-20.
 */

class CategoryResources(val category: Category) {
    @DrawableRes
    val iconId: Int
    @StringRes
    val labelId: Int

    init {
        when (category) {
            Category.FOOD -> {
                iconId = R.drawable.ic_restaurant_black_24dp
                labelId = R.string.label_category_food
            }
            Category.VEHICLE -> {
                iconId = R.drawable.ic_car_black_24dp
                labelId = R.string.label_category_vehicle
            }
            Category.HEALTH_PRODUCT -> {
                iconId = R.drawable.ic_healing_black_24dp
                labelId = R.string.label_category_health_product
            }
            Category.CONSUMER_PRODUCT -> {
                iconId = R.drawable.ic_shopping_cart_black_24dp
                labelId = R.string.label_category_consumer_product
            }
            else -> {
                iconId = R.drawable.ic_category_miscellaneous_black_24dp
                labelId = R.string.label_category_miscellaneous
            }
        }
    }
}
