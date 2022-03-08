package core.database.collections

data class Settings(
    val guildId: String,
    val logChannelId: Long = 0L,
    val modLogChannelId: Long = 0L,
    val errorChannelId: Long = 0L,
    val muteRoleId: Long = 0L,
    val boosterRoleId: Long = 0L,
    val modRoleIds: List<Long> = emptyList(),
    val whitelistedSpamChannelIds: List<Long> = emptyList(),
    val whitelistedAutoresponsesChannelIds: List<Long> = emptyList(),
    val allowedQuoteRoleIds: List<Long> = emptyList(),
    val allowedColourMeRoleIds: List<Long> = emptyList(),
)