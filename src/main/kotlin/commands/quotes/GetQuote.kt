package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandTypes.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.sendIncorrectQuote
import ext.sendQuote
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
        val args = ctx.args.apply { remove("get") }
        val contentIDRegex = "\\b\\d{18}\\b".toRegex()
        val guildFilter = BasicDBObject().append("guildID", ctx.guild.id)
        if (args.isNotEmpty()) {
            val message = args[0]
            when {
                message.matches(contentIDRegex) -> quotesCollection.findOne(guildFilter.append("messageID", message))
                    .getQuote()
                message.toLongOrNull() != null -> quotesCollection.findOne(
                    guildFilter.append(
                        "quoteId",
                        message.toLong()
                    )
                ).getQuote()
                else -> {
                    val quotes = quotesCollection.find().filter { it.messageContent.contains(message, true) }
                    if (quotes.isNotEmpty()) {
                        if (quotes.size > 1) {
                            if (quotes.size < 10) {
                                quotes.sendQuotes()
                            } else {
                                channel.sendMessage("Too many quotes matching this search!").queueAddReaction()
                            }
                        } else {
                            quotes[0].getQuote()
                        }
                    } else {
                        channel.sendIncorrectQuote(this)
                    }
                }
            }
        } else {
            commandManager.getCommand("randomquote")?.execute(ctx)
        }
    }

    private fun Quote?.getQuote() {
        if (this != null) {
            embedBuilder.sendQuote(this, channel, this@GetQuote)
        } else {
            channel.sendIncorrectQuote(this@GetQuote)
        }
    }

    private fun List<Quote>.sendQuotes() {
        channel.sendMessage(
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
        ).queue {
            messageId = it.id
        }
    }
}