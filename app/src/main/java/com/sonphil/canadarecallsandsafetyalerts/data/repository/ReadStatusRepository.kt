package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.ReadStatusDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus
import javax.inject.Inject

/**
 * Created by Sonphil on 08-03-20.
 */

class ReadStatusRepository @Inject constructor(private val readStatusDao: ReadStatusDao) {
    suspend fun insertReadStatus(readStatus: ReadStatus) = readStatusDao
        .insertReadStatus(readStatus)

    suspend fun getReadStatus(recallId: String) = readStatusDao.getReadStatus(recallId)
}
