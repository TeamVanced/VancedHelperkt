package commands.moderation

import config
import core.command.CommandContext
import core.command.base.BaseCommand
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import database.deleteUserWarns
import database.getUserWarns
import dev.kord.common.annotation.KordPreview
import dev.kord.core.entity.interaction.int
import dev.kord.core.entity.interaction.string
import dev.kord.core.entity.interaction.user
import ext.takeMax

@OptIn(KordPreview::class)
class Warns : BaseCommand(
    commandName = "warn",
    commandDescription = "Warn actions"
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

    private suspend fun warnUser(ctx: CommandContext) {
        val user = ctx.args["user"]!!.user()
        val reason = ctx.args["reason"]!!.string()

        val userId = user.id.asString

        database.warnUser(
            userId = userId,
            userTag = user.tag,
            reason = reason
        )

        ctx.respond {
            content = "Successfully warned ${user.mention} for $reason"
        }

        val warns = getUserWarns(userId)

        if (warns != null && warns.reasons.size >= 3) {
            user.asMember(config.guildSnowflake).kick(
                reason = "Too many infractions"
            )
            deleteUserWarns(userId)
        }
    }

    private fun unwarnUser(ctx: CommandContext) {
        val user = ctx.args["user"]!!.user()
        val warnId = ctx.args["warnid"]?.int()

        val userId = user.id.asString

        if (warnId != null) {
            val warns = getUserWarns(userId)
            if (warnId == 0 || (warns != null && warnId > warns.reasons.size)) {
                ctx.respond {
                    content = "$warnId is an incorrect warn ID"
                }
                return
            }
        }

        database.unwarnUser(
            userId = userId,
            warnId = warnId
        )

        ctx.respond {
            content = "Successfully unwarned ${user.mention}"
        }
    }

    private fun listWarns(ctx: CommandContext) {
        val user = ctx.args["user"]!!.user()

        val warns = getUserWarns(user.id.asString)

        if (warns == null || warns.reasons.isEmpty()) {
            ctx.respond {
                content = "${user.mention} has no warns"
            }
            return
        }

        ctx.respond {
            embed {
                title = "Warns for ${user.mention}"
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