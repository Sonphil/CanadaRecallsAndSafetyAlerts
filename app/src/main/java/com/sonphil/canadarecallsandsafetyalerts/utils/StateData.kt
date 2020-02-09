package com.sonphil.canadarecallsandsafetyalerts.utils

sealed class StateData<T>(open val data: T?, open val message: String?) {

    data class Success<T>(override val data: T) : StateData<T>(data, null)

    data class Error<T>(
        override val message: String?,
        override val data: T
    ) : StateData<T>(data, message)

    data class Loading<T>(override val data: T) : StateData<T>(data, null)
}
