package interaction

import core.Member
import core.Snowflake
import kotlinx.serialization.Serializable

@Serializable
data class Interaction(
    val id: Snowflake,
    val type: InteractionType,
    val data: ApplicationCommandInteractionData? = null,
    val guild_id: Snowflake,
    val channel_id: Snowflake,
    val member: Member,
    val token: String,
    val version: String
)