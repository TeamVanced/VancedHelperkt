package configuration

import dev.kord.common.entity.Snowflake

data class Config(
    val token: String,
    val guildId: String,
    val coinlibToken: String,
    val mongoString: String,
    val genderToken: String,
) {
    val guildSnowflake = Snowflake(guildId)
}
