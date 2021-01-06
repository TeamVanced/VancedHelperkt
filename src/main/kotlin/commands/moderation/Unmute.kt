package commands.moderation

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Moderation
import database.muteRole
import ext.sendUnmuteLog
import ext.useArguments
import ext.useCommandProperly
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Unmute : BaseCommand(
    commandName = "unmute",
    commandDescription = "Unmute a member",
    commandType = Moderation
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            val user = args[0]
            val role = ctx.guild.getRoleById(guildId.muteRole)
            if (role != null) {
                removeRole(user, role, ctx)
            } else {
                channel.sendMessage("Mute role does not exist, how the fuck can user be muted???").queueAddReaction()
            }
        } else {
            useArguments(1)
        }
    }

    private fun removeRole(user: String, role: Role, ctx: CommandContext) {
        val id = user.filter { it.isDigit() }
        if (id.isEmpty()) {
            useCommandProperly()
            return
        }
        if (contentIDRegex.matchEntire(id)?.value?.matches(contentIDRegex) == true) {
            ctx.guild.retrieveMemberById(id).queue({ member ->
                if (member.roles.contains(role)) {
                    ctx.guild.removeRoleFromMember(member, role).queue {
                        ctx.authorAsMember?.let { it1 -> embedBuilder.sendUnmuteLog(member, it1, guildId) }
                        channel.sendMessage("Successfully unmuted ${member.asMention}").queueAddReaction()
                    }
                } else {
                    channel.sendMessage("Provided user is not muted!").queueAddReaction()
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                channel.sendMessage("Provided user does not exist!").queueAddReaction()
            })
        } else {
            useCommandProperly()
        }
    }

}