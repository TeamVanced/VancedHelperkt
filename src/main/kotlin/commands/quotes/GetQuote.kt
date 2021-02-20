package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandType.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.sendIncorrectQuote
import ext.sendQuote
import net.dv8tion.jda.api.entities.TextChannel
import org.litote.kmongo.findOne

class GetQuote(
    private val commandManager: CommandManager
) : BaseCommand(
    commandName = "getquote",
    commandDescription = "Get a quote",
    commandType = Quotes,
    commandAliases = listOf("q")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        val contentIDRegex = "\\b\\d{18}\\b".toRegex()
        val guildFilter = BasicDBObject().append("guildID", ctx.guild.id)
        if (args.isNotEmpty()) {
            val message = args[0]
            when {
                message.matches(contentIDRegex) -> quotesCollection.findOne(guildFilter.append("messageID", message)).getQuote(ctx.channel)
                message.toLongOrNull() != null -> quotesCollection.findOne(
                    guildFilter.append(
                        "quoteId",
                        message.toLong()
                    )
                ).getQuote(ctx.channel)
                else -> {
                    val quotes = quotesCollection.find().filter { it.messageContent.contains(args.joinToString(" "), true) }
                    if (quotes.isNotEmpty()) {
                        if (quotes.size > 1) {
                            if (quotes.size < 10) {
                                quotes.sendQuotes(ctx.channel)
                            } else {
                                ctx.event.channel.sendMsg("Too many quotes matching this search!")
                            }
                        } else {
                            quotes[0].getQuote(ctx.channel)
                        }
                    } else {
                        ctx.channel.sendIncorrectQuote()
                    }
                }
            }
        } else {
            commandManager.getCommand("randomquote")?.execute(ctx)
        }
    }

    private fun Quote?.getQuote(channel: TextChannel) {
        if (this != null) {
            sendQuote(this, channel)
        } else {
            channel.sendIncorrectQuote()
        }
    }

    private fun List<Quote>.sendQuotes(channel: TextChannel) {
        channel.sendMsg(
            embedBuilder.apply {
                setTitle("Quotes")
                setDescription("I found multiple results matching that quote!")
                forEach {
                    addField(
                        "Quote #${it.quoteId} by ${it.authorName}",
                        it.messageContent,
                        false
                    )
                }
            }.build()
        )
    }
}