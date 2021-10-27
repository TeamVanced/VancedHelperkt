package network.model.coin

import domain.model.coin.CoinModel
import domain.util.EntityMapper
import java.text.DecimalFormat

class CoinDtoMapper : EntityMapper<CoinDto, CoinModel> {

    override suspend fun mapToModel(entity: CoinDto): CoinModel {
        return with(entity) {
            CoinModel(
                symbol = symbol,
                name = name,
                price = price.take(7).toDouble(),
                delta1H = delta1H.toDouble(),
                delta24H = delta24H.toDouble(),
                delta7D = delta7D.toDouble(),
                delta30D = delta30D.toDouble()
            )
        }
    }

}