package commands.database

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandTypes.Database
import database.*
import ext.useArguments
import ext.useCommandProperly
import org.litote.kmongo.eq

class Settings : BaseCommand(
    commandName = "settings",
    commandDescription = "Configurable settings for helper",
    commandType = Database,
    commandArguments = listOf("<prefix | boosterchat | boosterrole | muterole | modlogchannel | infochannel | errorchannel | addowner | removeowner | addquoterole | removequoterole | addcolourmerole | removecolourmerole | clear>"),
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
                        channel.sendMessage("Successfully set the prefix to `$value`!").queueAddReaction()
                    }
                    "boosterchat" -> {
                        boosterChat = value
                        channel.sendMessage("Successfully set the booster chat to `$value`!").queueAddReaction()
                    }
                    "boosterrole" -> {
                        boosterRole = value
                        channel.sendMessage("Successfully set the booster role to `$value`!").queueAddReaction()
                    }
                    "muterole" -> {
                        muteRole = value
                        channel.sendMessage("Successfully set the mute role to `$value`!").queueAddReaction()
                    }
                    "logchannel" -> {
                        logChannel = value
                        channel.sendMessage("Successfully set the log channel to `$value`!").queueAddReaction()
                    }
                    "modlogchannel" -> {
                        modlogChannel = value
                        channel.sendMessage("Successfully set the mod log channel to `$value`!").queueAddReaction()
                    }
                    "boosterchannel" -> {
                        boosterChannel = value
                        channel.sendMessage("Successfully set the booster channel to `$value`!").queueAddReaction()
                    }
                    "errorchannel" -> {
                        errorChannel = value
                        channel.sendMessage("Successfully set the error channel to `$value`!").queueAddReaction()
                    }
                    "addowner" -> {
                        if (owners.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queueAddReaction()
                            return@with
                        }
                        addOwner(value)
                        channel.sendMessage("Successfully added `$value` to owners!").queueAddReaction()
                    }
                    "removeowner" -> {
                        if (!owners.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queueAddReaction()
                            return@with
                        }
                        removeOwner(value)
                        channel.sendMessage("Successfully removed `$value` from owners!").queueAddReaction()
                    }
                    "addmodrole" -> {
                        if (modRoles.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queueAddReaction()
                            return@with
                        }
                        addModRole(value)
                        channel.sendMessage("Successfully added `$value` to moderators!").queueAddReaction()
                    }
                    "removemodrole" -> {
                        if (!modRoles.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queueAddReaction()
                            return@with
                        }
                        removeModRole(value)
                        channel.sendMessage("Successfully removed `$value` from moderators!").queueAddReaction()
                    }
                    "addquoterole" -> {
                        if (quoteRoles.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queueAddReaction()
                            return@with
                        }
                        addQuoteRole(value)
                        channel.sendMessage("Successfully added `$value` to allowed quote roles!").queueAddReaction()
                    }
                    "removequoterole" -> {
                        if (!quoteRoles.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queueAddReaction()
                            return@with
                        }
                        removeQuoteRole(value)
                        channel.sendMessage("Successfully removed `$value` from allowed quote roles!").queueAddReaction()
                    }
                    "addcolourmerole" -> {
                        if (colourmeRoles.contains(value)) {
                            channel.sendMessage("`$value` already exists in the collection!").queueAddReaction()
                            return@with
                        }
                        addColourmeRole(value)
                        channel.sendMessage("Successfully added `$value` to allowed colourme roles!").queueAddReaction()
                    }
                    "removecolourmerole" -> {
                        if (!colourmeRoles.contains(value)) {
                            channel.sendMessage("`$value` does not exist in the collection!").queueAddReaction()
                            return@with
                        }
                        removeColourmeRole(value)
                        channel.sendMessage("Successfully removed `$value` from allowed quote roles!").queueAddReaction()
                    }
                    "clear" -> {
                        settingsCollection.findOneAndDelete(Settings::guildId eq this)
                        channel.sendMessage("Successfully cleared settings").queueAddReaction()
                    }
                    else -> {
                        useCommandProperly()
                    }
                }
            }
        } else {
            useArguments(2)
        }
    }

}