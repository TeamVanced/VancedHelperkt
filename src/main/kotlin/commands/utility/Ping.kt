package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Utility
import net.dv8tion.jda.api.utils.TimeUtil

class Ping : BaseCommand(
    commandName = "ping",
    commandDescription = "Get bot's ping",
    commandType = Utility
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        ctx.channel.sendMsg("Pinging...") { message ->
            message.editMessage("Pong! Took ${message.idLong.timeCreatedMillis() - ctx.event.message.idLong.timeCreatedMillis()}ms").queue {
                it.addReaction()
            }
        }
    }

    private fun Long.timeCreatedMillis(): Long = TimeUtil.getTimeCreated(this).toInstant().toEpochMilli()

}