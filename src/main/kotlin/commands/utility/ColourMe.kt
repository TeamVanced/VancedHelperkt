package commands.utility

import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.boosterRoleId
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.Color
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.createRole
import dev.kord.core.behavior.edit
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.string

class ColourMe : BaseCommand(
    commandName = "colourme",
    commandDescription = "Create a custom role for yourself.",
    requiresPermissions = true
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val roleName = ctx.args["name"]!!.string()
        val roleColor = ctx.args["color"]!!.string()

        val author = ctx.author

        val kordColor = Color(java.awt.Color.decode(roleColor).rgb)

        val existingRole = author.roleBehaviors.find { it.asRole().name.contains("-CC") }
        if (existingRole != null) {
            existingRole.edit {
                name = "$roleName-CC"
                color = kordColor
            }
        } else {
            val newRole = author.guild.createRole {
                name = "$roleName-CC"
                color = kordColor
            }
            author.addRole(newRole.id, "colourme")
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                string(
                    name = "name",
                    description = "Name of the role"
                ) {
                    required = true
                }
                string(
                    name = "color",
                    description = "Hex color of the role"
                ) {
                    required = true
                }
            }
        )

    override suspend fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder(
            permissions = {
                role(
                    id = Snowflake(boosterRoleId),
                    allow = true
                )
            }
        )

}