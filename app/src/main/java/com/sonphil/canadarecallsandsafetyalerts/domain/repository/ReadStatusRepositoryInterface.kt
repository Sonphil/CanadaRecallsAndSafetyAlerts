package com.sonphil.canadarecallsandsafetyalerts.domain.repository

import com.sonphil.canadarecallsandsafetyalerts.domain.entity.ReadStatus

/**
 * Created by Sonphil on 15-08-20.
 */

interface ReadStatusRepositoryInterface {
    suspend fun insertReadStatus(readStatus: ReadStatus)

    suspend fun getReadStatus(recallId: String): ReadStatus?
}
