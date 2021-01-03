package commands.moderation

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import ext.sendUnbanLog
import ext.useArguments
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Unban : BaseCommand(
    commandName = "unban",
    commandDescription = "Unban a user",
    commandType = Moderation,
    commandArguments = listOf("<User ID>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val id = user.filter { it.isDigit() }
            ctx.guild.retrieveBanById(id).queue({ ban ->
                ctx.event.guild.unban(ban.user).queue {
                    ctx.authorAsMember?.let { it1 ->
                        ctx.guild.getMemberById(ban.user.id)?.let { it2 ->
                            embedBuilder.sendUnbanLog(it2, it1, guildId)
                        }
                    }
                    channel.sendMessage("Successfully unbanned ${ban.user.asTag}").queueAddReaction()
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_BAN) {
                channel.sendMessage("Either user does not exist or they're not banned!").queueAddReaction()
            })

        } else {
            channel.useArguments(1, this)
        }
    }

}