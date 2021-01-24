package slashcommands.interaction

import kotlinx.serialization.Serializable
import net.dv8tion.jda.api.entities.ISnowflake

@Serializable
data class AllowedMentionStructure(
    val parse: AllowedMentionTypes,
    val roles: List<ISnowflake>,
    val users: List<ISnowflake>,
    val replied_user: Boolean
)
