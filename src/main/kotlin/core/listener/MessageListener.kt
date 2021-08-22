package core.listener

import core.util.emoteRegex
import database.warnUser
import dev.kord.core.entity.Message
import ext.checkWarnForTooManyInfractions
import ext.isMod
import ext.isWhitelistedSpamChannel

class MessageListener {

    suspend fun filterMessageSpam(
        message: Message
    ) {
        val channel = message.getChannel()
        if (channel.isWhitelistedSpamChannel) return

        val messageAuthor = message.getAuthorAsMember() ?: return
        if (messageAuthor.isBot || messageAuthor.isMod) return

        val words = message.content
            .replace(".", " ")
            .replace(",", " ")
            .replace("\n", " ")
            .split("\\s+".toRegex())

        var duplicateCount = 0

        for (i in 1 until words.size) {
            if (words[i].equals(words[i - 1], ignoreCase = true)) {
                duplicateCount++
            } else {
                duplicateCount = 0
            }
        }

        if (duplicateCount < 6) return

        message.delete("Spam")
        warnUser(
            userId = messageAuthor.id.asString,
            userTag = messageAuthor.tag,
            reason = "Spam"
        )
        channel.createMessage("${messageAuthor.mention} has been warned for spamming")
        messageAuthor.checkWarnForTooManyInfractions()
    }

    suspend fun filterSingleMessageEmoteSpam(
        message: Message
    ) {
        val channel = message.getChannel()
        if (channel.isWhitelistedSpamChannel) return

        val messageAuthor = message.getAuthorAsMember() ?: return

        if (messageAuthor.isBot || messageAuthor.isMod) return

        val emotes = emoteRegex.findAll(message.content)

        if (emotes.count() < 6) return

        message.delete("Spam")
        warnUser(
            userId = messageAuthor.id.asString,
            userTag = messageAuthor.tag,
            reason = "Emote spam"
        )
        channel.createMessage("${messageAuthor.mention} has been warned for spamming")
        messageAuthor.checkWarnForTooManyInfractions()
    }

}