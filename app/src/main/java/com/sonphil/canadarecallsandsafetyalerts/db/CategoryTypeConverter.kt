package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.TypeConverter
import com.sonphil.canadarecallsandsafetyalerts.entity.Category

/**
 * Created by Sonphil on 01-02-20.
 */

class CategoryTypeConverter {
    @TypeConverter
    fun categoryToInt(value: Category) = value.ordinal + 1

    @TypeConverter
    fun intToCategory(value: Int) = Category.values()[value - 1]
}