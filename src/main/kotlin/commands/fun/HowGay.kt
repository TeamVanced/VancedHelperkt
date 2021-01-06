package commands.`fun`

import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandTypes.Fun

class HowGay(
    private val commandManager: CommandManager
) : BaseCommand(
    commandName = "howgay",
    commandDescription = "Calculate gayness",
    commandType = Fun,
    commandArguments = listOf("[The thing]"),
    commandAliases = listOf("gay")
) {
    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        val howcmd = commandManager.getCommand("how")
        if (args.isNotEmpty()) {
            howcmd?.execute(CommandContext(ctx.event, mutableListOf("gay | ${args.joinToString(" ")}")))
        } else {
            val user = ctx.author.asMention
            howcmd?.execute(CommandContext(ctx.event, mutableListOf("gay | $user")))
        }

    }
}