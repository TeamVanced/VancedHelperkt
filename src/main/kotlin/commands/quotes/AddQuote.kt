package commands.quotes

import commandhandler.CommandContext
import commands.base.BaseCommand
import database.collections.Quote
import database.quotesCollection
import ext.hasQuotePerms
import ext.required
import ext.useArguments
import ext.useCommandProperly
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import org.litote.kmongo.eq
import org.litote.kmongo.findOne
import type.CommandType.Quotes
import java.time.format.DateTimeFormatter

class AddQuote : BaseCommand(
    commandName = "addquote",
    commandDescription = "Add a quote",
    commandType = Quotes,
    commandAliases = listOf("aq"),
    commandArguments = mapOf("Message link | Message ID | #Channel Message ID".required())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        if (ctx.authorAsMember?.hasQuotePerms(guildId) == false) {
            ctx.message.replyMsg("You are not allowed to use this command")
            return
        }
        val args = ctx.args
        val urlRegex = "https://discord(?:app)?.com/channels/\\d+/(?<channel>\\d+)/(?<message>\\d+)".toRegex()
        if (args.isNotEmpty()) {
            when {
                args.size == 1 -> {
                    val args0 = args[0]
                    when {
                        args0.removeSuffix("/").matches(urlRegex) -> {
                            val match = urlRegex.matchEntire(args0)!!.groups
                            createQuote(
                                ctx = ctx,
                                channelID = match["channel"]!!.value,
                                messageID = match["message"]!!.value
                            )
                        }
                        args0.matches(contentIDRegex) -> {
                            createQuote(
                                ctx = ctx,
                                channelID = ctx.channel.id,
                                messageID = args0
                            )
                        }
                        else -> {
                            ctx.message.replyMsg("Use the command properly bruh")
                        }
                    }
                }
                args.filter { it.convertChannelToID().matches(contentIDRegex) }.size == 2 -> {
                    createQuote(
                        ctx = ctx,
                        channelID = args[0].convertChannelToID(),
                        messageID = args[1]
                    )
                }
                else -> {
                    ctx.message.useCommandProperly()
                }
            }
        } else {
            ctx.message.useArguments(1)
        }
    }

    private fun createQuote(ctx: CommandContext, channelID: String, messageID: String) {
        ctx.event.jda.getTextChannelById(channelID)?.retrieveMessageById(messageID)?.queue({ it ->
            if (quotesCollection.findOne(Quote::messageID eq messageID) != null) {
                ctx.message.replyMsg("Pog, that quote is already added :stonks:")
                return@queue
            }

            val quoteid =
                try {
                    quotesCollection.find().last().quoteId
                } catch (e: Exception) {
                    0
                } + 1

            quotesCollection.insertOne(
                Quote(
                    guildID = ctx.guild.id,
                    messageID = messageID,
                    channelID = channelID,
                    messageUrl = it.jumpUrl,
                    messageContent = it.contentRaw,
                    messageTimestamp = it.timeCreated.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    authorID = it.author.id,
                    authorName = it.author.asTag,
                    authorAvatar = it.author.avatarUrl,
                    attachment = it.attachments.firstOrNull()?.url,
                    quoteId = quoteid,
                    stars = mutableListOf()
                )
            )

            ctx.message.replyMsg("Quote #$quoteid successfully created!")
        }, ErrorHandler().handle(ErrorResponse.MISSING_ACCESS) {
            ctx.message.replyMsg("I don't have permissions to view that message")
        }.handle(ErrorResponse.UNKNOWN_MESSAGE) {
            ctx.message.replyMsg("Pretty sure that's not a valid message lul")
        }.handle(ErrorResponse.UNKNOWN_CHANNEL) {
            ctx.message.replyMsg("Pretty sure that's not a valid channel lul")
        }.handle(ErrorResponse.EMPTY_MESSAGE) {
            ctx.message.replyMsg("That's an empty message pal")
        })
    }

    private fun String.convertChannelToID(): String = removePrefix("<#").removeSuffix(">")

}