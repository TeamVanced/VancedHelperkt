package commands.utility

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Utility
import ext.isBooster
import ext.isMod
import ext.useArguments
import java.awt.Color

class Colourme : BaseCommand(
    commandName = "colourme",
    commandDescription = "Create a custom role for yourself",
    commandType = Utility,
    commandArguments = listOf("<color> <role name>"),
    commandAliases = listOf("colorme")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val member = ctx.authorAsMember
        val guildId = ctx.guild.id
        if (!(member!!.isBooster(guildId) || member.isMod(guildId))) {
            channel.sendMessage("You are not allowed to use this command!").queue {
                messageId = it.id
            }
            return
        }
        val args = ctx.args
        if (args.isNotEmpty() && args.size >= 2) {
            val color = try {
                Color.decode(args[0])
            } catch (e: Exception) {
                Color.BLACK
            }

            val roleName = args.apply { removeAt(0) }.joinToString(" ")
            ctx.guild.createRole().setColor(color).setName("$roleName-CC").queue { role ->
                ctx.guild.addRoleToMember(member, role).queue {
                    channel.sendMessage("Successfully added the role!").queue {
                        messageId = it.id
                    }
                }
            }
        } else {
            channel.useArguments(2, this)
        }
    }

}