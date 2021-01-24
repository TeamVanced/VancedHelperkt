package ext

import commands.BaseCommand
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

fun BaseCommand.useCommandProperly() {
    sendMessage("Use the command properly!")
}

fun BaseCommand.useArguments(argumentCount: Int) {
    sendMessage("You need to provide at least $argumentCount argument(s)!")
}

fun TextChannel.sendMsg(message: String, onComplete: (message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(this, Permission.MESSAGE_WRITE)) {
            sendMessage(message).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}

fun TextChannel.sendMsg(embed: MessageEmbed, onComplete:(message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(this, Permission.MESSAGE_WRITE)) {
            sendMessage(embed).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}



