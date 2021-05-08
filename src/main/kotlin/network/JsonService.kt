package network

import network.model.GuideJsonDto
import retrofit2.http.GET
import retrofit2.http.Url

interface JsonService {

    @GET
    suspend fun get(@Url jsonName: String): GuideJsonDto

}