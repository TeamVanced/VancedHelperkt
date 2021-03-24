package commands.quotes

import commandhandler.CommandContext
import commands.BaseMultipageCommand
import commands.CommandType
import database.collections.Quote
import database.quotesCollection
import ext.getQuote
import ext.required
import ext.sendIncorrectQuote
import net.dv8tion.jda.api.entities.MessageEmbed

class SearchQuote : BaseMultipageCommand<Quote>(
    commandName = "searchquote",
    commandDescription = "Search a quote matching arguments",
    commandType = CommandType.Quotes,
    commandAliases = listOf("search"),
    commandArguments = mapOf("argument".required())
) {

    override fun getMainPage(): MessageEmbed = embedBuilder.apply {
        setTitle("Quotes")
        setDescription("I found these results matching that quote!")
        for (i in itemsList.indices) {
            val item = itemsList[i]
            addField(
                "${emotes[i + 1]} Quote #${item.quoteId} by ${item.authorName} (${item.stars.size} :star:)",
                item.messageContent,
                false
            )
        }
    }.build()

    override fun getPage(item: Quote): MessageEmbed = getQuote(item)

    override fun getItems(args: MutableList<String>): List<Quote> = quotesCollection.find().filter { it.messageContent.contains(args.joinToString(" "), true) }.sortedByDescending { it.stars.size }.take(10)

    override fun handleEmptylist(ctx: CommandContext) = ctx.channel.sendIncorrectQuote()

}