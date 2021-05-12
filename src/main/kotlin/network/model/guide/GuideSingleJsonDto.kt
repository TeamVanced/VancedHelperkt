package network.model.guide

data class GuideSingleJsonDto(
    val title: String,
    val description: String? = null,
    val fields: List<GuideFieldDto>? = null
)
