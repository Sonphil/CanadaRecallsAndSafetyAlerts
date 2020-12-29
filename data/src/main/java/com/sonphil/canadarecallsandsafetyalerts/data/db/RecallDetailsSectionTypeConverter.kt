package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.TypeConverter
import com.sonphil.canadarecallsandsafetyalerts.domain.model.RecallDetailsSectionType

/**
 * Created by Sonphil on 07-03-20.
 */

class RecallDetailsSectionTypeConverter {
    @TypeConverter
    fun typeToString(value: RecallDetailsSectionType) = value.name

    @TypeConverter
    fun stringToType(value: String) = RecallDetailsSectionType.values()
        .find { type ->
            type.name.equals(value, true)
        } ?: RecallDetailsSectionType.OTHER
}
