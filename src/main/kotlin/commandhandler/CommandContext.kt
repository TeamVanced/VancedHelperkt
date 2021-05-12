package commandhandler

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

data class CommandContext(
    val event: GuildMessageReceivedEvent,
    val args: MutableList<String>
) {
    val channel get() = event.channel
    val author get() = event.author
    val authorAsMember get() = event.guild.getMemberById(author.id)
    val guild get() = event.guild
    val message get() = event.message
}