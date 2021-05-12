package commands.base

import commandhandler.CommandContext
import ext.useArguments
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import type.ArgumentType
import type.CommandType

abstract class BaseMultipageCommand <T> (
    override val commandName: String,
    override val commandDescription: String,
    override val commandType: CommandType,
    override val commandAliases: List<String> = listOf(commandName),
    override val commandArguments: Map<String, ArgumentType> = mapOf()
) : BaseCommand(
    commandName = commandName,
    commandDescription = commandDescription,
    commandType = commandType,
    commandAliases = commandAliases,
    commandArguments = commandArguments,
    addTrashCan = false
) {

    var itemsList = listOf<T>()
    val emotes = arrayOf("🔢", "1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "🔟")

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        itemsList = getItems(ctx.args)

        val requiredArgs = commandArguments.filter { it.value == ArgumentType.Required }.size
        if (ctx.args.size < requiredArgs) {
            ctx.message.useArguments(requiredArgs)
            return
        }

        if (itemsList.isEmpty()) {
            handleEmptylist(ctx)
            return
        }
        sendPager(ctx.message)
    }

    override fun onReactionAdd(event: MessageReactionAddEvent) {
        super.onReactionAdd(event)
        val botMessageId = event.channel.botMessage?.id

        if (event.userId != event.channel.userMessage?.author?.id)
            return

        if (event.reactionEmote.asReactionCode == emotes[0] && botMessageId != null) {
            event.channel.editMessageById(botMessageId, getMainPage()).queue()
            return
        }
        if (emotes.contains(event.reactionEmote.asReactionCode) && botMessageId != null)
            event.channel.editMessageById(
                botMessageId,
                getPage(itemsList[emotes.indexOf(event.reactionEmote.asReactionCode) - 1])
            ).queue()
    }

    abstract fun getPage(item: T): MessageEmbed
    abstract fun getMainPage(): MessageEmbed

    abstract fun getItems(args: MutableList<String>): List<T>

    abstract fun handleEmptylist(ctx: CommandContext)

    private fun sendPager(message: Message) {
        message.replyMsg(
            getMainPage()
        ) { msg ->
            commandMessage[message.channel] = msg
            (0..itemsList.size).forEach {
                msg.addReaction(emotes[it]).queue()
            }
            msg.addReaction(trashEmote).queue()
        }

    }

}