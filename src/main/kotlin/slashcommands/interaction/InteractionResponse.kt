package slashcommands.interaction

import kotlinx.serialization.Serializable

@Serializable
data class InteractionResponse(
    val type: InteractionResponseType,
    val data: InteractionApplicationCommandCallbackData? = null
)