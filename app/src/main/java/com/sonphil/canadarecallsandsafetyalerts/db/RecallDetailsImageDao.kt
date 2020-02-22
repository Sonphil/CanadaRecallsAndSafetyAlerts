package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetailsImage

/**
 * Created by Sonphil on 22-02-20.
 */

@Dao
interface RecallDetailsImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<RecallDetailsImage>)

    @Query("DELETE FROM recalldetailsimage WHERE recallId = :recallId")
    suspend fun deleteAllOfRecall(recallId: String)

    @Transaction
    suspend fun refreshRecallDetailsImagesForRecall(
        images: List<RecallDetailsImage>,
        recall: Recall
    ) {
        deleteAllOfRecall(recall.id)
        insertAll(images)
    }
}