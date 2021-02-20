package commands.quotes

import commandhandler.CommandContext
import commandhandler.CommandManager
import commands.BaseCommand
import commands.CommandType.Quotes
import ext.optional
import ext.useArguments
import ext.useCommandProperly

class Quote(
    private val commandManager: CommandManager
) : BaseCommand(
    commandName = "quote",
    commandDescription = "Quote manager",
    commandType = Quotes,
    commandArguments = mapOf("get | add | remove | addstar | removestar".optional())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        val event = ctx.event
        if (args.isNotEmpty()) {
            with(commandManager) {
                when (val command = args[0]) {
                    "get" -> execWithChecks(getCommand("getquote")!!, event, args.no0())
                    "add" -> execWithChecks(getCommand("addquote")!!, event, args.no0())
                    "remove" -> execWithChecks(getCommand("removequote")!!, event, args.no0())
                    "addstar" -> execWithChecks(getCommand(command)!!, event, args.no0())
                    "removestar" -> execWithChecks(getCommand(command)!!, event, args.no0())
                    else -> ctx.channel.useCommandProperly()
                }
            }
        } else {
            ctx.channel.useArguments(1)
        }

    }

    fun MutableList<String>.no0(): MutableList<String> = apply { removeAt(0) }

}