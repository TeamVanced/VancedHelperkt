package commands

import commandhandler.CommandContext
import commandhandler.ICommand
import commandhandler.IMessageReactionListener
import database.prefix
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import java.awt.Color
import javax.annotation.OverridingMethodsMustInvokeSuper
import kotlin.properties.Delegates

open class BaseCommand(
    override val commandType: CommandTypes,
    override val commandName: String,
    override val commandDescription: String,
    override val commandArguments: List<String> = emptyList(),
    override val commandAliases: List<String> = listOf(commandName),
    override val devOnly: Boolean = false,
    private val addTrashCan: Boolean = true,
) : ICommand, IMessageReactionListener {

    val contentIDRegex = "\\b\\d{18}\\b".toRegex()

    val trashEmote = "\uD83D\uDDD1"
    lateinit var channel: TextChannel

    open val embedBuilder get() = EmbedBuilder().setColor(Color((Math.random() * 0x1000000).toInt()))

    var messageId by Delegates.observable("") { _, oldValue, newValue ->
        if (addTrashCan) {
            try {
                channel.addReactionById(newValue, trashEmote).queue()
            } catch (e: Exception) {
            }
            try {
                channel.removeReactionById(oldValue, trashEmote).queue()
            } catch (e: Exception) {
            }
        }
    }

    var commandAuthorId = ""
    var guildId = ""
    private var userMessageId: String = ""

    @OverridingMethodsMustInvokeSuper
    override fun execute(ctx: CommandContext) {
        channel = ctx.channel
        userMessageId = ctx.event.messageId
        commandAuthorId = ctx.author.id
        guildId = ctx.guild.id
    }

    @OverridingMethodsMustInvokeSuper
    override fun onReactionAdd(event: MessageReactionAddEvent) {
        if (event.userId != commandAuthorId)
            return

        event.user?.let {
            event.reaction.removeReaction(it).queue {
                if (event.reactionEmote.asReactionCode == trashEmote) {
                    channel.deleteMessageById(messageId).queue()
                    channel.deleteMessageById(userMessageId).queue()
                }
            }
        }
    }

    override fun onReactionRemove(event: MessageReactionRemoveEvent) {}

    fun getHelpEmbed(): MessageEmbed = EmbedBuilder().apply {
        setTitle(commandName)
        setDescription("Owner Only: $devOnly\nRequires arguments: ${commandArguments.isNotEmpty()}")
        addField("Description", commandDescription, false)
        addField("Usage", "```css\n${guildId.prefix}$commandName ${commandArguments.joinToString()}```", false)
        addField("Aliases", commandAliases.joinToString(" "), false)
    }.build()

}