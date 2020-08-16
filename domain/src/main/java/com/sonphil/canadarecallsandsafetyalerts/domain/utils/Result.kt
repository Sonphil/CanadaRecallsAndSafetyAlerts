package com.sonphil.canadarecallsandsafetyalerts.domain.utils

sealed class Result<T>(open val data: T?, open val throwable: Throwable?) {

    data class Success<T>(override val data: T) : Result<T>(data, null)

    data class Error<T>(
        override val data: T?,
        override val throwable: Throwable?
    ) : Result<T>(data, throwable)

    data class Loading<T>(override val data: T?) : Result<T>(data, null)
}
