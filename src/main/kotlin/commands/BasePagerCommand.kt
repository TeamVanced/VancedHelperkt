package commands

import com.beust.klaxon.JsonObject
import commandhandler.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import utils.EmbedPagerAdapter
import utils.getJson

abstract class BasePagerCommand(
    override val commandName: String,
    override val commandDescription: String,
    override val commandType: CommandTypes,
    override val commandAliases: List<String> = listOf(commandName),
    override val commandArguments: List<String> = emptyList()
) : BaseCommand(
    commandName = commandName,
    commandDescription = commandDescription,
    commandType = commandType,
    commandAliases = commandAliases,
    commandArguments = commandArguments,
    addTrashCan = false
) {

    abstract val jsonName: String
    private val emotes = listOf("⏪", "⬅️", trashEmote, "➡️", "⏩")
    private lateinit var embedPagerAdapter: EmbedPagerAdapter
    private val pagesWithTableOfContent = getPagesWithTableOfContent()

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        embedPagerAdapter = EmbedPagerAdapter(this, ctx.event, emotes, pagesWithTableOfContent)
        val args = ctx.args
        if (args.isNotEmpty()) {
            try {
                embedPagerAdapter.newInstance(args[0].toInt())
            } catch (e: NumberFormatException) {
                ctx.channel.sendMessage("Provided argument is not a number!").queueAddReaction()
            } catch (e: IndexOutOfBoundsException) {
                ctx.channel.sendMessage("Provided page does not exist!").queueAddReaction()
            }
        } else {
            embedPagerAdapter.newInstance()
        }

    }

    //this shit is kinda retarded but it works
    //will probably optimise this later
    private fun getPagesWithTableOfContent(): List<MessageEmbed> {
        val tableDesc = mutableListOf("0 | You are here ;)")
        val jsonArray = "file:///C:/Users/Xinto/IdeaProjects/VancedBackend/strings/en/$jsonName.json".getJson()?.array<JsonObject>(jsonName)
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
        if (event.userId != commandAuthorId)
            return

        when (event.reactionEmote.asReactionCode) {
            emotes[0] -> embedPagerAdapter.firstPage()
            emotes[1] -> embedPagerAdapter.previousPage()
            emotes[3] -> embedPagerAdapter.nextPage()
            emotes[4] -> embedPagerAdapter.lastPage()
        }
    }

    override fun onReactionRemove(event: MessageReactionRemoveEvent) {}

}