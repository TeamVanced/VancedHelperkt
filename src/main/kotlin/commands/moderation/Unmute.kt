package commands.moderation

import commandhandler.CommandContext
import commands.base.BaseCommand
import type.CommandType.Moderation
import database.muteRole
import ext.required
import ext.sendUnmuteLog
import ext.useArguments
import ext.useCommandProperly
import net.dv8tion.jda.api.entities.Role
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse

class Unmute : BaseCommand(
    commandName = "unmute",
    commandDescription = "Unmute a member",
    commandType = Moderation,
    commandArguments = mapOf("User ID | User Mention".required())
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
                ctx.event.channel.sendMsg("Mute role does not exist, how the fuck can user be muted???")
            }
        } else {
            ctx.channel.useArguments(1)
        }
    }

    private fun removeRole(user: String, role: Role, ctx: CommandContext) {
        val id = user.filter { it.isDigit() }
        if (id.isEmpty()) {
            ctx.channel.useCommandProperly()
            return
        }
        if (contentIDRegex.matchEntire(id)?.value?.matches(contentIDRegex) == true) {
            ctx.guild.retrieveMemberById(id).queue({ member ->
                if (member.roles.contains(role)) {
                    ctx.guild.removeRoleFromMember(member, role).queue {
                        ctx.authorAsMember?.let { it1 -> sendUnmuteLog(member.user, it1.user, guildId) }
                        ctx.event.channel.sendMsg("Successfully unmuted ${member.asMention}")
                    }
                } else {
                    ctx.event.channel.sendMsg("Provided user is not muted!")
                }
            }, ErrorHandler().handle(ErrorResponse.UNKNOWN_USER) {
                ctx.event.channel.sendMsg("Provided user does not exist!")
            })
        } else {
            ctx.channel.useCommandProperly()
        }
    }

}