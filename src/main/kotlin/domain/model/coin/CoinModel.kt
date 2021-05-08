package domain.model.coin

data class CoinModel (
    val symbol: String,
    val name: String,
    val price: Double,
    val delta1H: Double,
    val delta24H: Double,
    val delta7D: Double,
    val delta30D: Double
)