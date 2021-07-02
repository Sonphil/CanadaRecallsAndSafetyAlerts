package com.sonphil.canadarecallsandsafetyalerts.domain.utils

sealed class LoadResult<T>(open val data: T?, open val throwable: Throwable?) {

    data class Success<T>(override val data: T) : LoadResult<T>(data, null)

    data class Error<T>(
        override val data: T?,
        override val throwable: Throwable
    ) : LoadResult<T>(data, throwable)

    data class Loading<T>(override val data: T?) : LoadResult<T>(data, null)
}
