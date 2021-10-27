package network.model.coin

import com.google.gson.annotations.SerializedName

data class CoinDto(
    val symbol: String,
    val name: String,
    val price: String,
    @SerializedName("delta_1h") val delta1H: String,
    @SerializedName("delta_24h") val delta24H: String,
    @SerializedName("delta_7d") val delta7D: String,
    @SerializedName("delta_30d") val delta30D: String
)
