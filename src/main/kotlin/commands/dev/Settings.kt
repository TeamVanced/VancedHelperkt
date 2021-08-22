package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import database.*
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.channel
import dev.kord.core.entity.interaction.role

@OptIn(KordPreview::class)
class Settings : BaseCommand(
    commandName = "settings",
    commandDescription = "Configure settings"
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val subCommand = ctx.subCommand
        val subCommandGroup = ctx.subCommandGroup

        when {
            subCommand != null -> {
                when (subCommand.name) {
                    "logchannel" -> configureLogChannel(ctx)
                    "modlogchannel" -> configureModLogChannel(ctx)
                    "errorchannel" -> configureErrorChannel(ctx)
                    "muterole" -> configureMuteRole(ctx)
                    "boosterrole" -> configureBoosterRole(ctx)
                }
            }
            subCommandGroup != null -> {
                when (subCommandGroup.groupName) {
                    "moderators" -> {
                        when (subCommandGroup.name) {
                            "add" -> addModerator(ctx)
                            "remove" -> removeModerator(ctx)
                        }
                    }
                    "quoters" -> {
                        when (subCommandGroup.name) {
                            "add" -> addQuoteRole(ctx)
                            "remove" -> removeQuoteRole(ctx)
                        }
                    }
                    "colourmes" -> {
                        when (subCommandGroup.name) {
                            "add" -> addColourMeRole(ctx)
                            "remove" -> removeColourMeRole(ctx)
                        }
                    }
                }
            }
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
                subCommand(
                    name = "logchannel",
                    description = "Set the log channel",
                    builder = {
                        channel(
                            name = "channel",
                            description = "Channel to use as the log channel",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "modlogchannel",
                    description = "Set the moderator action log channel",
                    builder = {
                        channel(
                            name = "channel",
                            description = "Channel to use as the moderator action log channel",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "errorchannel",
                    description = "Set the error log channel",
                    builder = {
                        channel(
                            name = "channel",
                            description = "Channel to use as the error log channel",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                group(
                    name = "moderators",
                    description = "Edit moderators",
                    builder = {
                        subCommand(
                            name = "add",
                            description = "Add a moderator",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to add to moderators",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                        subCommand(
                            name = "remove",
                            description = "Remove a moderator",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to remove from moderators",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                    }
                )
                group(
                    name = "quoters",
                    description = "Edit roles that can add quotes",
                    builder = {
                        subCommand(
                            name = "add",
                            description = "Add allowed quote role",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to add to allowed quote roles",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                        subCommand(
                            name = "remove",
                            description = "Removed allowed quote role",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to remove from allowed quote roles",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                    }
                )
                group(
                    name = "colourmes",
                    description = "Edit roles that can use colourme",
                    builder = {
                        subCommand(
                            name = "add",
                            description = "Add allowed colourme role",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to add to allowed colourme roles",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                        subCommand(
                            name = "remove",
                            description = "Removed allowed colourme role",
                            builder = {
                                role(
                                    name = "role",
                                    description = "Role to remove from allowed colourme roles",
                                    builder = {
                                        required = true
                                    }
                                )
                            }
                        )
                    }
                )
            }
        )

    private fun configureLogChannel(ctx: CommandContext) {
        val channel = ctx.args["channel"]!!.channel()

        logChannelId = channel.id.value
        ctx.respondPublic {
            content = "Successfully updated the log channel"
        }
    }

    private fun configureModLogChannel(ctx: CommandContext) {
        val channel = ctx.args["channel"]!!.channel()

        modLogChannelId = channel.id.value
        ctx.respondPublic {
            content = "Successfully updated the moderator action log channel"
        }
    }

    private fun configureErrorChannel(ctx: CommandContext) {
        val channel = ctx.args["channel"]!!.channel()

        errorChannelId = channel.id.value
        ctx.respondPublic {
            content = "Successfully updated the error log channel"
        }
    }

    private fun configureMuteRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        muteRoleId = role.id.value
        ctx.respondPublic {
            content = "Successfully updated the mute role"
        }
    }

    private fun configureBoosterRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        boosterRoleId = role.id.value
        ctx.respondPublic {
            content = "Successfully updated the booster role"
        }
    }

    private fun addModerator(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        addModeratorRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully added ${role.mention} to moderators"
        }
    }

    private fun removeModerator(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        removeModeratorRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully removed ${role.mention} from moderators"
        }
    }

    private fun addQuoteRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        addAllowedQuoteRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully added ${role.mention} to allowed quote roles"
        }
    }

    private fun removeQuoteRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        removeAllowedQuoteRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully removed ${role.mention} from allowed quote roles"
        }
    }

    private fun addColourMeRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        addAllowedColourMeRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully added ${role.mention} to allowed colourme roles"
        }
    }

    private fun removeColourMeRole(ctx: CommandContext) {
        val role = ctx.args["role"]!!.role()

        removeAllowedColourMeRoleId(role.id.value)
        ctx.respondPublic {
            content = "Successfully removed ${role.mention} from allowed colourme roles"
        }
    }

}