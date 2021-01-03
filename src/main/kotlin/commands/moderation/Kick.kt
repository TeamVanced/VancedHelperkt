package commands.moderation

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import ext.sendKickLog
import ext.useArguments
import ext.useCommandProperly
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.exceptions.HierarchyException
import net.dv8tion.jda.api.requests.ErrorResponse

class Kick : BaseCommand(
    commandName = "kick",
    commandDescription = "Kick a user",
    commandType = Moderation,
    commandArguments = listOf("<User ID | User mention>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val reason = if (args.size > 1) args.apply { remove(user) }.joinToString(" ") else "reason not provided"
            val id = user.filter { it.isDigit() }
            if (contentIDRegex.matchEntire(id)?.value?.matches(contentIDRegex) == true) {
                ctx.guild.retrieveMemberById(id).queue({ member ->
                    try {
                        member.kick(reason).queue {
                            ctx.authorAsMember?.let { it1 -> embedBuilder.sendKickLog(member, it1, reason, guildId) }
                            channel.sendMessage("Successfully kicked ${member.user.asTag}").queueAddReaction()
                        }
                    } catch (e: HierarchyException) {
                        channel.sendMessage("You cannot kick this member because their role is higher than yours!").queueAddReaction()
                    }
                }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                    channel.sendMessage("Provided user does not exist!").queueAddReaction()
                })
            } else {
                channel.useCommandProperly(this)
            }

        } else {
            channel.useArguments(1, this)
        }
    }
}