package network.model.country

import com.google.gson.annotations.SerializedName

data class CountryDto(
    @SerializedName("country_name") val countryName: String,
    val probability: Double
)
