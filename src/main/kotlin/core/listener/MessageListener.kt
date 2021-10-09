package core.listener

import core.command.CommandManager
import core.util.emoteRegex
import core.database.warnUser
import dev.kord.core.Kord
import dev.kord.core.entity.Message
import core.ext.checkWarnForTooManyInfractions
import core.ext.isMod
import core.ext.isWhitelistedSpamChannel
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

    suspend fun runDevCommands(
        message: Message,
        commandManager: CommandManager,
        kord: Kord,
        logger: Logger,
    ) {
        val commandPrefix = "vh!"

        val messageContent = message.content
        val messageChannel = message.getChannel()

        if (!messageContent.startsWith(commandPrefix)) return

        when (messageContent.substringAfter(commandPrefix)) {
            "registercommands" -> {
                messageChannel.createMessage("Registering all slash commands...")
                commandManager.registerCommands(kord, logger)
                messageChannel.createMessage("Done! Registered slash commands.")
                messageChannel.createMessage("Configuring command permissions...")
                commandManager.configureCommandPermissions(kord, logger)
                messageChannel.createMessage("Done! Configured command permissions.")
            }
            "unregistercommands" -> {
                messageChannel.createMessage("Unregistering all slash commands...")
                commandManager.unregisterCommands(kord, logger)
                messageChannel.createMessage("Done! Unregistered slash commands.")
            }
        }
    }

}