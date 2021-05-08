package repository

import domain.model.GuideJsonModel
import network.JsonService
import network.model.GuideJsonDtoMapper

class JsonRepositoryImpl(
    private val service: JsonService,
    private val guideJsonDtoMapper: GuideJsonDtoMapper
) : JsonRepository {

    override suspend fun fetch(jsonName: String): GuideJsonModel {
        return guideJsonDtoMapper.mapToModel(service.get(jsonName))
    }

}