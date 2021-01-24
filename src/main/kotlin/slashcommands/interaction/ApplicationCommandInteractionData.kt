package interaction

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationCommandInteractionData(
    val id: String,
    val name: String,
    val options: List<ApplicationCommandInteractionDataOption>?
)
