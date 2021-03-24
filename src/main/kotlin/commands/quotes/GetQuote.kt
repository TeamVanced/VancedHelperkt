package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandType.Quotes
import database.quotesCollection
import ext.getQuote
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
                message.matches(contentIDRegex) -> getQuote(quotesCollection.findOne(guildFilter.append("messageID", message)), ctx.channel)
                message.toLongOrNull() != null -> getQuote(
                    quotesCollection.findOne(
                        guildFilter.append(
                            "quoteId",
                            message.toLong()
                        )
                    ),
                    ctx.channel
                )
                else -> commandManager.getCommand("searchquote")?.execute(ctx)
            }
        } else {
            commandManager.getCommand("randomquote")?.execute(ctx)
        }
    }

}