package cz.utb.fai.soundboard.services.api

import retrofit2.http.GET
import retrofit2.http.Query

interface WikidataApiService {
    @GET("sparql")
    suspend fun getMovies(
        @Query("query") query: String,
        @Query("format") format: String = "json"
    ): WikidataResponse
}