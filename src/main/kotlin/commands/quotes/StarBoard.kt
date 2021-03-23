package commands.quotes

import commandhandler.CommandContext
import commands.BaseMultipageCommand
import commands.CommandType.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.getQuote
import net.dv8tion.jda.api.entities.MessageEmbed
import org.litote.kmongo.eq

class StarBoard : BaseMultipageCommand<Quote>(
    commandName = "starboard",
    commandDescription = "Get most starred quotes",
    commandType = Quotes,
    commandAliases = listOf("sb"),
) {

    override fun getMainPage(): MessageEmbed = embedBuilder.apply {
        setTitle("Starboard")
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

    override fun getItems(args: MutableList<String>): List<Quote> = quotesCollection.find(Quote::guildID eq guildId).filter { it.stars.size > 0 }.sortedByDescending { it.stars.size }.take(10)

    override fun handleEmptylist(ctx: CommandContext) {
        ctx.channel.sendMessage("Quotes are empty! try starring some").queue()
    }

}