package core.listener

import core.command.CommandManager
import core.database.addUserWarn
import core.database.getKeywords
import core.database.getResponseForKeyword
import core.ext.*
import core.util.emoteRegex
import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Message
import org.slf4j.Logger

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
        addUserWarn(
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
        addUserWarn(
            userId = messageAuthor.id.asString,
            userTag = messageAuthor.tag,
            reason = "Emote spam"
        )
        channel.createMessage("${messageAuthor.mention} has been warned for spamming")
        messageAuthor.checkWarnForTooManyInfractions()
    }

    suspend fun autoRespond(message: Message) {
        if (!message.getChannel().isWhitelistedAutoresponseChannel) return

        val messageAuthor = message.getAuthorAsMember() ?: return

        if (messageAuthor.isBot) return

        val keywords = getKeywords()
        val match = keywords.find { message.content.contains(it.keyword) }
        if (match != null) {
            val response = getResponseForKeyword(match.keyword)
            if (response != null) {
                message.reply {
                    content = response.message
                }
            }
        }
    }

    suspend fun runDevCommands(
        message: Message,
        commandManager: CommandManager,
        kord: Kord,
        logger: Logger,
    ) {
        if (message.author?.isDev != true) return

        val commandPrefix = "vh!"

        val messageContent = message.content
        val messageChannel = message.getChannel()

        if (!messageContent.startsWith(commandPrefix)) return

        when (messageContent.substringAfter(commandPrefix)) {
            "regcmd" -> {
                messageChannel.createMessage("Registering all slash commands...")
                commandManager.registerCommands(kord, logger)
                messageChannel.createMessage("Done! Registered slash commands.")
            }
            "cfgcmdperm" -> {
                messageChannel.createMessage("Configuring command permissions...")
                commandManager.configureCommandPermissions(kord, logger)
                messageChannel.createMessage("Done! Configured command permissions.")
            }
            "unregcmd" -> {
                messageChannel.createMessage("Unregistering all slash commands...")
                commandManager.unregisterCommands(kord, logger)
                messageChannel.createMessage("Done! Unregistered slash commands.")
            }
        }
    }

}