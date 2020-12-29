package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.db.entity.RecallImage

/**
 * Created by Sonphil on 22-02-20.
 */

@Dao
interface RecallDetailsImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<RecallImage>)

    @Query("DELETE FROM recallimage WHERE recallId = :recallId")
    suspend fun deleteAllOfRecall(recallId: String)

    @Transaction
    suspend fun refreshRecallDetailsImagesForRecall(
        images: List<RecallImage>,
        recall: Recall
    ) {
        deleteAllOfRecall(recall.id)
        insertAll(images)
    }
}
