package com.sonphil.canadarecallsandsafetyalerts.data.repository

import com.sonphil.canadarecallsandsafetyalerts.data.db.ReadStatusDao
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toDbReadStatus
import com.sonphil.canadarecallsandsafetyalerts.data.db.mapper.toReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.model.ReadStatus
import com.sonphil.canadarecallsandsafetyalerts.domain.repository.ReadStatusRepositoryInterface
import com.sonphil.canadarecallsandsafetyalerts.domain.utils.AppDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created by Sonphil on 08-03-20.
 */

class ReadStatusRepository @Inject constructor(
    private val appDispatchers: AppDispatchers,
    private val readStatusDao: ReadStatusDao
) : ReadStatusRepositoryInterface {
    override suspend fun insertReadStatus(readStatus: ReadStatus) = readStatusDao
        .insertReadStatus(readStatus.toDbReadStatus())

    override suspend fun getReadStatus(
        recallId: String
    ): ReadStatus? = withContext(appDispatchers.io) {
        readStatusDao.getReadStatus(recallId)?.toReadStatus()
    }
}
