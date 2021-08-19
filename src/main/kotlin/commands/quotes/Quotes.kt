package commands.quotes

import config

import core.command.CommandContext
import core.command.base.BaseCommand
import core.const.stonks
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.interaction.CustomInteractionResponseCreateBuilder
import database.*
import database.collections.Quote
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.int
import dev.kord.core.entity.interaction.string
import ext.takeMax
import java.text.SimpleDateFormat
import java.util.*

@OptIn(KordPreview::class)
class Quotes : BaseCommand(
    name = "quote",
    description = "Quote actions"
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
        val linkRegex = """https://(?:\w+.)?discord(?:app)?.com/channels/\d+/(?<channelId>\d+)/(?<messageId>\d+)""".toRegex()

        val link = ctx.args["link"]!!.string()

        if (!linkRegex.matches(link)) {
            ctx.respond {
                content = "Link was not provided"
            }
            return
        }

        val groups = linkRegex.find(link)!!.groups

        val messageId = groups["messageId"]!!.value
        val channelId = groups["channelId"]!!.value

        if (quoteExists(messageId)) {
            ctx.respond {
                content = "Pog, that quote is already added $stonks"
            }
            return
        }

        val message = ctx.channel.supplier.getMessageOrNull(Snowflake(channelId), Snowflake(messageId))

        if (message == null) {
            ctx.respond {
                content = "Message not found"
            }
            return
        }

        if (message.author?.isBot == true) {
            ctx.respond {
                content = "Beep Boop. Do not bully robots"
            }
            return
        }

        if (message.author == null) {
            ctx.respond {
                content = "That's not a message from a guild member!"
            }
            return
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
                authorId = message.author!!.id.asString,
                authorAvatar = message.author!!.avatar.url,
                authorName = message.author!!.tag,
                attachment = message.attachments.firstOrNull()?.url,
                quoteId = quoteId,
                stars = mutableListOf()
            )
        )

        ctx.respond {
            content = "Quote #$quoteId successfully created!"
        }
    }

    private fun getQuote(ctx: CommandContext) {
        val quoteId = ctx.args["quoteid"]!!.int()

        val quote = getQuote(quoteId)

        if (quote == null) {
            ctx.respond {
                content = "Quote #$quoteId not found"
            }
            return
        }

        ctx.respond {
            parseQuote(quote)
        }
    }

    private fun searchQuote(ctx: CommandContext) {
        val keyword = ctx.args["keyword"]!!.string()

        val quotes = searchQuotes(keyword)

        ctx.respond {
            if (quotes.isEmpty()) {
                content = "No quotes matched that filter"
                return@respond
            }

            parseQuote(quotes[0])
        }
    }

    private fun getRandomQuote(ctx: CommandContext) {
        ctx.respond {
            val randomQuote = randomQuote
            if (randomQuote != null) {
                parseQuote(randomQuote)
                return@respond
            }

            content = "There are no quotes in this server! Try adding some"
        }
    }

    private fun removeQuote(ctx: CommandContext) {
        val quoteId = ctx.args["quoteid"]!!.int()

        deleteQuote(quoteId)

        ctx.respond {
            content = "Successfully deleted quote #$quoteId"
        }
    }

    private fun starQuote(ctx: CommandContext) {
        val quoteId = ctx.args["quoteid"]!!.int()
        val authorId = ctx.author.id.asString

        val quote = getQuote(quoteId)

        if (quote == null) {
            ctx.respond {
                content = "Quote #$quoteId does not exist"
            }
            return
        }

        if (quote.stars.contains(authorId)) {
            ctx.respond {
                content = "Bruh you already starred this"
            }
            return
        }

        starQuote(
            quoteId = quoteId,
            authorId = authorId
        )

        ctx.respond {
            content = "Successfully starred quote #$quoteId"
        }
    }

    private fun unstarQuote(ctx: CommandContext) {
        val quoteId = ctx.args["quoteid"]!!.int()
        val authorId = ctx.author.id.asString

        val quote = getQuote(quoteId)

        if (quote == null) {
            ctx.respond {
                content = "Quote #$quoteId does not exist"
            }
            return
        }

        if (!quote.stars.contains(authorId)) {
            ctx.respond {
                content = "You don't have this quote starred!"
            }
            return
        }

        unstarQuote(
            quoteId = quoteId,
            authorId = authorId
        )

        ctx.respond {
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