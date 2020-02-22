package com.sonphil.canadarecallsandsafetyalerts.db

import androidx.room.*
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import com.sonphil.canadarecallsandsafetyalerts.entity.RecallDetailsSection

/**
 * Created by Sonphil on 22-02-20.
 */

@Dao
interface RecallDetailsSectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sections: List<RecallDetailsSection>)

    @Query("DELETE FROM recalldetailssection WHERE recallId = :recallId")
    suspend fun deleteAllOfRecall(recallId: String)

    @Transaction
    suspend fun refreshRecallDetailsSectionsForRecall(
        sections: List<RecallDetailsSection>,
        recall: Recall
    ) {
        deleteAllOfRecall(recall.id)
        insertAll(sections)
    }
}