package commands.quotes

import com.mongodb.BasicDBObject
import commandhandler.CommandContext
import commands.base.BaseCommand
import database.collections.Quote
import database.quotesCollection
import ext.*
import net.dv8tion.jda.api.entities.Message
import type.CommandType.Quotes

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
            ctx.message.replyMsg("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        if (args.isNotEmpty()) {
            val args0 = args[0]
            val guildFilter = BasicDBObject().append("guildID", ctx.guild.id)
            when {
                args0.matches(contentIDRegex) -> quotesCollection.findOneAndDelete(guildFilter.append("messageID", args0))
                    .checkDelete(ctx.message)
                args0.toLongOrNull() != null -> quotesCollection.findOneAndDelete(
                    guildFilter.append(
                        "quoteId",
                        args0.toLong()
                    )
                ).checkDelete(ctx.message)
                else -> ctx.message.useCommandProperly()
            }
        } else {
            ctx.message.useArguments(1)
        }
    }

    private fun Quote?.checkDelete(channel: Message) {
        if (this == null) {
            channel.sendIncorrectQuote()
        } else {
            channel.replyMsg("Quote $quoteId deleted successfully!")
        }
    }

}
