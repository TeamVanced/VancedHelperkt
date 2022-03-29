package core.database.collections

data class Quote(
    val guildId: String,
    val messageId: String,
    val channelId: String,
    val messageUrl: String,
    val messageContent: String,
    val messageTimestamp: String,
    val authorId: String,
    val authorAvatar: String,
    val authorName: String,
    val attachment: String?,
    val quoteId: Long,
    val stars: List<String>
)
