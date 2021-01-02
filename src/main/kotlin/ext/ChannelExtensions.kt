package ext

import commands.BaseCommand
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.useCommandProperly(baseCommand: BaseCommand) {
    sendMessage("Use the command properly!").queue {
        baseCommand.messageId = it.id
    }
}

fun TextChannel.useArguments(argumentCount: Int, baseCommand: BaseCommand) {
    sendMessage("You need to provide at least $argumentCount argument(s)!").queue {
        baseCommand.messageId = it.id
    }
}

