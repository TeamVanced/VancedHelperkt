package commands.moderation

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.moderatorRoleIds
import core.database.muteRoleId
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.edit
import dev.kord.core.entity.interaction.user
import dev.kord.rest.builder.interaction.subCommand
import dev.kord.rest.builder.interaction.user

class Mutes : BaseCommand(
    commandName = "mute",
    commandDescription = "Mute actions",
    defaultPermissions = false
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val subCommand = ctx.subCommand ?: return

        when (subCommand.name) {
            "add" -> muteUser(ctx)
            "remove" -> unmuteUser(ctx)
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder(
            arguments = {
                subCommand(
                    name = "add",
                    description = "Mute a member",
                    builder = {
                        user(
                            name = "user",
                            description = "Who to mute",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
                subCommand(
                    name = "remove",
                    description = "Unmute a member",
                    builder = {
                        user(
                            name = "user",
                            description = "User to unmute",
                            builder = {
                                required = true
                            }
                        )
                    }
                )
            }
        )

    override fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder(
            permissions = {
                for (moderatorRoleId in moderatorRoleIds) {
                    role(
                        id = Snowflake(moderatorRoleId),
                        allow = true
                    )
                }
            }
        )

    private suspend fun muteUser(ctx: CommandContext) {
        val user = ctx.args["user"]!!.user()

        val userMention = user.mention

        user.asMember(config.guildSnowflake).edit {
            if (roles == null) {
                ctx.respondEphemeral {
                    content = "Failed to retrieve roles for $userMention"
                }
                return@edit
            }

            val muteSnowflake = Snowflake(muteRoleId)

            if (roles!!.contains(muteSnowflake)) {
                ctx.respondEphemeral {
                    content = "$userMention is already muted"
                }
                return@edit
            }

            reason = "Muted"
            roles!!.add(muteSnowflake)
            ctx.respondPublic {
                content = "Successfully muted $userMention"
            }
        }
    }

    private suspend fun unmuteUser(ctx: CommandContext) {
        val user = ctx.args["user"]!!.user()

        val userMention = user.mention

        user.asMember(config.guildSnowflake).edit {
            if (roles == null) {
                ctx.respondEphemeral {
                    content = "Failed to retrieve roles for $userMention"
                }
                return@edit
            }

            val muteSnowflake = Snowflake(muteRoleId)

            if (!roles!!.contains(muteSnowflake)) {
                ctx.respondEphemeral {
                    content = "$userMention is not muted"
                }
                return@edit
            }

            reason = "Unmuted"
            roles!!.remove(muteSnowflake)
            ctx.respondPublic {
                content = "Successfully unmuted $userMention"
            }
        }
    }

}