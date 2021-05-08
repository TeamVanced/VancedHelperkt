package commands.quotes

import commandhandler.CommandContext
import commands.base.BaseCommand
import type.CommandType.Quotes
import ext.optional
import ext.useArguments
import ext.useCommandProperly

class Quote  : BaseCommand(
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
                val command = when (val cmd = args[0].toLowerCase()) {
                    "get" -> getCommand("getquote")
                    "add" -> getCommand("addquote")
                    "remove" -> getCommand("removequote")
                    else -> getCommand(cmd)
                }
                if (command != null) {
                    execWithChecks(
                        command,
                        event,
                        args.no0
                    )
                } else {
                    ctx.channel.useCommandProperly()
                }
            }
        } else {
            ctx.channel.useArguments(1)
        }

    }

    private val MutableList<String>.no0: MutableList<String> get() = apply { removeAt(0) }

}