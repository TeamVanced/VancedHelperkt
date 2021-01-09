package commandhandler

import commands.ArgumentType
import commands.CommandType

interface ICommand {

    val commandName: String
    val commandDescription: String
    val commandType: CommandType
    val commandArguments: Map<String, ArgumentType>
    val commandAliases: List<String>
    val devOnly: Boolean

    fun execute(ctx: CommandContext)

}