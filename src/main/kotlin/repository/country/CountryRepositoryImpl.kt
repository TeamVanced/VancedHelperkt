package repository.country

import domain.model.country.CountryOfOriginModel
import network.model.country.CountryDtoMapper
import network.service.CountryService
class CountryRepositoryImpl(
    private val service: CountryService,
    private val mapper: CountryDtoMapper
) : CountryRepository {

    override suspend fun get(token: String, name: String): CountryOfOriginModel {
        return mapper.mapToModel(
            service.get(
                token = token,
                name = name
            )
        )
    }

}