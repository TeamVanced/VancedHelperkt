package utils

import commands.BaseCommand
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent

class EmbedPagerAdapter(
    private val command: BaseCommand,
    private val event: GuildMessageReceivedEvent,
    private val emotes: List<String>,
    private val embeds: List<MessageEmbed>
) {
    private var currentPage = 0

    fun newInstance(position: Int = 0) {
        currentPage = position
        event.channel.sendMessage(embeds[currentPage]).queue {
            command.messageId = it.id
            emotes.forEach { emote ->
                event.channel.addReactionById(it.id, emote).queue()
            }
        }
    }

    private fun editPage() {
        event.channel.editMessageById(command.messageId, embeds[currentPage]).queue()
    }

    fun nextPage() {
        if (currentPage != embeds.size - 1) {
            currentPage++
            editPage()
        }
    }

    fun lastPage() {
        currentPage = embeds.size - 1
        editPage()
    }

    fun previousPage() {
        if (currentPage != 0) {
            currentPage--
            editPage()
        }
    }

    fun firstPage() {
        currentPage = 0
        editPage()
    }

}