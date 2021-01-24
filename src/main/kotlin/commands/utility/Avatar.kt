package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Utility
import ext.required
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Avatar : BaseCommand(
    commandName = "avatar",
    commandDescription = "Get a user avatar",
    commandType = Utility,
    commandArguments = mapOf("User Mention | User ID".required()),
    commandAliases = listOf("av", "pfp", "icon")
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
            sendMessage("Provided user does not exist!")
            return
        }
        ctx.guild.retrieveMemberById(userId).queue({
            sendMessage(
                embedBuilder.apply {
                    val url = it.user.avatarUrl + "?size=256"
                    setTitle("${it.user.name}'s avatar")
                    setImage(url)
                    setDescription("[Avatar URL]($url)")
                }.build()
            )
        }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
            sendMessage("Provided user does not exist!")
        })

    }

}