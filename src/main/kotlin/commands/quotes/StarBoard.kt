package commands.quotes

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.getQuote
import ext.sendMsg
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import org.litote.kmongo.eq

class StarBoard : BaseCommand(
    commandName = "starboard",
    commandDescription = "Get most starred quotes",
    commandType = Quotes,
    commandAliases = listOf("sb"),
    addTrashCan = false
) {

    private var sbquotes = emptyList<Quote>()
    private val emotes = arrayOf("🔢", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟")

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        sbquotes = quotesCollection.find(Quote::guildID eq guildId).filter { it.stars.size > 0 }.sortedByDescending { it.stars.size }.take(10)
        if (sbquotes.isEmpty()) {
            sendMessage("Quotes not found, try adding some")
            return
        }
        sendStarboard()
    }

    override fun onReactionAdd(event: MessageReactionAddEvent) {
        super.onReactionAdd(event)
        if (event.userId != commandAuthorId)
            return
        if (event.reactionEmote.asReactionCode == emotes[0]) {
            channel.editMessageById(messageId, getStarboard()).queue()
            return
        }
        if (emotes.contains(event.reactionEmote.asReactionCode))
            channel.editMessageById(
                messageId,
                embedBuilder.getQuote(sbquotes[emotes.indexOf(event.reactionEmote.asReactionCode) - 1])
            ).queue()
    }

    private fun getStarboard(): MessageEmbed = embedBuilder.apply {
        setTitle("Starboard")
        for (i in sbquotes.indices) {
            addField(
                "${emotes[i + 1]} Quote #${sbquotes[i].quoteId} by ${sbquotes[i].authorName} (${sbquotes[i].stars.size} :star:)",
                sbquotes[i].messageContent,
                false
            )
        }
    }.build()

    private fun sendStarboard() {
        channel.sendMsg(
            getStarboard()
        ) { message ->
            messageId = message.id
            (0..sbquotes.size).forEach {
                message.addReaction(emotes[it]).queue()
            }
            channel.addReactionById(message.id, trashEmote).queue()
        }

    }

}