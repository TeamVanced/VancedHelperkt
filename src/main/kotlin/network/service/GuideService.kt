package network.service

import network.model.guide.GuideJsonDto
import retrofit2.http.GET
import retrofit2.http.Url

interface GuideService {

    @GET
    suspend fun get(@Url jsonName: String): GuideJsonDto

}