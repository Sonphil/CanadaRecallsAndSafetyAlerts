package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.ReadStatusDao
import com.sonphil.canadarecallsandsafetyalerts.data.entity.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.ReadStatusRepositoryInterface
import javax.inject.Inject

/**
 * Created by Sonphil on 08-03-20.
 */

class ReadStatusRepository @Inject constructor(
    private val readStatusDao: ReadStatusDao
) : ReadStatusRepositoryInterface {
    override suspend fun insertReadStatus(readStatus: ReadStatus) = readStatusDao
        .insertReadStatus(readStatus)

    override suspend fun getReadStatus(recallId: String): ReadStatus? = readStatusDao
        .getReadStatus(recallId)
}
