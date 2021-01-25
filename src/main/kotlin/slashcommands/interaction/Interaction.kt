package slashcommands.interaction

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.ISnowflake
import net.dv8tion.jda.api.entities.Member

@Serializable
data class Interaction(
    val id: ISnowflake,
    val type: InteractionType,
    val data: ApplicationCommandInteractionData? = null,
    val guild_id: ISnowflake,
    val channel_id: ISnowflake,
    val member: Member,
    val token: String,
    val version: String
)