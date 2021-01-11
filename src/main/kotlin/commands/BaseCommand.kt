package commands

import commandhandler.CommandContext
import commandhandler.ICommand
import commandhandler.IMessageReactionListener
import database.prefix
import ext.sendMsg
import ext.transformToArg
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import java.awt.Color
import javax.annotation.OverridingMethodsMustInvokeSuper

open class BaseCommand(
    override val commandType: CommandType,
    override val commandName: String,
    override val commandDescription: String,
    override val commandArguments: Map<String, ArgumentType> = mapOf(),
    override val commandAliases: List<String> = listOf(commandName),
    override val devOnly: Boolean = false,
    private val addTrashCan: Boolean = true,
) : ICommand, IMessageReactionListener {

    val contentIDRegex = "\\b\\d{18}\\b".toRegex()
    val emoteRegex = "<?(a)?:?(\\w{2,32}):(\\d{17,19})>?".toRegex()

    val trashEmote = "\uD83D\uDDD1"
    lateinit var channel: TextChannel

    open val embedBuilder get() = EmbedBuilder().setColor(Color((Math.random() * 0x1000000).toInt()))

    var messageId = ""
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
                    channel.deleteMessagesByIds(listOf(messageId, userMessageId)).queue(null, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE){})
                }
            }
        }
    }

    override fun onReactionRemove(event: MessageReactionRemoveEvent) {}

    fun sendMessage(message: String) {
        channel.sendMsg(message) {
            it.addReaction()
        }
    }

    fun sendMessage(embed: MessageEmbed) {
        channel.sendMsg(embed) {
            it.addReaction()
        }
    }

    fun Message.addReaction() {
        if (messageId != "") channel.removeReactionById(messageId, trashEmote).queue(null, ErrorHandler().handle(ErrorResponse.UNKNOWN_MESSAGE) {})
        messageId = id
        addReaction(trashEmote).queue()
    }

    fun getHelpEmbed(): MessageEmbed = EmbedBuilder().apply {
        setTitle(commandName)
        setDescription("Owner Only: $devOnly\nMinimum required arguments: ${commandArguments.filter { it.value != ArgumentType.Optional }.size}")
        addField("Description", commandDescription, false)
        addField("Usage", "```css\n${guildId.prefix}$commandName ${commandArguments.map { it.transformToArg() }.joinToString(" ")}```", false)
        addField("Aliases", commandAliases.joinToString(" "), false)
    }.build()

}