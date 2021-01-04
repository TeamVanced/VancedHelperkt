package ext

import commands.BaseCommand

fun BaseCommand.useCommandProperly() {
    channel.sendMessage("Use the command properly!").queueAddReaction()
}

fun BaseCommand.useArguments(argumentCount: Int) {
    channel.sendMessage("You need to provide at least $argumentCount argument(s)!").queueAddReaction()
}

