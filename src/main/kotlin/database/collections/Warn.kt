package database.collections

data class Warn(
    val guildId: String,
    val userId: String,
    val userName: String,
    val reasons: List<String>
)