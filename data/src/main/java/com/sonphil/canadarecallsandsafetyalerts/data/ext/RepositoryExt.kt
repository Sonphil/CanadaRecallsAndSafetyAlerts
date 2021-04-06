package com.sonphil.canadarecallsandsafetyalerts.data.ext

import com.sonphil.canadarecallsandsafetyalerts.domain.utils.LoadResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Created by Sonphil on 22-03-20.
 */

/**
 *
 * @param initialDbCall Called to get cached data that will be delivered with loading status. Cached
 * data should be shown to the user while new data is being fetched.
 * @param refreshCall Called fetch new data from the server and refresh the database
 * @param dbFlow Called to get a [Flow] that provides the latest data available in the database
 */
fun <T> getRefreshedDatabaseFlow(
    initialDbCall: suspend () -> T?,
    refreshCall: suspend () -> Unit,
    dbFlow: () -> Flow<T>
) = flow<LoadResult<T>> {
    emit(LoadResult.Loading(null))

    val dBValues = initialDbCall()

    emit(LoadResult.Loading(dBValues))

    try {
        refreshCall()
    } catch (cause: Throwable) {
        emit(LoadResult.Error(dBValues, cause))
    } finally {
        // Always emit DB values because the user might try again on failure
        emitAll(
            dbFlow().map { value ->
                LoadResult.Success(value)
            }
        )
    }
}
