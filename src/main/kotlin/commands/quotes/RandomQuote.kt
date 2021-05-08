package commands.quotes

import commandhandler.CommandContext
import commands.base.BaseCommand
import type.CommandType.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.getQuote
import org.litote.kmongo.eq

class RandomQuote : BaseCommand(
    commandName = "randomquote",
    commandDescription = "Get a random quote",
    commandType = Quotes,
    commandAliases = listOf("rq", "random")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val userId = args[0].filter { it.isDigit() }
            if (userId.matches(contentIDRegex)) {
                val quote = quotesCollection.find(Quote::authorID eq userId)
                try {
                    ctx.event.channel.sendMsg(getQuote(quote.toList().random()))
                } catch (e: NoSuchElementException) {
                    ctx.event.channel.sendMsg("There are no quotes in this server from the provided! Try adding some")
                }
            }
        } else {
            try {
                ctx.event.channel.sendMsg(getQuote(quotesCollection.find().toList().random()))
            } catch (e: NoSuchElementException) {
                ctx.event.channel.sendMsg("There are no quotes in this server! Try adding some")
            }
        }


    }

}