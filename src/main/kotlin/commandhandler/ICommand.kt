package commandhandler

import commands.CommandTypes

interface ICommand {

    val commandName: String
    val commandDescription: String
    val commandType: CommandTypes
    val commandArguments: List<String>
    val commandAliases: List<String>
    val devOnly: Boolean

    fun execute(ctx: CommandContext)

}