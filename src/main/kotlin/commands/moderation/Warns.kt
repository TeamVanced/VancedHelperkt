package commands.moderation

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.addUserWarn
import core.database.getUserWarns
import core.database.moderatorRoleIds
import core.database.removeUserWarn
import core.ext.canInteractWith
import core.ext.checkWarnForTooManyInfractions
import core.ext.takeMax
import core.util.Infraction
import core.util.sendInfractionToModLogChannel
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string
import dev.kord.rest.builder.interaction.subCommand
import dev.kord.rest.builder.interaction.user

class Warns : BaseCommand(
    commandName = "warn",
    commandDescription = "Warn actions",
    defaultPermissions = false
) {

    override suspend fun execute(
        ctx: CommandContext
    ) {
        val subCommand = ctx.subCommand ?: return

        when (subCommand.name) {
            "add" -> warnUser(ctx)
            "remove" -> unwarnUser(ctx)
            "list" -> listWarns(ctx)
        }
    }

    override suspend fun commandOptions() = CustomApplicationCommandCreateBuilder(
        arguments = {
            subCommand(
                name = "add",
                description = "Warn a user",
                builder = {
                    user(
                        name = "user",
                        description = "Who to warn",
                        builder = {
                            required = true
                        }
                    )
                    string(
                        name = "reason",
                        description = "Reason of warn",
                        builder = {
                            required = true
                        }
                    )
                }
            )
            subCommand(
                name = "remove",
                description = "Unwarn a user",
                builder = {
                    user(
                        name = "user",
                        description = "Who to unwarn",
                        builder = {
                            required = true
                        }
                    )
                    int(
                        name = "warnid",
                        description = "Number of the warn",
                        builder = {
                            required = false
                        }
                    )
                }
            )
            subCommand(
                name = "list",
                description = "List user's warnings",
                builder = {
                    user(
                        name = "user",
                        description = "Whose warnings to get",
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

    private suspend fun warnUser(ctx: CommandContext) {
        val user = ctx.args.users["user"]!!
        val reason = ctx.args.strings["reason"]!!

        val member = user.asMember(config.guildSnowflake)
        val memberMention = member.mention

        if (!ctx.author.canInteractWith(member)) {
            ctx.respondEphemeral {
                content = "You don't have enough permissions to interact with $memberMention."
            }
            return
        }

        addUserWarn(
            userId = user.id.toString(),
            userTag = user.tag,
            reason = reason
        )

        ctx.respondPublic {
            content = "Successfully warned $memberMention for $reason"
        }
        sendInfractionToModLogChannel(
            Infraction.Warn(user, ctx.author, reason)
        )
        user.asMember(config.guildSnowflake).checkWarnForTooManyInfractions()
    }

    private suspend fun unwarnUser(ctx: CommandContext) {
        val user = ctx.args.users["user"]!!
        val warnId = ctx.args.integers["warnid"]?.toInt()

        val member = user.asMember(config.guildSnowflake)
        val memberMention = member.mention

        if (!ctx.author.canInteractWith(member)) {
            ctx.respondEphemeral {
                content = "You don't have enough permissions to interact with $memberMention."
            }
            return
        }

        val userId = user.id.toString()

        if (warnId != null) {
            val warns = getUserWarns(userId)
            if (warnId == 0 || (warns != null && warnId > warns.reasons.size)) {
                ctx.respondEphemeral {
                    content = "$warnId is an incorrect warn ID"
                }
                return
            }
        }

        removeUserWarn(
            userId = userId,
            warnId = warnId
        )
        sendInfractionToModLogChannel(
            Infraction.Unwarn(user, ctx.author)
        )
        ctx.respondPublic {
            content = "Successfully unwarned $memberMention"
        }
    }

    private suspend fun listWarns(ctx: CommandContext) {
        val user = ctx.args.users["user"]!!

        val member = user.asMember(config.guildSnowflake)
        val memberMention = member.mention

        if (!ctx.author.canInteractWith(member)) {
            ctx.respondEphemeral {
                content = "You don't have enough permissions to interact with $memberMention."
            }
            return
        }

        val warns = getUserWarns(user.id.toString())

        if (warns == null || warns.reasons.isEmpty()) {
            ctx.respondEphemeral {
                content = "${user.mention} has no warns"
            }
            return
        }

        ctx.respondPublic {
            embed {
                title = "Warns for ${user.tag}"
                warns.reasons.forEachIndexed { index, reason ->
                    field {
                        name = "Warn ${index + 1}"
                        value = reason.takeMax(1024)
                    }
                }
            }
        }
    }

}