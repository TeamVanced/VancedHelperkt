package repository.gender

import domain.model.gender.GenderModel
import network.model.gender.GenderDtoMapper
import network.service.GenderService
class GenderRepositoryImpl(
    private val service: GenderService,
    private val mapper: GenderDtoMapper
) : GenderRepository {

    override suspend fun get(token: String, name: String): GenderModel {
        return mapper.mapToModel(
            service.get(
                token = token,
                name = name
            )
        )
    }

}