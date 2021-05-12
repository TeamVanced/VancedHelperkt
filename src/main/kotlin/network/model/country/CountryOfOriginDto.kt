package network.model.country

import com.google.gson.annotations.SerializedName

data class CountryOfOriginDto(
    @SerializedName("country_of_origin")
    val countryOrigin: List<CountryDto>
)
