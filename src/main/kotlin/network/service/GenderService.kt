package network.service

import network.model.gender.GenderDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GenderService {

    @GET("get")
    suspend fun get(
        @Query("key") token: String,
        @Query("email") email: String
    ): GenderDto

}