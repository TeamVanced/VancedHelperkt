package utils

import commands.base.BaseCommand
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

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
            command.commandMessage[event.channel] = it
            emotes.forEach { emote ->
                event.channel.addReactionById(it.id, emote).queue()
            }
        }
    }

    /**
     * @param message K: Message channel; V: message ID
     */
    private fun editPage(message: Pair<MessageChannel, String?>) {
        val messageId = message.second
        if (messageId != null) {
            message.first.editMessageById(messageId, embeds[currentPage]).queue(null, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
        }
    }

    fun nextPage(message: Pair<MessageChannel, String?>) {
        if (currentPage != embeds.size - 1) {
            currentPage++
            editPage(message)
        }
    }

    fun lastPage(message: Pair<MessageChannel, String?>) {
        currentPage = embeds.size - 1
        editPage(message)
    }

    fun previousPage(message: Pair<MessageChannel, String?>) {
        if (currentPage != 0) {
            currentPage--
            editPage(message)
        }
    }

    fun firstPage(message: Pair<MessageChannel, String?>) {
        currentPage = 0
        editPage(message)
    }

}