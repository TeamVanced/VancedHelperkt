package ext

import commands.BaseCommand
import database.collections.Quote
import database.quoteRoles
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

fun EmbedBuilder.sendQuote(quote: Quote, channel: MessageChannel, baseCommand: BaseCommand) {
    with (baseCommand) {
        channel.sendMessage(
            getQuote(quote)
        ).queueAddReaction()
    }
}

fun EmbedBuilder.getQuote(quote: Quote): MessageEmbed {
    return apply {
        setTitle(quote.authorName)
        setDescription(quote.messageContent + "\n\n[Jump to message](${quote.messageUrl})")
        setThumbnail(quote.authorAvatar)
        setImage(quote.attachment)
        setFooter("⭐${quote.stars.size} | ID: ${quote.quoteId} • ${quote.messageTimestamp}")
    }.build()
}

fun BaseCommand.sendIncorrectQuote() {
    channel.sendMessage("That's not a valid quote bro").queueAddReaction()
}

fun Member.hasQuotePerms(guildId: String): Boolean {
    val quoteRoles = guildId.quoteRoles
    return roles.any { quoteRoles.contains(it.id) }
}