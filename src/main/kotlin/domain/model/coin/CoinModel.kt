package domain.model.coin

import network.model.coin.CoinDto

data class CoinModel(
    val symbol: String,
    val name: String,
    val price: Double,
    val delta1H: Double,
    val delta24H: Double,
    val delta7D: Double,
    val delta30D: Double
) {
    companion object {
        fun fromDto(coinDto: CoinDto): CoinModel {
            return CoinModel(
                symbol = coinDto.symbol,
                name = coinDto.name,
                price = coinDto.price.take(7).toDouble(),
                delta1H = coinDto.delta1H.toDouble(),
                delta24H = coinDto.delta24H.toDouble(),
                delta7D = coinDto.delta7D.toDouble(),
                delta30D = coinDto.delta30D.toDouble()
            )
        }
    }
}