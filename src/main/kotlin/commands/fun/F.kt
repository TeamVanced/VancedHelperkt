package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import type.CommandType.Fun
import ext.optional
import utils.pressF

class F : BaseCommand(
    commandName = "f",
    commandDescription = "F",
    commandType = Fun,
    commandArguments = mapOf("Thing to pay respect for".optional()),
    commandAliases = listOf("pressf", "payrespects")
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            ctx.event.channel.sendMsg("$pressF ${ctx.author.name} pays respect for ${args.joinToString(" ")}")
        } else {
            ctx.event.channel.sendMsg(pressF)
        }
    }

}