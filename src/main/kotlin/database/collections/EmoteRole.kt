package database.collections

data class EmoteRole(
    val guildId: String,
    val messageId: String,
    val emote: String,
    val roleId: String
)
