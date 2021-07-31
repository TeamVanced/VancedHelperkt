package database.collections

data class Settings(
    val guildID: String,
    val logChannelId: Long = 0,
    val modlogChannelId: Long = 0,
    val boosterChannelId: Long = 0,
    val boosterRoleId: Long = 0,
    val errorChannelId: Long = 0,
    val muteRoleId: Long = 0,
    val ownerIds: List<Long> = listOf(202115709231300617, 256143257472335872, 423915768191647755),
    val modRoleIds: List<Long> = emptyList(),
    val allowedQuoteRoleIds: List<Long> = emptyList(),
    val allowedColourmeRoleIds: List<Long> = emptyList()
)