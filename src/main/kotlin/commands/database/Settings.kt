package commands.database

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Database
import database.*
import ext.required
import ext.useArguments
import ext.useCommandProperly
import org.litote.kmongo.eq

class Settings : BaseCommand(
    commandName = "settings",
    commandDescription = "Configurable settings for helper",
    commandType = Database,
    commandArguments = mapOf("prefix | boosterchat | boosterrole | muterole | modlogchannel | infochannel | errorchannel | addowner | removeowner | addquoterole | removequoterole | addcolourmerole | removecolourmerole | clear".required()),
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
                        ctx.event.channel.sendMsg("Successfully set the prefix to `$value`!")
                    }
                    "boosterchat" -> {
                        boosterChat = value
                        ctx.event.channel.sendMsg("Successfully set the booster chat to `$value`!")
                    }
                    "boosterrole" -> {
                        boosterRole = value
                        ctx.event.channel.sendMsg("Successfully set the booster role to `$value`!")
                    }
                    "muterole" -> {
                        muteRole = value
                        ctx.event.channel.sendMsg("Successfully set the mute role to `$value`!")
                    }
                    "logchannel" -> {
                        logChannel = value
                        ctx.event.channel.sendMsg("Successfully set the log channel to `$value`!")
                    }
                    "modlogchannel" -> {
                        modlogChannel = value
                        ctx.event.channel.sendMsg("Successfully set the mod log channel to `$value`!")
                    }
                    "boosterchannel" -> {
                        boosterChannel = value
                        ctx.event.channel.sendMsg("Successfully set the booster channel to `$value`!")
                    }
                    "errorchannel" -> {
                        errorChannel = value
                        ctx.event.channel.sendMsg("Successfully set the error channel to `$value`!")
                    }
                    "addowner" -> {
                        if (owners.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` already exists in the collection!")
                            return@with
                        }
                        addOwner(value)
                        ctx.event.channel.sendMsg("Successfully added `$value` to owners!")
                    }
                    "removeowner" -> {
                        if (!owners.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` does not exist in the collection!")
                            return@with
                        }
                        removeOwner(value)
                        ctx.event.channel.sendMsg("Successfully removed `$value` from owners!")
                    }
                    "addmodrole" -> {
                        if (modRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` already exists in the collection!")
                            return@with
                        }
                        addModRole(value)
                        ctx.event.channel.sendMsg("Successfully added `$value` to moderators!")
                    }
                    "removemodrole" -> {
                        if (!modRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` does not exist in the collection!")
                            return@with
                        }
                        removeModRole(value)
                        ctx.event.channel.sendMsg("Successfully removed `$value` from moderators!")
                    }
                    "addquoterole" -> {
                        if (quoteRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` already exists in the collection!")
                            return@with
                        }
                        addQuoteRole(value)
                        ctx.event.channel.sendMsg("Successfully added `$value` to allowed quote roles!")
                    }
                    "removequoterole" -> {
                        if (!quoteRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` does not exist in the collection!")
                            return@with
                        }
                        removeQuoteRole(value)
                        ctx.event.channel.sendMsg("Successfully removed `$value` from allowed quote roles!")
                    }
                    "addcolourmerole" -> {
                        if (colourmeRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` already exists in the collection!")
                            return@with
                        }
                        addColourmeRole(value)
                        ctx.event.channel.sendMsg("Successfully added `$value` to allowed colourme roles!")
                    }
                    "removecolourmerole" -> {
                        if (!colourmeRoles.contains(value)) {
                            ctx.event.channel.sendMsg("`$value` does not exist in the collection!")
                            return@with
                        }
                        removeColourmeRole(value)
                        ctx.event.channel.sendMsg("Successfully removed `$value` from allowed quote roles!")
                    }
                    "clear" -> {
                        settingsCollection.findOneAndDelete(Settings::guildId eq this)
                        ctx.event.channel.sendMsg("Successfully cleared settings")
                    }
                    else -> {
                        ctx.channel.useCommandProperly()
                    }
                }
            }
        } else {
            ctx.channel.useArguments(2)
        }
    }

}