package com.sonphil.canadarecallsandsafetyalerts.domain.utils

import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

/**
 * Created by Sonphil on 05-04-21.
 */

class AppDispatchers @Inject constructor(
    val main: CoroutineDispatcher,
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher
)
