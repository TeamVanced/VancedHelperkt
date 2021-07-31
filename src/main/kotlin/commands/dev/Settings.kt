package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import database.muteRoleId
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.role

@OptIn(KordPreview::class)
class Settings : BaseCommand(
    name = "settings",
    description = "Configure settings"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val subCommand = ctx.subCommand ?: return

        when (subCommand.name) {
            "muterole" -> configureMuteRole(ctx)
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                subCommand(
                    name = "muterole",
                    description = "Set the mute role",
                    builder = {
                        role(
                            name = "role",
                            description = "Role to assign",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
            }
        )

    private fun configureMuteRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        muteRoleId = role.id.value
        ctx.respond {
            content = "Successfully updated mute role"
        }
    }

}