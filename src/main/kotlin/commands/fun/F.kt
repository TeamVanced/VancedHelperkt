package commands.`fun`

import commandhandler.CommandContext
import commands.BaseCommand
import commands.CommandType.Fun
import ext.optional
import utils.pressF

class F : BaseCommand(
    commandName = "f",
    commandDescription = "F",
    commandType = Fun,
    commandArguments = mapOf("Thing to pay respect for".optional())
) {

    override fun execute(ctx: CommandContext) {
        super.execute(ctx)
        val args = ctx.args
        if (args.isNotEmpty()) {
            sendMessage("$pressF ${ctx.author.name} pays respect for ${args.joinToString(" ")}")
        } else {
            sendMessage(pressF)
        }
    }

}