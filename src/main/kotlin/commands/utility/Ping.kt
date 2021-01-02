package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility

class Ping : BaseCommand(
    commandName = "ping",
    commandDescription = "Get bot's ping",
    commandType = Utility
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        channel.sendMessage("Pinging...").queue { message ->
            message.editMessage("Pong! Took ${message.timeCreated.toInstant().toEpochMilli() - ctx.event.message.timeCreated.toInstant().toEpochMilli()}ms").queue {
                messageId = it.id
            }
        }
    }

}