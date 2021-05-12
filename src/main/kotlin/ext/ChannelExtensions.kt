package ext

import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.TextChannel

fun Message.useCommandProperly() {
    replyWithChecks("Use the command properly!")
}

fun Message.useArguments(argumentCount: Int) {
    replyWithChecks("You need to provide at least $argumentCount argument(s)!")
}

fun Message.replyWithChecks(message: String, onComplete: (message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(textChannel, Permission.MESSAGE_WRITE)) {
            reply(message).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}

fun Message.replyWithChecks(embed: MessageEmbed, onComplete:(message: Message) -> Unit = {}) {
    println("issued")
    try {
        if (guild.selfMember.hasPermission(textChannel, Permission.MESSAGE_WRITE)) {
            reply(embed).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
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

fun TextChannel.sendMessageWithChecks(embed: MessageEmbed, onComplete: (message: Message) -> Unit = {}) {
    try {
        if (guild.selfMember.hasPermission(this, Permission.MESSAGE_WRITE)) {
            sendMessage(embed).queue {
                onComplete(it)
            }
        }
    } catch (e: Exception) {}
}



