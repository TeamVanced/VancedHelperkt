package repository.genderapi

import domain.model.genderapi.CountryOfOriginModel
import domain.model.genderapi.GenderModel
import network.service.GenderapiService

interface GenderapiRepository {

    suspend fun getGender(
        token: String,
        name: String
    ): GenderModel

    suspend fun getCountryOfOrigin(
        token: String,
        name: String
    ): CountryOfOriginModel

}

class GenderapiRepositoryImpl(
    private val service: GenderapiService,
) : GenderapiRepository {

    override suspend fun getGender(
        token: String,
        name: String
    ): GenderModel {
        return GenderModel.fromDto(
            service.getGender(
                token = token,
                name = name
            )
        )
    }

    override suspend fun getCountryOfOrigin(
        token: String,
        name: String
    ): CountryOfOriginModel {
        return CountryOfOriginModel.fromDto(
            service.getCountryOfOrigin(
                token = token,
                name = name
            )
        )
    }
}