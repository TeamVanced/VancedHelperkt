package ext

import commands.base.BaseCommand
import database.collections.Quote
import database.quoteRoles
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed

fun BaseCommand.sendQuote(quote: Quote, message: Message) {
    message.replyWithChecks(
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

fun Message.sendIncorrectQuote() {
    replyWithChecks("That's not a valid quote bro")
}

fun Member.hasQuotePerms(guildId: String): Boolean {
    val quoteRoles = guildId.quoteRoles
    return roles.any { quoteRoles.contains(it.id) }
}

fun BaseCommand.getQuote(quote: Quote?, message: Message) {
    if (quote != null) {
        sendQuote(quote, message)
    } else {
        message.sendIncorrectQuote()
    }
}