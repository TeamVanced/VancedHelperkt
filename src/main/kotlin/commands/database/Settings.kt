package commands.database

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Database
import database.*
import ext.useArguments
import ext.useCommandProperly

class Settings : BaseCommand(
    commandName = "settings",
    commandDescription = "Configurable settings for helper",
    commandType = Database,
    commandArguments = listOf("<prefix | boosterchannel | modlogchannel | infochannel | errorchannel>"),
    devOnly = true
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty() && args.size == 2) {
            val value = args[1]
            with(ctx.guild.id) {
                when (args[0]) {
                    "prefix" -> {
                        prefix = value
                        channel.sendMessage("Successfully set the prefix to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "boosterchat" -> {
                        boosterChat = value
                        channel.sendMessage("Successfully set the booster chat to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "muterole" -> {
                        muteRole = value
                        channel.sendMessage("Successfully set the mute role to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "boosterrole" -> {
                        boosterRole = value
                        channel.sendMessage("Successfully set the booster role to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "logchannel" -> {
                        logChannel = value
                        channel.sendMessage("Successfully set the log channel to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "modlogchannel" -> {
                        modlogChannel = value
                        channel.sendMessage("Successfully set the mod log channel to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "boosterchannel" -> {
                        boosterChannel = value
                        channel.sendMessage("Successfully set the booster channel to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "errorchannel" -> {
                        errorChannel = value
                        channel.sendMessage("Successfully set the error channel to `$value`!").queue {
                            messageId = it.id
                        }
                    }
                    "addowner" -> {
                        if (owners.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        addOwner(value)
                        channel.sendMessage("Successfully added `$value` to owners!").queue {
                            messageId = it.id
                        }
                    }
                    "removeowner" -> {
                        if (!owners.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        removeOwner(value)
                        channel.sendMessage("Successfully removed `$value` from owners!").queue {
                            messageId = it.id
                        }
                    }
                    "addmodrole" -> {
                        if (modRoles.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        addModRole(value)
                        channel.sendMessage("Successfully added `$value` to moderators!").queue {
                            messageId = it.id
                        }
                    }
                    "removemodrole" -> {
                        if (!modRoles.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        removeModRole(value)
                        channel.sendMessage("Successfully removed `$value` from moderators!").queue {
                            messageId = it.id
                        }
                    }
                    "addquoterole" -> {
                        if (quoteRoles.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        addQuoteRole(value)
                        channel.sendMessage("Successfully added `$value` to allowed quote roles!").queue {
                            messageId = it.id
                        }
                    }
                    "removequoterole" -> {
                        if (!quoteRoles.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queue {
                                messageId = it.id
                            }
                            return@with
                        }
                        removeQuoteRole(value)
                        channel.sendMessage("Successfully removed `$value` from allowed quote roles!").queue {
                            messageId = it.id
                        }
                    }
                    else -> {
                        channel.useCommandProperly(this@Settings)
                    }
                }
            }
        } else {
            channel.useArguments(2, this)
        }
    }

}