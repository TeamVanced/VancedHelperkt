package commands.moderation

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import ext.useArguments
import ext.useCommandProperly
import ext.warn
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Warn : BaseCommand(
    commandName = "warn",
    commandDescription = "Warn a user",
    commandType = Moderation,
    commandArguments = listOf("<User ID | User Mention>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val reason = if (args.size > 1) args.apply { remove(user) }.joinToString(" ") else "no reason provided"
            val id = user.filter { it.isDigit() }
            if (id.isEmpty()) {
                useCommandProperly()
                return
            }
            ctx.guild.retrieveMemberById(id).queue({ member ->
                if (!ctx.authorAsMember?.canInteract(member)!!) {
                    channel.sendMessage("You can't warn this member!").queueAddReaction()
                    return@queue
                }
                member.warn(guildId, reason, channel, embedBuilder)
                channel.sendMessage("Successfully warned ${member.user.asMention}").queue {
                    messageId = it.id
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                channel.sendMessage("Provided user does not exist!").queueAddReaction()
            })

        } else {
            useArguments(1)
        }
    }

}