package ext

import commands.BaseCommand
import net.dv8tion.jda.api.entities.TextChannel

fun TextChannel.useCommandProperly(baseCommand: BaseCommand) {
    with(baseCommand) {
        sendMessage("Use the command properly!").queueAddReaction()
    }
}

fun TextChannel.useArguments(argumentCount: Int, baseCommand: BaseCommand) {
    with(baseCommand) {
        sendMessage("You need to provide at least $argumentCount argument(s)!").queueAddReaction()
    }
}

