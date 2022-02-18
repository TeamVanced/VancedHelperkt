package repository.coin

import domain.model.coin.CoinModel
import network.service.CoinlibService

interface CoinlibRepository {

    suspend fun get(
        token: String,
        pref: String,
        symbol: String
    ): CoinModel

}

class CoinlibRepositoryImpl(
    private val service: CoinlibService,
) : CoinlibRepository {

    override suspend fun get(
        token: String,
        pref: String,
        symbol: String
    ): CoinModel {
        return CoinModel.fromDto(
            service.get(
                token = token,
                pref = pref,
                symbol = symbol
            )
        )
    }
}