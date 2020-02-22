package com.sonphil.canadarecallsandsafetyalerts.api

import com.sonphil.canadarecallsandsafetyalerts.api.details.ApiRecallDetailsResponse
import com.sonphil.canadarecallsandsafetyalerts.api.recent.ApiRecentRecallsResponse
import com.sonphil.canadarecallsandsafetyalerts.api.search.ApiSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton

/**
 * Allows access to information about recalls and safety alerts
 *
 * https://healthycanadians.gc.ca/connect-connectez/data-donnees/recall-alert-rappel-avis-eng.php
 * https://canadiensensante.gc.ca/connect-connectez/data-donnees/recall-alert-rappel-avis-fra.php
 */
@Singleton
interface CanadaGovernmentApi {
    /**
     * Returns the 15 latest recalls in each category
     *
     * @param lang Whether the response is in English (en) or French (fr)
     */
    @GET("recent/{lang}")
    suspend fun recentRecalls(@Path("lang") lang: String): ApiRecentRecallsResponse

    /**
     * Returns details about a recall
     *
     * @param recallId The ID of the recall
     * @param lang Whether the response is in English (en) or French (fr). Not required but defaults
     * to English if not specified
     */
    @GET("{id}/{lang}")
    suspend fun recallDetails(
        @Path("id") recallId: String,
        @Path("lang") lang: String?
    ): ApiRecallDetailsResponse

    /**
     * Search based on a text
     *
     * @param search The text string to search the remote database for
     * @param lang Whether the response is in English (en) or French (fr). Not required, but
     * defaults to English if not specified
     * @param category Selects specific categories to search. Searches all categories if not
     * specified.
     * @param lim Limits the number of results. Defaults to 5 if not specified.
     * @param offset Offsets the search results. Defaults to 0 if not specified.
     */
    @GET("search")
    suspend fun searchRecall(
        @Query("search") search: String?,
        @Query("lang") lang: String?,
        @Query("cat") category: String?,
        @Query("lim") lim: Int?,
        @Query("off") offset: Int?
    ): ApiSearchResponse
}