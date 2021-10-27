package network.service

import network.model.guide.GuideJsonDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GuideService {

    @GET("{language}/{jsonName}.json")
    suspend fun get(
        @Path("jsonName") jsonName: String,
        @Path("language") language: String,
    ): GuideJsonDto

}