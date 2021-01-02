package database.collections

data class Quote(
    val guildID: String,
    val messageID: String,
    val channelID: String,
    val messageUrl: String,
    val messageContent: String,
    val messageTimestamp: String,
    val authorID: String,
    val authorAvatar: String?,
    val authorName: String,
    val attachment: String?,
    val quoteId: Long,
    val stars: MutableList<String>
)
