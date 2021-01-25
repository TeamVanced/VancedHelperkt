package slashcommands.application

import slashcommands.interaction.Interaction

class SlashCommandReceivedEvent(private val interaction: Interaction) {

    val type get() = interaction.type
    val data get() = interaction.data
    val guildId get() = interaction.guild_id
    val channelId get() = interaction.channel_id
    val member get() = interaction.member

}
