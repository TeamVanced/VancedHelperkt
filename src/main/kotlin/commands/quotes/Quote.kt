package commands.quotes

import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandTypes.Quotes
import ext.useArguments
import ext.useCommandProperly

class Quote(
    private val commandManager: CommandManager
) : BaseCommand(
    commandName = "quote",
    commandDescription = "Quote manager",
    commandType = Quotes,
    commandArguments = listOf("<get | add | remove | addstar | removestar>")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            with(commandManager) {
                when (val command = args[0]) {
                    "get" -> execWithChecks(getCommand("getquote")!!, event, args)
                    "add" -> execWithChecks(getCommand("addquote")!!, event, args)
                    "remove" -> execWithChecks(getCommand("removequote")!!, event, args)
                    "addstar" -> execWithChecks(getCommand(command)!!, event, args)
                    "removestar" -> execWithChecks(getCommand(command)!!, event, args)
                    else -> ctx.channel.useCommandProperly(this@Quote)
                }
            }
        } else {
            channel.useArguments(1, this)
        }

    }

}