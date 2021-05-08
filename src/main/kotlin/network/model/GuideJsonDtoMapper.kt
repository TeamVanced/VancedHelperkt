package network.model

import domain.model.GuideFieldModel
import domain.model.GuideJsonModel
import domain.model.GuideSingleJsonModel
import network.EntityMapper

class GuideJsonDtoMapper : EntityMapper<GuideJsonDto, GuideJsonModel> {

    override suspend fun mapToModel(entity: GuideJsonDto): GuideJsonModel {
        return with (entity) {
            GuideJsonModel(
                data = data.map { jsonDto ->
                    GuideSingleJsonModel(
                        jsonDto.title,
                        jsonDto.description,
                        jsonDto.fields?.map { fieldDto ->
                            GuideFieldModel(
                                fieldDto.title,
                                fieldDto.content
                            )
                        }
                    )
                }
            )
        }
    }

}