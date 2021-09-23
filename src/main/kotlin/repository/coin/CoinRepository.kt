package repository.coin

import domain.model.coin.CoinModel

interface CoinRepository {

    suspend fun get(token: String, pref: String, symbol: String): CoinModel

}