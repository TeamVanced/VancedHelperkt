package database.collections

data class Settings(
    val guildId: String,
    val logChannelId: Long = 0L,
    val modLogChannelId: Long = 0L,
    val errorChannelId: Long = 0L,
    val muteRoleId: Long = 0L,
    val boosterRoleId: Long = 0L,
    val ownerIds: List<Long> = listOf(202115709231300617L, 256143257472335872L, 423915768191647755L),
    val modRoleIds: List<Long> = emptyList(),
    val whitelistedSpamChannelIds: List<Long> = emptyList(),
    val allowedQuoteRoleIds: List<Long> = emptyList(),
    val allowedColourMeRoleIds: List<Long> = emptyList(),
)