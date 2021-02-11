package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.*
import net.dv8tion.jda.api.entities.TextChannel

class RemoveQuote : BaseCommand(
    commandName = "removequote",
    commandDescription = "Remove a quote",
    commandType = Quotes,
    commandArguments = mapOf("Quote ID | Message ID".optional()),
    commandAliases = listOf("rmquote", "rmq", "dq")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        if (ctx.authorAsMember?.hasQuotePerms(guildId) == false) {
            ctx.event.channel.sendMsg("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        if (args.isNotEmpty()) {
            val args0 = args[0]
            val guildFilter = BasicDBObject().append("guildID", ctx.guild.id)
            when {
                args0.matches(contentIDRegex) -> quotesCollection.findOneAndDelete(guildFilter.append("messageID", args0))
                    .checkDelete(ctx.channel)
                args0.toLongOrNull() != null -> quotesCollection.findOneAndDelete(
                    guildFilter.append(
                        "quoteId",
                        args0.toLong()
                    )
                ).checkDelete(ctx.channel)
                else -> ctx.channel.useCommandProperly()
            }
        } else {
            ctx.channel.useArguments(1)
        }
    }

    private fun Quote?.checkDelete(channel: TextChannel) {
        if (this == null) {
            channel.sendIncorrectQuote()
        } else {
            channel.sendMsg("Quote $quoteId deleted successfully!")
        }
    }

}
