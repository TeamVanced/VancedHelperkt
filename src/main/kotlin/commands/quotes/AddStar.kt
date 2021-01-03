package commands.quotes

import com.mongodb.client.MongoCollection
import com.mongodb.client.model.Updates
import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Quotes
import database.collections.Quote
import database.quotesCollection
import ext.sendIncorrectQuote
import ext.useCommandProperly
import org.bson.conversions.Bson
import org.litote.kmongo.*

class AddStar : BaseCommand(
    commandName = "addstar",
    commandDescription = "Star a quote",
    commandType = Quotes,
    commandArguments = listOf("<Quote ID | Message ID>"),
    commandAliases = listOf("as")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args.apply { remove("addstar") }
        val contentIDRegex = "\\b\\d{18}\\b".toRegex()
        if (args.isNotEmpty()) {
            val message = args[0]
            when {
                message.matches(contentIDRegex) -> quotesCollection.addStar(ctx, Quote::messageID eq message)
                message.toLongOrNull() != null -> quotesCollection.addStar(ctx, Quote::quoteId eq message.toLong())
                else -> ctx.channel.sendIncorrectQuote(this)
            }
        } else {
            ctx.channel.useCommandProperly(this)
        }
    }

    private fun MongoCollection<Quote>.addStar(ctx: CommandContext, filter: Bson) {
        val authorId = ctx.author.id
        val quote = findOne(filter)
        if (quote != null) {
            if (quote.stars.contains(authorId)) {
                ctx.channel.sendMessage("Bruh you already starred this").queueAddReaction()
            } else {
                quotesCollection.updateOne(filter, Updates.push("stars", authorId))
                ctx.channel.sendMessage("Successfully starred quote #${quote.quoteId}").queueAddReaction()
            }
        } else {
            ctx.channel.sendIncorrectQuote(this@AddStar)
        }
    }

}