package com.sonphil.canadarecallsandsafetyalerts.domain.use_case.logging

import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject

/**
 * Created by Sonphil on 29-12-20.
 */

class RecordNonFatalExceptionUseCase @Inject constructor() {
    operator fun invoke(throwable: Throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable)
    }
}
