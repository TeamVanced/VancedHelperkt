package ext

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.useCommandProperly() {
    sendMessageWithChecks("Use the command properly!")
}

fun TextChannel.useArguments(argumentCount: Int) {
    sendMessageWithChecks("You need to provide at least $argumentCount argument(s)!")
}

fun TextChannel.sendMessageWithChecks(message: String, onComplete: (message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(this, Permission.MESSAGE_WRITE)) {
            sendMessage(message).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}

fun TextChannel.sendMessageWithChecks(embed: MessageEmbed, onComplete:(message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(this, Permission.MESSAGE_WRITE)) {
            sendMessage(embed).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}



