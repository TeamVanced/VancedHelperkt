package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Avatar : BaseCommand(
    commandName = "avatar",
    commandDescription = "Get a user avatar",
    commandType = Utility,
    commandArguments = listOf("[User Mention | User ID]"),
    commandAliases = listOf("av")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            sendAvatar(args[0].filter { it.isDigit() }, ctx)
        } else {
            sendAvatar(ctx.author.id, ctx)
        }

    }

    private fun sendAvatar(userId: String, ctx: CommandContext) {
        if (userId.isEmpty()) {
            channel.sendMessage("Provided user does not exist!").queue {
                messageId = it.id
            }
            return
        }
        ctx.guild.retrieveMemberById(userId).queue({
            channel.sendMessage(
                embedBuilder.apply {
                    val url = it.user.avatarUrl + "?size=256"
                    setTitle("${it.user.name}'s avatar")
                    setImage(url)
                    setDescription("[Avatar URL]($url)")
                }.build()
            ).queue {
                messageId = it.id
            }
        }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
            channel.sendMessage("Provided user does not exist!").queue {
                messageId = it.id
            }
        })

    }

}