package com.sonphil.canadarecallsandsafetyalerts.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.sonphil.canadarecallsandsafetyalerts.db.CategoryTypeConverter

/**
 * Created by Sonphil on 31-01-20.
 */

@Entity(foreignKeys = [ForeignKey(
    entity = Recall::class,
    parentColumns = ["id"],
    childColumns = ["recallId"],
    onDelete = CASCADE
)])
@TypeConverters(CategoryTypeConverter::class)
data class RecallDetails(
    val category: Category,
    val datePublished: Int?,
    val basicDetails: String?,
    val summary: String?,
    val whatToDo: String?,
    val productDescription: String,
    //val images: List<RecallDetailsImage>,
    @PrimaryKey
    val recallId: String,
    val startDate: Int?,
    val title: String?,
    val url: String?
)