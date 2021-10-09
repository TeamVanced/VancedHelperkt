package core.database.collections

data class Settings(
    val guildId: String,
    val logChannelId: ULong = 0UL,
    val modLogChannelId: ULong = 0UL,
    val errorChannelId: ULong = 0UL,
    val muteRoleId: ULong = 0UL,
    val boosterRoleId: ULong = 0UL,
    val modRoleIds: List<ULong> = emptyList(),
    val whitelistedSpamChannelIds: List<ULong> = emptyList(),
    val allowedQuoteRoleIds: List<ULong> = emptyList(),
    val allowedColourMeRoleIds: List<ULong> = emptyList(),
)