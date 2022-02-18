package domain.model.genderapi

import network.model.genderapi.CountryOfOriginDto

data class CountryOfOriginModel(
    val countries: List<CountryModel>
) {
    companion object {
        fun fromDto(dto: CountryOfOriginDto): CountryOfOriginModel {
            return CountryOfOriginModel(
                countries = dto.countryOrigin.map {
                    CountryModel(
                        countryName = it.countryName,
                        probability = (it.probability * 100).toInt()
                    )
                }
            )
        }
    }
}

data class CountryModel(
    val countryName: String,
    val probability: Int
)
