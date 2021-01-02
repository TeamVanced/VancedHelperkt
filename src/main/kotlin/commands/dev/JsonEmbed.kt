package commands.dev

data class JsonEmbed(
    val title: String,
    val description: String? = null,
    val fields: List<EmbedField>? = emptyList(),
    val thumbnail: String? = null,
    val image: String? = null,
    val color: String? = null,
    val footer: String? = null
)

data class EmbedField(
    val title: String,
    val content: String
)
