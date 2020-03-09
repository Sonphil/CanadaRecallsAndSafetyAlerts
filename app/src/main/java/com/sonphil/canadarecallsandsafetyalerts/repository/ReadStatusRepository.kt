package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.db.ReadStatusDao
import com.sonphil.canadarecallsandsafetyalerts.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.entity.Recall
import javax.inject.Inject

/**
 * Created by Sonphil on 08-03-20.
 */

class ReadStatusRepository @Inject constructor(private val readStatusDao: ReadStatusDao) {
    suspend fun markRecallAsRead(recall: Recall) {
        val readStatus = ReadStatus(recall.id)

        readStatusDao.insertReadStatus(readStatus)
    }
}