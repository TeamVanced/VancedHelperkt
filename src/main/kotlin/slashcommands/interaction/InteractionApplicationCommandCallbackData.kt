package slashcommands.interaction

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.MessageEmbed

@Serializable
data class InteractionApplicationCommandCallbackData(
    val tts: Boolean = false,
    val content: String,
    val embeds: List<@Contextual MessageEmbed>,
    val allowed_mentions: AllowedMentionStructure
)