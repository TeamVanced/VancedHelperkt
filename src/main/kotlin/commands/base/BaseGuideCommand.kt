package commands.base

import commandhandler.CommandContext
import ext.optional
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageChannel
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import org.koin.core.component.inject
import repository.guide.GuideRepositoryImpl
import type.CommandType
import utils.EmbedPagerAdapter

abstract class BaseGuideCommand(
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
    private val repository by inject<GuideRepositoryImpl>()

    abstract val jsonName: String
    private val emotes = listOf("⏪", "⬅️", trashEmote, "➡️", "⏩")
    private var embedPagerAdapter = mutableMapOf<MessageChannel, EmbedPagerAdapter>()
    private lateinit var pagesWithTableOfContent: List<MessageEmbed>

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        embedPagerAdapter[ctx.channel] = EmbedPagerAdapter(this, ctx.event, emotes, pagesWithTableOfContent)
        val args = ctx.args
        if (args.isNotEmpty()) {
            try {
                embedPagerAdapter[ctx.channel]?.newInstance(args[0].toInt())
            } catch (e: NumberFormatException) {
                ctx.message.replyMsg("Provided argument is not a number!")
            } catch (e: IndexOutOfBoundsException) {
                ctx.message.replyMsg("Provided page does not exist!")
            }
        } else {
            embedPagerAdapter[ctx.channel]?.newInstance()
        }

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

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val tableDesc = mutableListOf("0 | You are here ;)")
            val jsonArray = repository.fetch("$jsonName.json").data
            val embedPages = mutableListOf<EmbedBuilder>()
            val builtEmbedPages = mutableListOf<MessageEmbed>()
            jsonArray.forEachIndexed { index, element ->
                embedPages.add(
                    embedBuilder.apply {
                        setTitle(element.title)
                        setDescription(element.description)
                        val fields = element.fields
                        fields?.forEach { field ->
                            with (field) {
                                addField(
                                    title,
                                    content,
                                    false
                                )
                            }

                        }
                    }
                )
                tableDesc.add("${index + 1} | ${element.title}")
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
            pagesWithTableOfContent = listOf(tablePage) + builtEmbedPages
        }
    }

}
