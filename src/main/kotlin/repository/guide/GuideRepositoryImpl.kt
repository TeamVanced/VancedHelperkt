package repository.guide

import domain.model.guide.GuideJsonModel
import network.model.guide.GuideJsonDtoMapper
import network.service.GuideService

class GuideRepositoryImpl(
    private val service: GuideService,
    private val mapper: GuideJsonDtoMapper
) : GuideRepository {

    override suspend fun fetch(jsonName: String): GuideJsonModel {
        return mapper.mapToModel(service.get(jsonName))
    }

}