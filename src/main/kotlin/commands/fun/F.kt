package commands.`fun`

import commandhandler.CommandContext
import commands.base.BaseCommand
import ext.optional
import type.CommandType.Fun
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
            ctx.message.replyMsg("$pressF ${ctx.author.name} pays respect for ${args.joinToString(" ")}")
        } else {
            ctx.message.replyMsg(pressF)
        }
    }

}