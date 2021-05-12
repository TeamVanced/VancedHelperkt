package domain.model.guide

data class GuideSingleJsonModel(
    val title: String,
    val description: String? = null,
    val fields: List<GuideFieldModel>? = null
)
