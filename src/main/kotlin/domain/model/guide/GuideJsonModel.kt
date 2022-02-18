package domain.model.guide

import network.model.guide.GuideJsonDto

data class GuideJsonModel(
    val data: List<GuideSingleJsonModel>
) {
    companion object {
        fun fromDto(dto: GuideJsonDto): GuideJsonModel {
            return GuideJsonModel(
                data = dto.data.map { jsonDto ->
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

data class GuideSingleJsonModel(
    val title: String,
    val description: String? = null,
    val fields: List<GuideFieldModel>? = null
)

data class GuideFieldModel(
    val title: String,
    val content: String
)
