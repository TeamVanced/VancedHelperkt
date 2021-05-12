package repository.coin

import domain.model.coin.CoinModel
import network.model.coin.CoinDtoMapper
import network.service.CoinService

class CoinRepositoryImpl(
    private val service: CoinService,
    private val mapper: CoinDtoMapper
) : CoinRepository {

    override suspend fun get(token: String, pref: String, symbol: String): CoinModel {
        return mapper.mapToModel(
            service.get(
                token = token,
                pref = pref,
                symbol = symbol
            )
        )
    }

}