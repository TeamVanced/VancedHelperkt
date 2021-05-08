package commands.quotes

import commandhandler.CommandContext
import commands.base.BaseMultipageCommand
import database.collections.Quote
import database.quotesCollection
import ext.addQuoteFields
import ext.getQuote
import net.dv8tion.jda.api.entities.MessageEmbed
import org.litote.kmongo.eq
import type.CommandType.Quotes

class StarBoard : BaseMultipageCommand<Quote>(
    commandName = "starboard",
    commandDescription = "Get most starred quotes",
    commandType = Quotes,
    commandAliases = listOf("sb"),
) {

    override fun getMainPage(): MessageEmbed = embedBuilder.apply {
        setTitle("Starboard")
        addQuoteFields(emotes, itemsList)
    }.build()

    override fun getPage(item: Quote): MessageEmbed = getQuote(item)

    override fun getItems(args: MutableList<String>): List<Quote> = quotesCollection.find(Quote::guildID eq guildId).filter { it.stars.size > 0 }.sortedByDescending { it.stars.size }.take(10)

    override fun handleEmptylist(ctx: CommandContext) {
        ctx.channel.sendMessage("Quotes are empty! try starring some").queue()
    }

}