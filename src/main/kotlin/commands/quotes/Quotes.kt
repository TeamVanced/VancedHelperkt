package commands.quotes

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.*
import core.database.collections.Quote
import core.ext.isQuoter
import core.ext.takeMax
import core.util.EMOTE_STONKS
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import java.text.SimpleDateFormat
import java.util.*

class Quotes : BaseCommand(
    commandName = "quote",
    commandDescription = "Quote actions"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val subCommand = ctx.subCommand ?: return

        when (subCommand.name) {
            "add" -> addQuote(ctx)
            "get" -> getQuote(ctx)
            "search" -> searchQuote(ctx)
            "random" -> getRandomQuote(ctx)
            "remove" -> removeQuote(ctx)
            "star" -> starQuote(ctx)
            "unstar" -> unstarQuote(ctx)
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                subCommand(
                    name = "add",
                    description = "Add a quote",
                    builder = {
                        string(
                            name = "link",
                            description = "message link",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "get",
                    description = "Retrieve a quote",
                    builder = {
                        int(
                            name = "quoteid",
                            description = "ID of the quote",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "random",
                    description = "Get a random quote"
                )
                subCommand(
                    name = "remove",
                    description = "Remove a quote",
                    builder = {
                        int(
                            name = "quoteid",
                            description = "ID of the quote",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "search",
                    description = "Search a quote",
                    builder = {
                        string(
                            name = "keyword",
                            description = "Filter for the search",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "star",
                    description = "Star a quote",
                    builder = {
                        int(
                            name = "quoteid",
                            description = "ID of the quote",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "unstar",
                    description = "Unstar a quote",
                    builder = {
                        int(
                            name = "quoteid",
                            description = "ID of the quote",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
            }
        )

    private suspend fun addQuote(ctx: CommandContext) {
        if (!ctx.author.isQuoter) {
            return ctx.respondEphemeral {
                content = "You're not allowed to execute this command"
            }
        }

        val linkRegex =
            """https://(?:\w+.)?discord(?:app)?.com/channels/\d+/(?<channelId>\d+)/(?<messageId>\d+)""".toRegex()

        val link = ctx.args.strings["link"]!!

        if (!linkRegex.matches(link)) {
            ctx.respondPublic {
                content = "Link was not provided"
            }
            return
        }

        val groups = linkRegex.find(link)!!.groups

        val messageId = groups["messageId"]!!.value
        val channelId = groups["channelId"]!!.value

        if (quoteExists(messageId)) {
            ctx.respondPublic {
                content = "Pog, that quote is already added $EMOTE_STONKS"
            }
            return
        }

        val message = ctx.channel.supplier.getMessageOrNull(Snowflake(channelId), Snowflake(messageId))

        if (message == null) {
            ctx.respondPublic {
                content = "Message not found"
            }
            return
        }

        if (message.author?.isBot != false) {
            return ctx.respondEphemeral {
                content = "Beep Boop. Do not bully robots"

                addFile("GLaDOS.mp3", javaClass.getResourceAsStream("/robots.mp3")!!)
            }
        }

        val quoteId = (lastQuote?.quoteId ?: 0) + 1

        addQuote(
            Quote(
                guildId = config.guildId,
                messageId = messageId,
                channelId = channelId,
                messageUrl = link.replace("""(?:\w+.)?discord(?:app)?.com""".toRegex(), "discord.com"),
                messageContent = message.content,
                messageTimestamp = SimpleDateFormat("dd/MM/yyyy").format(Date(message.timestamp.epochSeconds)),
                authorId = message.author!!.id.toString(),
                authorAvatar = message.author!!.avatar!!.url,
                authorName = message.author!!.tag,
                attachment = message.attachments.firstOrNull()?.url,
                quoteId = quoteId,
                stars = mutableListOf()
            )
        )

        ctx.respondPublic {
            content = "Quote #$quoteId successfully created!"
        }
    }

    private suspend fun getQuote(ctx: CommandContext) {
        val quoteId = ctx.args.integers["quoteid"]!!.toInt()

        val quote = getQuote(quoteId)
            ?: return ctx.respondEphemeral {
                content = "Quote #$quoteId not found"
            }

        ctx.respondPublic {
            parseQuote(quote)
        }
    }

    private suspend fun searchQuote(ctx: CommandContext) {
        val keyword = ctx.args.strings["keyword"]!!

        val quotes = searchQuotes(keyword)

        if (quotes.isEmpty()) {
            return ctx.respondEphemeral {
                content = "No quotes matched that filter"
            }
        }

        ctx.respondPublic {
            parseQuote(quotes[0])
        }
    }

    private suspend fun getRandomQuote(ctx: CommandContext) {
        val randomQuote = randomQuote
            ?: return ctx.respondEphemeral {
                content = "There are no quotes in this server! Try adding some"
            }

        ctx.respondPublic {
            parseQuote(randomQuote)
        }
    }

    private suspend fun removeQuote(ctx: CommandContext) {
        if (!ctx.author.isQuoter) {
            return ctx.respondEphemeral {
                content = "You're not allowed to execute this command"
            }
        }

        val quoteId = ctx.args.integers["quoteid"]!!.toInt()

        deleteQuote(quoteId)

        ctx.respondPublic {
            content = "Successfully deleted quote #$quoteId"
        }
    }

    private suspend fun starQuote(ctx: CommandContext) {
        val quoteId = ctx.args.integers["quoteid"]!!.toInt()
        val authorId = ctx.author.id.toString()

        val quote = getQuote(quoteId)

        if (quote == null) {
            ctx.respondEphemeral {
                content = "Quote #$quoteId does not exist"
            }
            return
        }

        if (quote.stars.contains(authorId)) {
            ctx.respondEphemeral {
                content = "Bruh you already starred this"
            }
            return
        }

        starQuote(
            quoteId = quoteId,
            authorId = authorId
        )

        ctx.respondPublic {
            content = "Successfully starred quote #$quoteId"
        }
    }

    private suspend fun unstarQuote(ctx: CommandContext) {
        val quoteId = ctx.args.integers["quoteid"]!!.toInt()
        val authorId = ctx.author.id.toString()

        val quote = getQuote(quoteId)

        if (quote == null) {
            ctx.respondEphemeral {
                content = "Quote #$quoteId does not exist"
            }
            return
        }

        if (!quote.stars.contains(authorId)) {
            ctx.respondEphemeral {
                content = "You don't have this quote starred!"
            }
            return
        }

        unstarQuote(
            quoteId = quoteId,
            authorId = authorId
        )

        ctx.respondPublic {
            content = "Successfully unstarred quote #$quoteId"
        }
    }

    private fun CustomInteractionResponseCreateBuilder.parseQuote(quote: Quote) {
        embed {
            val jumpTo = "\n\n[Jump to message](${quote.messageUrl})"

            title = quote.authorName
            description = quote.messageContent.takeMax(2048 - jumpTo.length) + jumpTo
            thumbnail {
                url = quote.authorAvatar
            }
            image = quote.attachment
            footer {
                text = "⭐${quote.stars.size} | ID: ${quote.quoteId} • ${quote.messageTimestamp}"
            }
        }
    }

}