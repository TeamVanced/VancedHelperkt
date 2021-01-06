package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility
import database.colourmeRoles
import ext.useArguments
import net.dv8tion.jda.api.exceptions.ErrorHandler
import net.dv8tion.jda.api.requests.ErrorResponse
import java.awt.Color

class Colourme : BaseCommand(
    commandName = "colourme",
    commandDescription = "Create a custom role for yourself",
    commandType = Utility,
    commandArguments = listOf("<color> <role name>"),
    commandAliases = listOf("colorme", "colorme")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val guildId = ctx.guild.id
        val args = ctx.args
        if (args.isNotEmpty() && args.size >= 2) {
            val color = try {
                Color.decode("#${args[0].removePrefix("#")}")
            } catch (e: Exception) {
                Color.CYAN
            }

            ctx.guild.retrieveMemberById(ctx.author.id).queue member@ { member ->
                if (member.roles.none { guildId.colourmeRoles.contains(it.id) }) {
                    channel.sendMessage("You are not allowed to use this command!").queueAddReaction()
                    return@member
                }
                val roleName = args.apply { removeAt(0) }.joinToString(" ")
                val ccrole = member.roles.filter { it.name.endsWith("CC") }
                fun addRole() {
                    ctx.guild.createRole().setColor(color).setName("$roleName-CC").queue({ role ->
                        ctx.guild.modifyRolePositions().selectPosition(role).moveTo(member.roles.first().position + 1).queue {
                            ctx.guild.addRoleToMember(member, role).queue {
                                channel.sendMessage("Successfully added the role!").queueAddReaction()
                            }
                        }
                    }, ErrorHandler().handle(ErrorResponse.MAX_ROLES_PER_GUILD) {
                        channel.sendMessage("Guild reached maximum amount of roles!").queueAddReaction()
                    })
                }

                if (ccrole.isNotEmpty()) {
                    ccrole[0].delete().queue {
                        addRole()
                    }
                } else {
                    addRole()
                }

            }
        } else {
            useArguments(2)
        }
    }

}