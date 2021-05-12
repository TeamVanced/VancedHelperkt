package network.model.gender

import domain.model.gender.GenderModel
import domain.util.EntityMapper

class GenderDtoMapper : EntityMapper<GenderDto, GenderModel> {

    override suspend fun mapToModel(entity: GenderDto): GenderModel {
        return with (entity) {
            GenderModel(
                gender = gender,
                accuracy = accuracy
            )
        }
    }

}