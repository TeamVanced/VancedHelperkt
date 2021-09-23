package network.service

import network.model.coin.CoinDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CoinService {

    @GET("coin")
    suspend fun get(
        @Query("key") token: String,
        @Query("pref") pref: String,
        @Query("symbol") symbol: String
    ): CoinDto

}