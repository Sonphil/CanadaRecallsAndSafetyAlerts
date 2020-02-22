package com.sonphil.canadarecallsandsafetyalerts.repository

import com.sonphil.canadarecallsandsafetyalerts.api.CanadaGovernmentApi
import com.sonphil.canadarecallsandsafetyalerts.db.RecallDao
import javax.inject.Inject

/**
 * Created by Sonphil on 01-02-20.
 */

class RecallDetailsRepository @Inject constructor(
    private val api: CanadaGovernmentApi,
    private val dao: RecallDao
)