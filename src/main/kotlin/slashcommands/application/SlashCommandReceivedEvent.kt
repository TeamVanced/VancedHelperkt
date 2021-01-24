package application

import interaction.Interaction
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class SlashCommandReceivedEvent(json: String) {

    private val deserializedJson = Json.decodeFromString<Interaction>(json)

    val type get() = deserializedJson.type
    val data get() = deserializedJson.data
    val guildId get() = deserializedJson.guild_id
    val channelId get() = deserializedJson.channel_id
    val member get() = deserializedJson.member

}
