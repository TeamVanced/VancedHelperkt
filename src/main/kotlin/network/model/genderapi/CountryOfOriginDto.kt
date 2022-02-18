package network.model.genderapi

import com.google.gson.annotations.SerializedName

data class CountryOfOriginDto(
    @SerializedName("country_of_origin")
    val countryOrigin: List<CountryDto>
)

data class CountryDto(
    @SerializedName("country_name") val countryName: String,
    val probability: Double
)
