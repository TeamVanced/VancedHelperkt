package commands.base

import com.beust.klaxon.JsonObject
import commandhandler.CommandContext
import type.CommandType
import ext.optional
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import utils.EmbedPagerAdapter
import utils.getJson

abstract class BaseArrowpageCommand(
    override val commandName: String,
    override val commandDescription: String,
    override val commandType: CommandType,
    override val commandAliases: List<String> = listOf(commandName)
) : BaseCommand(
    commandName = commandName,
    commandDescription = commandDescription,
    commandType = commandType,
    commandAliases = commandAliases,
    commandArguments = mapOf("Page number".optional()),
    addTrashCan = false
) {

    abstract val jsonName: String
    private val emotes = listOf("⏪", "⬅️", trashEmote, "➡️", "⏩")
    private var embedPagerAdapter = mutableMapOf<MessageChannel, EmbedPagerAdapter>()
    private val pagesWithTableOfContent = getPagesWithTableOfContent()

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        embedPagerAdapter[ctx.channel] = EmbedPagerAdapter(this, ctx.event, emotes, pagesWithTableOfContent)
        val args = ctx.args
        if (args.isNotEmpty()) {
            try {
                embedPagerAdapter[ctx.channel]?.newInstance(args[0].toInt())
            } catch (e: NumberFormatException) {
                ctx.event.channel.sendMsg("Provided argument is not a number!")
            } catch (e: IndexOutOfBoundsException) {
                ctx.event.channel.sendMsg("Provided page does not exist!")
            }
        } else {
            embedPagerAdapter[ctx.channel]?.newInstance()
        }

    }

    //this shit is kinda retarded but it works
    //will probably optimise this later
    private fun getPagesWithTableOfContent(): List<MessageEmbed> {
        val tableDesc = mutableListOf("0 | You are here ;)")
        val jsonArray = "https://vancedapp.com/webapi/strings/en/$jsonName.json".getJson()?.array<JsonObject>(jsonName)
        val embedPages = mutableListOf<EmbedBuilder>()
        val builtEmbedPages = mutableListOf<MessageEmbed>()
        for (i in jsonArray?.indices!!) {
            val element = jsonArray[i]
            embedPages.add(
                embedBuilder.apply {
                    setTitle(element.string("title"))
                    setDescription(element.string("description"))
                    val fields = element.array<JsonObject>("fields")
                    if (fields != null) {
                        for (j in fields.indices) {
                            with (fields[j]) {
                                addField(
                                    string("title"),
                                    string("content"),
                                    false
                                )
                            }
                        }
                    }
                }
            )
            tableDesc.add("${i + 1} | ${element.string("title")}")
        }
        for (i in embedPages.indices) {
            val embed = embedPages[i]
            embed.setFooter("Page ${i + 1}/${embedPages.size}")
            builtEmbedPages.add(embed.build())
        }
        val tablePage = embedBuilder.apply {
            setTitle("Index")
            setDescription(
                "Review the table of contents below and jump to the page you need via reactions\n\n```\n${
                    tableDesc.joinToString(
                        "\n"
                    )
                }\n```"
            )
            setFooter("page 0/${embedPages.size}")
        }.build()
        return listOf(tablePage) + builtEmbedPages
    }

    override fun onReactionAdd(event: MessageReactionAddEvent) {
        super.onReactionAdd(event)
        val channel = event.channel
        val message = channel to channel.botMessage?.id
        if (event.userId != channel.userMessage?.author?.id)
            return

        val channelAdapter = embedPagerAdapter[channel]
        when (event.reactionEmote.asReactionCode) {
            emotes[0] -> channelAdapter?.firstPage(message)
            emotes[1] -> channelAdapter?.previousPage(message)
            emotes[3] -> channelAdapter?.nextPage(message)
            emotes[4] -> channelAdapter?.lastPage(message)
        }
    }

}
