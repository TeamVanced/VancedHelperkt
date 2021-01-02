package commands.quotes

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Quotes
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
            channel.sendMessage(
                embedBuilder.getQuote(quotesCollection.find().toList().random())
            ).queue {
                messageId = it.id
            }
        } catch (e: NoSuchElementException) {
            channel.sendMessage("There are no quotes in this server! Try adding some").queue {
                messageId = it.id
            }
        }

    }

}