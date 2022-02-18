package repository.guide

import domain.model.guide.GuideJsonModel
import network.service.GuideService

interface GuideRepository {

    suspend fun fetch(
        jsonName: String,
        language: String
    ): GuideJsonModel

}

class GuideRepositoryImpl(
    private val service: GuideService,
) : GuideRepository {

    override suspend fun fetch(
        jsonName: String,
        language: String,
    ): GuideJsonModel {
        return GuideJsonModel.fromDto(
            service.get(jsonName, language)
        )
    }
}