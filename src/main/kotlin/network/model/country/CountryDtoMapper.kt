package network.model.country

import domain.model.country.CountryModel
import domain.model.country.CountryOfOriginModel
import domain.util.EntityMapper
class CountryDtoMapper : EntityMapper<CountryOfOriginDto, CountryOfOriginModel> {

    override suspend fun mapToModel(entity: CountryOfOriginDto): CountryOfOriginModel {
        return with (entity) {
            CountryOfOriginModel(
                countries = countryOrigin.map {
                    CountryModel(
                        countryName = it.countryName,
                        probability = (it.probability * 100).toInt()
                    )
                }
            )
        }
    }

}