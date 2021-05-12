package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commands.base.BaseCommand
import database.quotesCollection
import ext.getQuote
import org.litote.kmongo.findOne
import type.CommandType.Quotes

class GetQuote : BaseCommand(
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
                message.matches(contentIDRegex) -> getQuote(quotesCollection.findOne(guildFilter.append("messageID", message)), ctx.message)
                message.toLongOrNull() != null -> getQuote(
                    quotesCollection.findOne(
                        guildFilter.append(
                            "quoteId",
                            message.toLong()
                        )
                    ),
                    ctx.message
                )
                else -> commandManager.getCommand("searchquote")?.execute(ctx)
            }
        } else {
            commandManager.getCommand("randomquote")?.execute(ctx)
        }
    }

}