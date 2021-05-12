package network.model.guide

import domain.model.guide.GuideFieldModel
import domain.model.guide.GuideJsonModel
import domain.model.guide.GuideSingleJsonModel
import domain.util.EntityMapper

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