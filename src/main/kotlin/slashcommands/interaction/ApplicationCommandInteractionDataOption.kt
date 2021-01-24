package interaction

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationCommandInteractionDataOption(
    val id: String,
    val value: String?,
    val options: List<ApplicationCommandInteractionDataOption>?
)
