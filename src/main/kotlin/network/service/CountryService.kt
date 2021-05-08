package network.service

import network.model.country.CountryOfOriginDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryService {

    @GET("get-country-of-origin")
    suspend fun get(
        @Query("key") token: String,
        @Query("name") name: String
    ): CountryOfOriginDto

}