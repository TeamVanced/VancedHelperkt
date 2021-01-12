package commands.quotes

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Quotes
import database.quotesCollection
import ext.getQuote

class RandomQuote : BaseCommand(
    commandName = "randomquote",
    commandDescription = "Get a random quote",
    commandType = Quotes,
    commandAliases = listOf("rq")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        try {
            sendMessage(
                embedBuilder.getQuote(quotesCollection.find().toList().random())
            )
        } catch (e: NoSuchElementException) {
            sendMessage("There are no quotes in this server! Try adding some")
        }

    }

}