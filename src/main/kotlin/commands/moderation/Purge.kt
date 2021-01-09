package commands.moderation

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Moderation
import ext.required
import ext.useArguments
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

class Purge : BaseCommand(
    commandName = "purge",
    commandDescription = "Purge a number of messages between 2 and 100 in the channel",
    commandType = Moderation,
    commandAliases = listOf("prune"),
    commandArguments = mapOf("amount".required())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val amount = args[0].toIntOrNull()
            if (amount == null || amount > 100 || amount < 2) {
                channel.sendMessage("Provide a valid amount!").queueAddReaction()
                return
            }
            channel.history.retrievePast(amount).queue { messagesList ->
                val messages = messagesList.filter { !it.timeCreated.isBefore(OffsetDateTime.now().minus(2, ChronoUnit.WEEKS)) }.take(100)
                if (messages.isEmpty()) {
                    channel.sendMessage("Messages not found!").queueAddReaction()
                    return@queue
                }
                channel.deleteMessages(messages).queue {
                    channel.sendMessage("Succesfully deleted $amount messages").queueAddReaction()
                }
            }
        } else {
            useArguments(1)
        }

    }

}