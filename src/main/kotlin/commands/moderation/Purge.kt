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
    commandAliases = listOf("prune", "clear"),
    commandArguments = mapOf("amount".required())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val amount = args[0].toIntOrNull()
            if (amount == null || amount > 100 || amount < 2) {
                sendMessage("Provide a valid amount!")
                return
            }
            channel.history.retrievePast(amount).queue { messagesList ->
                val messages = messagesList.filter { !it.timeCreated.isBefore(OffsetDateTime.now().minus(2, ChronoUnit.WEEKS)) }.take(100)
                if (messages.isEmpty()) {
                    sendMessage("Messages not found!")
                    return@queue
                }
                channel.deleteMessages(messages).queue {
                    sendMessage("Succesfully deleted $amount messages")
                }
            }
        } else {
            useArguments(1)
        }

    }

}