package network.service

import network.model.genderapi.CountryOfOriginDto
import network.model.genderapi.GenderDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GenderapiService {

    @GET("get")
    suspend fun getGender(
        @Query("key") token: String,
        @Query("name") name: String
    ): GenderDto

    @GET("get-country-of-origin")
    suspend fun getCountryOfOrigin(
        @Query("key") token: String,
        @Query("name") name: String
    ): CountryOfOriginDto

}