package domain.model

data class GuideJsonModel(
    val data: List<GuideSingleJsonModel>
)

data class GuideSingleJsonModel(
    val title: String,
    val description: String? = null,
    val fields: List<GuideFieldModel>? = null
)

data class GuideFieldModel(
    val title: String,
    val content: String
)
