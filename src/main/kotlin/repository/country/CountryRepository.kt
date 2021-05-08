package repository.country

import domain.model.country.CountryOfOriginModel

interface CountryRepository {

    suspend fun get(token: String, name: String): CountryOfOriginModel

}