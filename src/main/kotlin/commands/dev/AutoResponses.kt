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
            "add" -> {
                when (subCommandGroup.name) {
                    "keyword" -> {
                        val keyword = ctx.args["keyword"]!!.string()
                        val indexOfResponse = ctx.args["index"]!!.int().toInt()
                        addKeyword(keyword, indexOfResponse)
                        ctx.respondEphemeral {
                            content = "Successfully added `$keyword` keyword  to automatic responses."
                        }
                    }
                    "response" -> {
                        val message = ctx.args["message"]!!.string()
                        val index = addResponse(message)
                        ctx.respondEphemeral {
                            content = "Successfully added a response to automatic responses at index `$index`."
                        }
                    }
                }
            }
            "list" -> {
                when (subCommandGroup.name) {
                    "keywords" -> {
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
                                    field("Respond index $indexOfResponse") {
                                        keywords.joinToString(", ")
                                    }
                                }
                            }
                        }
                    }
                    "responses" -> {
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
                }
            }
            "remove" -> {
                when (subCommandGroup.name) {
                    "keyword" -> {
                        val keyword = ctx.args["keyword"]!!.string()
                        removeKeyword(keyword)
                        ctx.respondEphemeral {
                            content = "Successfully removed `$keyword` keyword from automatic responses."
                        }
                    }
                    "response" -> {
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
                name = "add",
                description = "Add items to automatic responses"
            ) {
                subCommand(
                    name = "keyword",
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
                    name = "response",
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
            }
            group(
                name = "list",
                description = "List automatic response items"
            ) {
                subCommand(
                    name = "keywords",
                    description = "List of keywords that activate a response"
                ) {
                    required = true
                }
                subCommand(
                    name = "responses",
                    description = "List of responses"
                ) {
                    required = true
                }
            }
            group(
                name = "remove",
                description = "Remove items from the automatic responses"
            ) {
                subCommand(
                    name = "keyword",
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
                subCommand(
                    name = "response",
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