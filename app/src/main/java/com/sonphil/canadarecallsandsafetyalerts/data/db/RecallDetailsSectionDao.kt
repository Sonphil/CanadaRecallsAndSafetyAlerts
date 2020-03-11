package com.sonphil.canadarecallsandsafetyalerts.data.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.data.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.data.entity.RecallDetailsSection

/**
 * Created by Sonphil on 22-02-20.
 */

@Dao
interface RecallDetailsSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(detailsSections: List<RecallDetailsSection>)

    @Query("DELETE FROM recalldetailssection WHERE recallId = :recallId")
    suspend fun deleteAllOfRecall(recallId: String)

    @Transaction
    suspend fun refreshRecallDetailsSectionsForRecall(
        detailsSections: List<RecallDetailsSection>,
        recall: Recall
    ) {
        deleteAllOfRecall(recall.id)
        insertAll(detailsSections)
    }
}