package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.*
import core.database.data.MongoMutableList
import core.util.botOwners
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.interaction.channel
import dev.kord.rest.builder.interaction.group
import dev.kord.rest.builder.interaction.role
import dev.kord.rest.builder.interaction.subCommand

class Settings : BaseCommand(
    commandName = "settings",
    commandDescription = "Configure settings",
    defaultPermissions = false
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
                    "whitelisted_spam_channels" -> {
                        when (subCommandGroup.name) {
                            "add" -> addWhitelistedSpamChannel(ctx)
                            "remove" -> removeWhitelistedSpamChannel(ctx)
                        }
                    }
                    "whitelisted_autoresp_channels" -> {
                        when (subCommandGroup.name) {
                            "add" -> addWhitelistedAutoresponsesChannel(ctx)
                            "remove" -> removeWhitelistedAutoresponsesChannel(ctx)
                        }
                    }
                }
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder {
            subCommand(
                name = "muterole",
                description = "Set the mute role"
            ) {
                role(
                    name = "role",
                    description = "Role to assign",
                    builder = {
                        required = true
                    }
                )
            }
            subCommand(
                name = "logchannel",
                description = "Set the log channel"
            ) {
                channel(
                    name = "channel",
                    description = "Channel to use as the log channel",
                    builder = {
                        required = true
                    }
                )
            }
            subCommand(
                name = "modlogchannel",
                description = "Set the moderator action log channel"
            ) {
                channel(
                    name = "channel",
                    description = "Channel to use as the moderator action log channel",
                    builder = {
                        required = true
                    }
                )
            }
            subCommand(
                name = "errorchannel",
                description = "Set the error log channel"
            ) {
                channel(
                    name = "channel",
                    description = "Channel to use as the error log channel",
                    builder = {
                        required = true
                    }
                )
            }
            group(
                name = "moderators",
                description = "Edit moderators"
            ) {
                subCommand(
                    name = "add",
                    description = "Add a moderator"
                ) {
                    role(
                        name = "role",
                        description = "Role to add to moderators",
                        builder = {
                            required = true
                        }
                    )
                }
                subCommand(
                    name = "remove",
                    description = "Remove a moderator"
                ) {
                    role(
                        name = "role",
                        description = "Role to remove from moderators",
                        builder = {
                            required = true
                        }
                    )
                }
            }
            group(
                name = "quoters",
                description = "Edit roles that can add quotes"
            ) {
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
                    description = "Removed allowed quote role"
                ) {
                    role(
                        name = "role",
                        description = "Role to remove from allowed quote roles",
                        builder = {
                            required = true
                        }
                    )
                }
            }
            group(
                name = "colourmes",
                description = "Edit roles that can use colourme"
            ) {
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
                    description = "Removed allowed colourme role"
                ) {
                    role(
                        name = "role",
                        description = "Role to remove from allowed colourme roles",
                        builder = {
                            required = true
                        }
                    )
                }
            }
            group(
                name = "whitelisted_spam_channels",
                description = "Edit channels where spam is allowed"
            ) {
                subCommand(
                    name = "add",
                    description = "Add a channel",
                    builder = {
                        channel(
                            name = "channel",
                            description = "Channel where spam should be allowed"
                        ) {
                            required = true
                        }
                    }
                )
                subCommand(
                    name = "remove",
                    description = "Removed a channel"
                ) {
                    channel(
                        name = "channel",
                        description = "Channel where spam is allowed"
                    ) {
                        required = true
                    }
                }
            }
            group(
                name = "whitelisted_autoresp_channels",
                description = "Edit channels where bot enforces autoresponses"
            ) {
                subCommand(
                    name = "add",
                    description = "Add a channel",
                    builder = {
                        channel(
                            name = "channel",
                            description = "Channel where autoresponses should be enforced"
                        ) {
                            required = true
                        }
                    }
                )
                subCommand(
                    name = "remove",
                    description = "Removed a channel"
                ) {
                    channel(
                        name = "channel",
                        description = "Channel where autoresponses are enforced"
                    ) {
                        required = true
                    }
                }
            }
        }

    override fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder(
            permissions = {
                for (owner in botOwners) {
                    user(
                        id = Snowflake(owner),
                        allow = true
                    )
                }
            }
        )

    private suspend fun configureLogChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        logChannelId = channel.id.value.toLong()
        ctx.respondEphemeral {
            content = "Successfully updated the log channel"
        }
    }

    private suspend fun configureModLogChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        modLogChannelId = channel.id.value.toLong()
        ctx.respondEphemeral {
            content = "Successfully updated the moderator action log channel"
        }
    }

    private suspend fun configureErrorChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        errorChannelId = channel.id.value.toLong()
        ctx.respondEphemeral {
            content = "Successfully updated the error log channel"
        }
    }

    private suspend fun configureMuteRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        muteRoleId = role.id.value.toLong()
        ctx.respondEphemeral {
            content = "Successfully updated the mute role"
        }
    }

    private suspend fun configureBoosterRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        boosterRoleId = role.id.value.toLong()
        ctx.respondEphemeral {
            content = "Successfully updated the booster role"
        }
    }

    private suspend fun addModerator(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        moderatorRoleIds.addWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "moderators",
            mention = role.mention
        )
    }

    private suspend fun removeModerator(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        moderatorRoleIds.removeWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "moderators",
            mention = role.mention
        )
    }

    private suspend fun addQuoteRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        allowedQuoteRoleIds.addWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "quoters",
            mention = role.mention
        )
    }

    private suspend fun removeQuoteRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        allowedQuoteRoleIds.removeWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "quoters",
            mention = role.mention
        )
    }

    private suspend fun addColourMeRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        allowedColourMeRoleIds.addWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "fruities",
            mention = role.mention
        )
    }

    private suspend fun removeColourMeRole(ctx: CommandContext) {
        val role = ctx.args.roles["role"]!!

        allowedColourMeRoleIds.removeWithChecks(
            ctx = ctx,
            element = role.id.value.toLong(),
            itemName = "fruities",
            mention = role.mention
        )
    }

    private suspend fun addWhitelistedSpamChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        whitelistedSpamChannelIds.addWithChecks(
            ctx = ctx,
            element = channel.id.value.toLong(),
            itemName = "whitelisted spam channels",
            mention = channel.mention
        )
    }
    
    private suspend fun removeWhitelistedSpamChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        whitelistedSpamChannelIds.removeWithChecks(
            ctx = ctx,
            element = channel.id.value.toLong(),
            itemName = "whitelisted spam channels",
            mention = channel.mention
        )
    }

    private suspend fun addWhitelistedAutoresponsesChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        whitelistedAutoresponsesChannelIds.addWithChecks(
            ctx = ctx,
            element = channel.id.value.toLong(),
            itemName = "whitelisted Autoresponses channels",
            mention = channel.mention
        )
    }
    
    private suspend fun removeWhitelistedAutoresponsesChannel(ctx: CommandContext) {
        val channel = ctx.args.channels["channel"]!!

        whitelistedAutoresponsesChannelIds.removeWithChecks(
            ctx = ctx,
            element = channel.id.value.toLong(),
            itemName = "whitelisted Autoresponses channels",
            mention = channel.mention
        )
    }

    private suspend fun <E, TDocument> MongoMutableList<E, TDocument>.addWithChecks(
        ctx: CommandContext,
        element: E,
        itemName: String,
        mention: String
    ) {
        if (!contains(element)) {
            add(element)
            ctx.respondEphemeral {
                content = "Successfully added $mention to $itemName"
            }
        } else {
            ctx.respondEphemeral {
                content = "$mention already exists in $itemName"
            }
        }
    }

    private suspend fun <E, TDocument> MongoMutableList<E, TDocument>.removeWithChecks(
        ctx: CommandContext,
        element: E,
        itemName: String,
        mention: String
    ) {
        if (remove(element)) {
            ctx.respondEphemeral {
                content = "Successfully removed $mention from $itemName"
            }
        } else {
            ctx.respondEphemeral {
                content = "$mention doesn't exist in $itemName"
            }
        }
    }

}