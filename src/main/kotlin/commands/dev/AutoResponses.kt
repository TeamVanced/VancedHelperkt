package commands.dev

import core.command.CommandContext
import core.command.base.BaseCommand
import core.database.*
import core.util.botOwners
import core.wrapper.applicationcommand.CustomApplicationCommandCreateBuilder
import core.wrapper.applicationcommand.CustomApplicationCommandPermissionBuilder
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.interaction.int
import dev.kord.core.entity.interaction.string
import dev.kord.rest.builder.interaction.group
import dev.kord.rest.builder.interaction.int
import dev.kord.rest.builder.interaction.string

class AutoResponses : BaseCommand(
    commandName = "autoresponses",
    commandDescription = "Manages automatic responses on certain keywords",
    defaultPermissions = false
) {

    override suspend fun execute(ctx: CommandContext) {
        val subCommandGroup = ctx.subCommandGroup ?: return

        when (subCommandGroup.groupName) {
            "keywords" -> {
                when (subCommandGroup.name) {
                    "add" -> {
                        val keyword = ctx.args["keyword"]!!.string()
                        val indexOfResponse = ctx.args["index"]!!.int().toInt()
                        addKeyword(keyword, indexOfResponse)
                        ctx.respondEphemeral {
                            content = "Successfully added `$keyword` keyword  to automatic responses."
                        }
                    }
                    "list" -> {
                        val groupedKeywords = getKeywords().sortedBy {
                            it.indexOfResponse
                        }.groupBy(
                            keySelector = {
                                it.indexOfResponse
                            },
                            valueTransform = {
                                it.keyword
                            }
                        )
                        ctx.respondEphemeral {
                            embed {
                                title = "Keywords"
                                for ((indexOfResponse, keywords) in groupedKeywords) {
                                    field("Response index $indexOfResponse") {
                                        keywords.joinToString(", ")
                                    }
                                }
                            }
                        }
                    }
                    "remove" -> {
                        val keyword = ctx.args["keyword"]!!.string()
                        removeKeyword(keyword)
                        ctx.respondEphemeral {
                            content = "Successfully removed `$keyword` keyword from automatic responses."
                        }
                    }
                }
            }
            "responses" -> {
                when (subCommandGroup.name) {
                    "add" -> {
                        val message = ctx.args["message"]!!.string()
                        val index = addResponse(message)
                        ctx.respondEphemeral {
                            content = "Successfully added a response to automatic responses at index `$index`."
                        }
                    }
                    "list" -> {
                        val responses = getResponses().sortedBy { it.index }
                        ctx.respondEphemeral {
                            embed {
                                title = "Responses"
                                for ((message, index) in responses) {
                                    field {
                                        name = "Index $index"
                                        value = message
                                    }
                                }
                            }
                        }
                    }
                    "remove" -> {
                        val index = ctx.args["index"]!!.int().toInt()
                        removeResponse(index)
                        ctx.respondEphemeral {
                            content = "Successfully removed a response at index `$index` from automatic responses."
                        }
                    }
                }
            }
        }
    }

    override suspend fun commandOptions() =
        CustomApplicationCommandCreateBuilder {
            group(
                name = "keywords",
                description = "Edit the autoresponses keywords"
            ) {
                subCommand(
                    name = "add",
                    description = "Add a keyword to automatic responses"
                ) {
                    required = true
                    string(
                        name = "keyword",
                        description = "Keyword that activates the response"
                    ) {
                        required = true
                    }
                    int(
                        name = "index",
                        description = "Index of the response"
                    ) {
                        required = true
                    }
                }
                subCommand(
                    name = "list",
                    description = "List the keywords that activate a response"
                ) {
                    required = true
                }
                subCommand(
                    name = "remove",
                    description = "Remove a keyword from automatic responses"
                ) {
                    required = true
                    string(
                        name = "keyword",
                        description = "Keyword that activates the response"
                    ) {
                        required = true
                    }
                }
            }
            group(
                name = "responses",
                description = "Edit the autoresponses responses"
            ) {
                subCommand(
                    name = "add",
                    description = "Add a response to automatic responses"
                ) {
                    required = true
                    string(
                        name = "message",
                        description = "Message to add to the response list"
                    ) {
                        required = true
                    }
                }
                subCommand(
                    name = "list",
                    description = "List the responses with their indexes"
                ) {
                    required = true
                }
                subCommand(
                    name = "remove",
                    description = "Remove a response from automatic responses"
                ) {
                    required = true
                    int(
                        name = "index",
                        description = "Index of the response"
                    ) {
                        required = true
                    }
                }
            }
        }

    override fun commandPermissions() =
        CustomApplicationCommandPermissionBuilder{
            for (owner in botOwners) {
                user(
                    id = Snowflake(owner),
                    allow = true
                )
            }
        }

}