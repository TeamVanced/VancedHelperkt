package commands

import commandhandler.CommandContext
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import utils.EmbedPagerAdapter

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

    abstract val embedPages: List<MessageEmbed>
    abstract val totalEmbedPages: Int
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

    fun EmbedBuilder.build(position: Int): MessageEmbed {
        setFooter("page $position/$totalEmbedPages")
        return build()
    }

    private fun getPagesWithTableOfContent(): List<MessageEmbed> {
        val tableDesc = mutableListOf("0 | You are here ;)")
        embedPages.forEach { it ->
            tableDesc.add("${it.footer?.text?.first { it.isDigit() }} | ${it.title}")
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
        }.build(0)
        return listOf(tablePage) + embedPages
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