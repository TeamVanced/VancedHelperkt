package ext

import commands.BaseCommand
import database.collections.Quote
import database.quoteRoles
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

fun BaseCommand.sendQuote(quote: Quote, channel: TextChannel) {
    channel.sendMsg(
        getQuote(quote)
    )
}

fun BaseCommand.getQuote(quote: Quote): MessageEmbed {
    return embedBuilder.apply {
        val jumpTo = "\n\n[Jump to message](${quote.messageUrl})"

        setTitle(quote.authorName)
        setDescription(quote.messageContent.takeMax(2048 - jumpTo.length) + jumpTo)
        setThumbnail(quote.authorAvatar)
        setImage(quote.attachment)
        setFooter("⭐${quote.stars.size} | ID: ${quote.quoteId} • ${quote.messageTimestamp}")
    }.build()
}

fun TextChannel.sendIncorrectQuote() {
    sendMessageWithChecks("That's not a valid quote bro")
}

fun Member.hasQuotePerms(guildId: String): Boolean {
    val quoteRoles = guildId.quoteRoles
    return roles.any { quoteRoles.contains(it.id) }
}

fun BaseCommand.getQuote(quote: Quote?, channel: TextChannel) {
    if (quote != null) {
        sendQuote(quote, channel)
    } else {
        channel.sendIncorrectQuote()
    }
}